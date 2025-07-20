package com.terravin.vendor_validation.controller;

import com.terravin.vendor_validation.model.VendorApplication;
import com.terravin.vendor_validation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;

@RestController
@CrossOrigin
@EnableScheduling
public class VendorController {

    @Autowired
    private PDFValidator pdfValidator;

    @Autowired
    private VisitScheduler visitScheduler;

    @Autowired
    private PdfGenerator pdfGenerator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private DatabaseService databaseService;

    // 1. Manual validation API endpoint
    @PostMapping("/api/validate")
    public ResponseEntity<Map<String, Object>> apiValidate(
            @RequestParam Map<String, String> params,
            @RequestParam("application_pdf") MultipartFile pdfFile) {

        Map<String, Object> result = new HashMap<>();
        try {
            if (pdfFile == null || pdfFile.isEmpty()) {
                throw new IllegalArgumentException("No PDF file uploaded.");
            }

            // Save uploaded PDF temporarily
            String fileName = StringUtils.cleanPath(pdfFile.getOriginalFilename());
            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
            pdfFile.transferTo(tempFile);

            // Build VendorApplication object
            VendorApplication vendor = new VendorApplication();
            vendor.setUserId(parseLong(params.get("userId")));
            vendor.setCompanyName(params.get("companyName"));
            vendor.setContactPerson(params.get("contactPerson"));
            vendor.setContactEmail(params.get("email"));
            vendor.setPhone(params.get("phone"));
            vendor.setYearsInOperation(parseInt(params.get("yearsInOperation")));
            vendor.setEmployees(parseInt(params.get("employees")));
            vendor.setTurnover(parseDouble(params.get("turnover")));
            vendor.setMaterial(params.get("material"));
            vendor.setClients(params.get("clients"));
            vendor.setCertificationOrganic("true".equalsIgnoreCase(params.get("certificationOrganic")));
            vendor.setCertificationIso("true".equalsIgnoreCase(params.get("certificationIso")));
            vendor.setApplicationPdf(tempFile.getAbsolutePath());

            // Validate and process
            boolean isValid = pdfValidator.validatePDF(tempFile, vendor);

            if (isValid) {
                Date scheduledDate = visitScheduler.scheduleVisit(vendor);
                databaseService.saveVendor(vendor, true);
                File summaryPdf = pdfGenerator.generateSummary(vendor);
                emailService.sendApprovalEmail(vendor, tempFile, summaryPdf, scheduledDate);
                result.put("status", "approved");
                result.put("scheduledVisitDate", scheduledDate.toString());
            } else {
                databaseService.saveVendor(vendor, false);
                emailService.sendRejectionEmail(vendor);
                result.put("status", "rejected");
            }

            return ResponseEntity.ok(result);

        } catch (Exception e) {
            result.put("status", "error");
            result.put("message", e.getMessage());
            return ResponseEntity.status(500).body(result);
        }
    }

    // 2. Auto-validation every 60 seconds
    @Scheduled(fixedDelay = 60000)
    public void autoValidatePendingVendors() {
        System.out.println("⏳ Auto-Validation: Looking for pending vendor applications...");

        List<VendorApplication> pendingVendors = databaseService.getPendingVendors();

        for (VendorApplication vendor : pendingVendors) {
            try {
                File tempFile = new File(vendor.getApplicationPdf());
                if (!tempFile.exists()) {
                    System.out.println("⚠️ Missing PDF: " + vendor.getApplicationPdf());
                    continue;
                }

                boolean isValid = pdfValidator.validatePDF(tempFile, vendor);

                if (isValid) {
                    Date visitDate = visitScheduler.scheduleVisit(vendor);
                    databaseService.saveVendor(vendor, true);
                    File summaryPdf = pdfGenerator.generateSummary(vendor);
                    emailService.sendApprovalEmail(vendor, tempFile, summaryPdf, visitDate);
                    System.out.println("✅ Auto-approved: " + vendor.getCompanyName());
                } else {
                    databaseService.saveVendor(vendor, false);
                    emailService.sendRejectionEmail(vendor);
                    System.out.println("❌ Auto-rejected: " + vendor.getCompanyName());
                }

            } catch (Exception e) {
                System.out.println("🚨 Error during auto-validation: " + e.getMessage());
            }
        }
    }

    // Helpers for parsing safely
    private Long parseLong(String value) {
        try {
            return value != null ? Long.parseLong(value) : null;
        } catch (Exception e) {
            return null;
        }
    }

    private Integer parseInt(String value) {
        try {
            return value != null ? Integer.parseInt(value) : 0;
        } catch (Exception e) {
            return 0;
        }
    }

    private Double parseDouble(String value) {
        try {
            return value != null ? Double.parseDouble(value) : 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}
