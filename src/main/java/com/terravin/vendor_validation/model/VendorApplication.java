package com.terravin.vendor_validation.model;

public class VendorApplication {
    private String companyName;
    private int yearsOfOperation;
    private double annualRevenue;
    private boolean hasCertifications;
    private boolean compliesWithRegulations;
    private String contactEmail;

    // Getters and setters
    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public int getYearsOfOperation() {
        return yearsOfOperation;
    }

    public void setYearsOfOperation(int yearsOfOperation) {
        this.yearsOfOperation = yearsOfOperation;
    }

    public double getAnnualRevenue() {
        return annualRevenue;
    }

    public void setAnnualRevenue(double annualRevenue) {
        this.annualRevenue = annualRevenue;
    }

    public boolean isHasCertifications() {
        return hasCertifications;
    }

    public void setHasCertifications(boolean hasCertifications) {
        this.hasCertifications = hasCertifications;
    }

    public boolean isCompliesWithRegulations() {
        return compliesWithRegulations;
    }

    public void setCompliesWithRegulations(boolean compliesWithRegulations) {
        this.compliesWithRegulations = compliesWithRegulations;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }
}
