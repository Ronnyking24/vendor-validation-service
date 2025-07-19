package com.terravin.vendor_validation.model;

public class VendorApplication {
    private String companyName;
    private String contactPerson;
    private String contactEmail;
    private String phone;
    private int yearsInOperation;
    private int employees;
    private double turnover;
    private String material;
    private String clients;
    private boolean certificationIso;
    private boolean certificationOrganic;

    // Getters
    public String getCompanyName() { return companyName; }
    public String getContactPerson() { return contactPerson; }
    public String getContactEmail() { return contactEmail; }
    public String getPhone() { return phone; }
    public int getYearsInOperation() { return yearsInOperation; }
    public int getEmployees() { return employees; }
    public double getTurnover() { return turnover; }
    public String getMaterial() { return material; }
    public String getClients() { return clients; }
    public boolean isCertificationIso() { return certificationIso; }
    public boolean isCertificationOrganic() { return certificationOrganic; }

    // Setters
    public void setCompanyName(String companyName) { this.companyName = companyName; }
    public void setContactPerson(String contactPerson) { this.contactPerson = contactPerson; }
    public void setContactEmail(String contactEmail) { this.contactEmail = contactEmail; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setYearsInOperation(int yearsInOperation) { this.yearsInOperation = yearsInOperation; }
    public void setEmployees(int employees) { this.employees = employees; }
    public void setTurnover(double turnover) { this.turnover = turnover; }
    public void setMaterial(String material) { this.material = material; }
    public void setClients(String clients) { this.clients = clients; }
    public void setCertificationIso(boolean certificationIso) { this.certificationIso = certificationIso; }
    public void setCertificationOrganic(boolean certificationOrganic) { this.certificationOrganic = certificationOrganic; }
}
