package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PDFValidator {

    public boolean validatePDF(File pdfFile, VendorApplication vendor) {
        // Simulated validation logic
        boolean hasValidCertification = vendor.isCertificationIso() || vendor.isCertificationOrganic();
        boolean meetsFinancials = vendor.getTurnover() > 10000;
        boolean sufficientExperience = vendor.getYearsInOperation() >= 2;

        return hasValidCertification && meetsFinancials && sufficientExperience;
    }
}
