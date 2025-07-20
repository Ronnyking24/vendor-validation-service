package com.terravin.vendor_validation.service;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Service
public class PdfGenerator {

    public File generateSummary(VendorApplication vendor) {
        try {
            Document document = new Document();
            File file = new File(System.getProperty("java.io.tmpdir"), "summary_" + vendor.getCompanyName().replaceAll("\\s+","_") + ".pdf");
            PdfWriter.getInstance(document, new FileOutputStream(file));
            document.open();

            document.add(new Paragraph("Vendor Summary"));
            document.add(new Paragraph("Company: " + vendor.getCompanyName()));
            document.add(new Paragraph("Contact: " + vendor.getContactPerson()));
            document.add(new Paragraph("Phone: " + vendor.getPhone()));
            document.add(new Paragraph("Email: " + (vendor.getContactEmail() != null ? vendor.getContactEmail() : "N/A")));
            document.add(new Paragraph("Years in Operation: " + vendor.getYearsInOperation()));
            document.add(new Paragraph("Employees: " + vendor.getEmployees()));
            document.add(new Paragraph("Turnover: " + vendor.getTurnover()));
            document.add(new Paragraph("Material: " + vendor.getMaterial()));
            document.add(new Paragraph("Clients: " + (vendor.getClients() != null ? vendor.getClients() : "N/A")));
            document.add(new Paragraph("Certified: ISO(" + vendor.isCertificationIso() + "), Organic(" + vendor.isCertificationOrganic() + ")"));

            document.close();
            return file;
        } catch (DocumentException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
