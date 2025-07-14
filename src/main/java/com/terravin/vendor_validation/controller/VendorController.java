package com.terravin.vendor_validation.controller;

import com.terravin.vendor_validation.model.VendorApplication;
import com.terravin.vendor_validation.service.PDFValidator;
import com.terravin.vendor_validation.service.EmailService;
import com.terravin.vendor_validation.service.VisitScheduler;
import com.terravin.vendor_validation.service.DatabaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Controller
public class VendorController {

    @Autowired
    private PDFValidator pdfValidator;

    @Autowired
    private EmailService emailService;

    @Autowired
    private VisitScheduler visitScheduler;

    @Autowired
    private DatabaseService databaseService;

    @GetMapping("/")
    public String showForm(Model model) {
        model.addAttribute("vendor", new VendorApplication());
        return "apply";
    }

    @PostMapping("/submit")
    public String handleSubmission(@ModelAttribute VendorApplication vendor,
                                   @RequestParam("pdf") MultipartFile pdf,
                                   Model model) {

        // Save uploaded PDF temporarily
        String fileName = StringUtils.cleanPath(pdf.getOriginalFilename());
        File tempFile = new File(System.getProperty("java.io.tmpdir"), fileName);

        try {
            pdf.transferTo(tempFile);
        } catch (IOException e) {
            model.addAttribute("message", "Failed to save uploaded file.");
            return "apply";
        }

        // Validate using PDF contents
        boolean isValid = pdfValidator.validatePDF(tempFile, vendor);

        if (isValid) {
            visitScheduler.scheduleVisit(vendor);
            databaseService.saveVendor(vendor);
            emailService.sendApprovalEmail(vendor);
            model.addAttribute("message", "Vendor approved. A visit has been scheduled.");
        } else {
            emailService.sendRejectionEmail(vendor);
            model.addAttribute("message", "Vendor rejected. Check your email for feedback.");
        }

        return "apply";
    }
}
