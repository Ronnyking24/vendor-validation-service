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
                 "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
                 "ON DUPLICATE KEY UPDATE " +
                 "company_name=VALUES(company_name), contact_person=VALUES(contact_person), contact_email=VALUES(contact_email), phone=VALUES(phone), years_in_operation=VALUES(years_in_operation), employees=VALUES(employees), turnover=VALUES(turnover), material=VALUES(material), clients=VALUES(clients), certification_organic=VALUES(certification_organic), certification_iso=VALUES(certification_iso), regulatory_compliance=VALUES(regulatory_compliance), validation_status=VALUES(validation_status), application_pdf=VALUES(application_pdf)";

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
        stmt.setBytes(15, vendor.getApplicationPdfData());  // store PDF as BLOB

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
                vendor.setContactEmail(rs.getString("contact_email"));
                vendor.setPhone(rs.getString("phone"));
                vendor.setYearsInOperation(rs.getInt("years_in_operation"));
                vendor.setEmployees(rs.getInt("employees"));
                vendor.setTurnover(rs.getDouble("turnover"));
                vendor.setMaterial(rs.getString("material"));
                vendor.setClients(rs.getString("clients"));
                vendor.setCertificationIso(rs.getBoolean("certification_iso"));
                vendor.setCertificationOrganic(rs.getBoolean("certification_organic"));
                vendor.setRegulatoryCompliance(rs.getBoolean("regulatory_compliance"));
                vendor.setApplicationPdfData(rs.getBytes("application_pdf")); // retrieve PDF as BLOB

                list.add(vendor);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // Fetch a vendor by user_id
    public VendorApplication getVendorByUserId(Long userId) {
        String sql = "SELECT * FROM vendors WHERE user_id = ? ORDER BY id DESC LIMIT 1";
        try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setLong(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    VendorApplication vendor = new VendorApplication();
                    vendor.setUserId(rs.getLong("user_id"));
                    vendor.setCompanyName(rs.getString("company_name"));
                    vendor.setContactPerson(rs.getString("contact_person"));
                    vendor.setContactEmail(rs.getString("contact_email"));
                    vendor.setPhone(rs.getString("phone"));
                    vendor.setYearsInOperation(rs.getInt("years_in_operation"));
                    vendor.setEmployees(rs.getInt("employees"));
                    vendor.setTurnover(rs.getDouble("turnover"));
                    vendor.setMaterial(rs.getString("material"));
                    vendor.setClients(rs.getString("clients"));
                    vendor.setCertificationIso(rs.getBoolean("certification_iso"));
                    vendor.setCertificationOrganic(rs.getBoolean("certification_organic"));
                    vendor.setRegulatoryCompliance(rs.getBoolean("regulatory_compliance"));
                    vendor.setApplicationPdfData(rs.getBytes("application_pdf"));
                    return vendor;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
