package com.terravin.vendor_validation.model;

public class VendorApplication {
    private Long userId;  // user_id foreign key from users table
    private String companyName;
    private String contactPerson;
    private String contactEmail;
    private String phone;
    private int yearsInOperation;
    private int employees;
    private double turnover;
    private String material;
    private String clients;
    private boolean certificationOrganic;
    private boolean certificationIso;
    private boolean regulatoryCompliance;  // new field
    private String validationStatus;       // optional for tracking status in app
    private String applicationPdf;          // path or filename of uploaded PDF

    // Getters and setters for all fields:

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getCompanyName() { return companyName; }
    public void setCompanyName(String companyName) { this.companyName = companyName; }

    public String getContactPerson() { return contactPerson; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }

    public String getContactEmail() { return contactEmail; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getYearsInOperation() { return yearsInOperation; }
    public void setYearsInOperation(int yearsInOperation) { this.yearsInOperation = yearsInOperation; }

    public int getEmployees() { return employees; }
    public void setEmployees(int employees) { this.employees = employees; }

    public double getTurnover() { return turnover; }
    public void setTurnover(double turnover) { this.turnover = turnover; }

    public String getMaterial() { return material; }
    public void setMaterial(String material) { this.material = material; }

    public String getClients() { return clients; }
    public void setClients(String clients) { this.clients = clients; }

    public boolean isCertificationOrganic() { return certificationOrganic; }
    public void setCertificationOrganic(boolean certificationOrganic) { this.certificationOrganic = certificationOrganic; }

    public boolean isCertificationIso() { return certificationIso; }
    public void setCertificationIso(boolean certificationIso) { this.certificationIso = certificationIso; }

    public boolean isRegulatoryCompliance() { return regulatoryCompliance; }
    public void setRegulatoryCompliance(boolean regulatoryCompliance) { this.regulatoryCompliance = regulatoryCompliance; }

    public String getValidationStatus() { return validationStatus; }
    public void setValidationStatus(String validationStatus) { this.validationStatus = validationStatus; }

    public String getApplicationPdf() { return applicationPdf; }
    public void setApplicationPdf(String applicationPdf) { this.applicationPdf = applicationPdf; }
}
