package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;

@Service
public class PDFValidator {

    public boolean validatePDF(File file, VendorApplication formData) {
        try (PDDocument document = PDDocument.load(file)) {
            String pdfContent = new PDFTextStripper().getText(document).toLowerCase();

            return pdfContent.contains(formData.getCompanyName().toLowerCase())
                && pdfContent.contains(String.valueOf(formData.getYearsOfOperation()))
                && pdfContent.contains(String.valueOf(formData.getAnnualRevenue()))
                && pdfContent.contains(formData.getContactEmail().toLowerCase());

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
