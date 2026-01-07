/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author benji
 */
public class CompanyInfo {
    
    private String companyName;
    private String companyCode;
    private String taxNumber;
    private String registrationNumber;
    
    public CompanyInfo(){
        
    }
    
    
    public CompanyInfo(String companyName, String companyCode, String taxNumber, String registrationNumber) {
        this.companyName = companyName;
        this.companyCode = companyCode;
        this.taxNumber = taxNumber;
        this.registrationNumber = registrationNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyCode() {
        return companyCode;
    }

    public void setCompanyCode(String companyCode) {
        this.companyCode = companyCode;
    }

    public String getTaxNumber() {
        return taxNumber;
    }

    public void setTaxNumber(String taxNumber) {
        this.taxNumber = taxNumber;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }

    
    
    
}
