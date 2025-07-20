package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class PDFValidator {

    public boolean validatePDF(File pdfFile, VendorApplication vendor) {
        // New strict validation logic
        boolean hasAllCertifications = vendor.isCertificationIso() && vendor.isCertificationOrganic() && vendor.isRegulatoryCompliance();
        boolean meetsFinancials = vendor.getTurnover() > 10000;
        boolean sufficientExperience = vendor.getYearsInOperation() >= 5;
        boolean enoughEmployees = vendor.getEmployees() > 50;

        return hasAllCertifications && meetsFinancials && sufficientExperience && enoughEmployees;
    }
}
