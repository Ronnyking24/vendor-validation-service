package com.terravin.vendor_validation.controller;

import com.terravin.vendor_validation.model.VendorApplication;
import com.terravin.vendor_validation.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
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

    @PostMapping("/api/validate")
    public ResponseEntity<Map<String, Object>> apiValidate(
            @RequestParam Map<String, String> params,
            @RequestParam("application_pdf") MultipartFile pdfFile) {

        Map<String, Object> result = new HashMap<>();
        try {
            if (pdfFile == null || pdfFile.isEmpty()) {
                throw new IllegalArgumentException("No PDF file uploaded.");
            }

            String fileName = StringUtils.cleanPath(pdfFile.getOriginalFilename());
            File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);
            pdfFile.transferTo(tempFile);

            VendorApplication vendor = new VendorApplication();
            vendor.setCompanyName(params.get("companyName"));
            vendor.setContactPerson(params.get("contactPerson"));
            vendor.setContactEmail(params.get("email"));
            vendor.setPhone(params.get("phone"));
            vendor.setYearsInOperation(Integer.parseInt(params.get("yearsInOperation")));
            vendor.setEmployees(Integer.parseInt(params.get("employees")));
            vendor.setTurnover(Double.parseDouble(params.get("turnover")));
            vendor.setMaterial(params.get("material"));
            vendor.setClients(params.get("clients"));
            vendor.setCertificationOrganic("true".equalsIgnoreCase(params.get("certificationOrganic")));
            vendor.setCertificationIso("true".equalsIgnoreCase(params.get("certificationIso")));

            boolean isValid = pdfValidator.validatePDF(tempFile, vendor);

            if (isValid) {
                Date scheduledDate = visitScheduler.scheduleVisit(vendor);
                databaseService.saveVendor(vendor);
                File summaryPdf = pdfGenerator.generateSummary(vendor);
                emailService.sendApprovalEmail(vendor, tempFile, summaryPdf, scheduledDate);
                result.put("status", "approved");
                result.put("scheduledVisitDate", scheduledDate.toString());
            } else {
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
}
