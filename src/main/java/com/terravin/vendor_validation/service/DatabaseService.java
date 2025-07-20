package com.terravin.vendor_validation.service;

import com.terravin.vendor_validation.model.VendorApplication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Service
public class DatabaseService {

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${spring.datasource.username}")
    private String dbUser;

    @Value("${spring.datasource.password}")
    private String dbPass;

    // Save vendor's validation result
    public void saveVendor(VendorApplication vendor, boolean isValid) {
    String sql = "INSERT INTO vendors (user_id, company_name, contact_person, contact_email, phone, years_in_operation, employees, turnover, material, clients, certification_organic, certification_iso, regulatory_compliance, validation_status, application_pdf) " +
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

    try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
         PreparedStatement stmt = conn.prepareStatement(sql)) {

        stmt.setLong(1, vendor.getUserId());   // user_id (foreign key)
        stmt.setString(2, vendor.getCompanyName());
        stmt.setString(3, vendor.getContactPerson());
        stmt.setString(4, vendor.getContactEmail());
        stmt.setString(5, vendor.getPhone());
        stmt.setInt(6, vendor.getYearsInOperation());
        stmt.setInt(7, vendor.getEmployees());
        stmt.setDouble(8, vendor.getTurnover());
        stmt.setString(9, vendor.getMaterial());
        stmt.setString(10, vendor.getClients());
        stmt.setBoolean(11, vendor.isCertificationOrganic());
        stmt.setBoolean(12, vendor.isCertificationIso());
        stmt.setBoolean(13, vendor.isRegulatoryCompliance());
        stmt.setString(14, isValid ? "approved" : "rejected");
        stmt.setString(15, vendor.getApplicationPdf());  // pdf filename/path

        stmt.executeUpdate();
        System.out.println("✅ Vendor saved to database.");

    } catch (Exception e) {
        e.printStackTrace();
    }
}


    // Get all vendors waiting for validation
    public List<VendorApplication> getPendingVendors() {
        List<VendorApplication> list = new ArrayList<>();
        String sql = "SELECT * FROM vendors WHERE validation_status = 'pending'";

        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                VendorApplication vendor = new VendorApplication();
                vendor.setUserId(rs.getLong("user_id"));
                vendor.setCompanyName(rs.getString("company_name"));
                vendor.setContactPerson(rs.getString("contact_person"));
                vendor.setPhone(rs.getString("phone"));
                vendor.setYearsInOperation(rs.getInt("years_in_operation"));
                vendor.setEmployees(rs.getInt("employees"));
                vendor.setTurnover(rs.getDouble("turnover"));
                vendor.setMaterial(rs.getString("material"));
                vendor.setClients(rs.getString("clients"));
                vendor.setCertificationIso(rs.getBoolean("certification_iso"));
                vendor.setCertificationOrganic(rs.getBoolean("certification_organic"));
                vendor.setApplicationPdf(rs.getString("application_pdf"));

                list.add(vendor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}
