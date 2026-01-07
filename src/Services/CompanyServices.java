/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;
import Config.DBConfig;
import Model.*;
import java.sql.*;

/**
 *1588 0316 4243
 * @author benji
 */


public class CompanyServices {
    
    
    private Connection conn;
        public CompanyServices() {
        try {
            conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    

public boolean saveOrUpdateCompanyInfo(CompanyInfo companyInfo) throws SQLException {

    String sql = """
        INSERT INTO company_info (id, company_name, company_code, tax_number, registration_number)
        VALUES (1, ?, ?, ?, ?)
        ON DUPLICATE KEY UPDATE
            company_name = VALUES(company_name),
            company_code = VALUES(company_code),
            tax_number = VALUES(tax_number),
            registration_number = VALUES(registration_number)
        """;

    PreparedStatement ps = conn.prepareStatement(sql);

    ps.setString(1, companyInfo.getCompanyName());
    ps.setString(2, companyInfo.getCompanyCode());
    ps.setString(3, companyInfo.getTaxNumber());
    ps.setString(4, companyInfo.getRegistrationNumber());

    return ps.executeUpdate() > 0;
}

    
   

    
    
    
}
