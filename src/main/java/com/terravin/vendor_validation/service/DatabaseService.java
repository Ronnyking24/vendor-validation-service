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
        String sql = "INSERT INTO vendors (company_name, contact_person, contact_email, phone, years_in_operation, employees, turnover, material, clients, certification_iso, certification_organic) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, vendor.getCompanyName());
            stmt.setString(2, vendor.getContactPerson());
            stmt.setString(3, vendor.getContactEmail());
            stmt.setString(4, vendor.getPhone());
            stmt.setInt(5, vendor.getYearsInOperation());
            stmt.setInt(6, vendor.getEmployees());
            stmt.setDouble(7, vendor.getTurnover());
            stmt.setString(8, vendor.getMaterial());
            stmt.setString(9, vendor.getClients());
            stmt.setBoolean(10, vendor.isCertificationIso());
            stmt.setBoolean(11, vendor.isCertificationOrganic());

            stmt.executeUpdate();
            System.out.println("Vendor saved to database.");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
