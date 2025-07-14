package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username}")
    private String from;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendApprovalEmail(VendorApplication vendor) {
        String subject = "Vendor Application Approved";
        String body = "Dear " + vendor.getCompanyName() + ",\n\n"
                + "Congratulations! Your vendor application has passed all validation checks. "
                + "We’ve scheduled a visit to your facility and will reach out shortly.\n\n"
                + "Thank you for choosing Terravin.\n\nBest regards,\nTerravin Procurement Team";

        sendEmail(vendor.getContactEmail(), subject, body);
    }

    public void sendRejectionEmail(VendorApplication vendor) {
        String subject = "Vendor Application Feedback";
        String body = "Dear " + vendor.getCompanyName() + ",\n\n"
                + "Unfortunately, your vendor application did not meet our criteria in one or more areas "
                + "(e.g. financial stability, certifications, or regulatory compliance).\n\n"
                + "Please review your documents and consider reapplying with updated information.\n\n"
                + "We appreciate your interest in working with Terravin.\n\nBest regards,\nTerravin Procurement Team";

        sendEmail(vendor.getContactEmail(), subject, body);
    }

    private void sendEmail(String to, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            System.out.println("Email sent to " + to);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
