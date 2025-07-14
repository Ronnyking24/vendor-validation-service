package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;

@Service
public class DatabaseService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    public void saveVendor(VendorApplication vendor) {
        String sql = "INSERT INTO vendors (company_name, years_of_operation, annual_revenue, certifications, regulations, contact_email) " +
                     "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vendor.getCompanyName());
            stmt.setInt(2, vendor.getYearsOfOperation());
            stmt.setDouble(3, vendor.getAnnualRevenue());
            stmt.setBoolean(4, vendor.isHasCertifications());
            stmt.setBoolean(5, vendor.isCompliesWithRegulations());
            stmt.setString(6, vendor.getContactEmail());

            stmt.executeUpdate();
            System.out.println("Vendor saved to database.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
