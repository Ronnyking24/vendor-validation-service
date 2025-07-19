package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import jakarta.mail.internet.MimeMessage;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    @Value("${admin.email}")
    private String adminEmail;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendApprovalEmail(VendorApplication vendor, File certificate, File summary, Date visitDate) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setFrom(from);
            helper.setTo(adminEmail);
            helper.setSubject("✅ Vendor Approved: " + vendor.getCompanyName());

            String formattedDate = new SimpleDateFormat("EEEE, dd MMM yyyy").format(visitDate);
            String content = "Vendor: " + vendor.getCompanyName() + "\n\n"
                    + "A visit has been scheduled on: " + formattedDate + "\n\n"
                    + "Attached is the vendor's summary and submitted certificates.";

            helper.setText(content);

            helper.addAttachment("Vendor_Certificates.pdf", new FileSystemResource(certificate));
            helper.addAttachment("Vendor_Summary.pdf", new FileSystemResource(summary));

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendRejectionEmail(VendorApplication vendor) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setFrom(from);
            helper.setTo(vendor.getContactEmail());
            helper.setSubject("❌ Vendor Application Rejected");
            helper.setText("Dear " + vendor.getCompanyName() + ",\n\n"
                    + "Unfortunately, your application did not meet the requirements.\n"
                    + "Please review your documents and consider reapplying.\n\n"
                    + "Regards,\nTerravin Procurement Team");

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
