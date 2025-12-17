/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.sql.*;
import java.util.ArrayList;
import Model.Employee;
import Model.Payroll;
import Config.DBConfig;

/**
 *
 * @author benji
 */
public class PayrollService {
    
    private Connection conn;
    
    public PayrollService(){
    
    try{
       conn = DriverManager.getConnection(
             DBConfig.URL,DBConfig.USER,DBConfig.PASSWORD
    );
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
     public boolean savePayroll(Employee emp, Payroll payroll, String payPeriod) throws SQLException {

        String sql = """
            INSERT INTO payroll (
                employee_id, pay_period, hours_worked, overtime_hours,
                basic_salary, overtime_pay, tax, uif, net_pay
            ) VALUES (?,?,?,?,?,?,?,?,?)
        """;

        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, emp.getID());
        ps.setDouble(2, payroll.getHoursWorked());
        ps.setDouble(3, payroll.getOvertimeHours());
        ps.setString(4, payPeriod);
        ps.setDouble(5, payroll.basicSalary(emp));
        ps.setDouble(6, payroll.overtimePay(emp));
        ps.setDouble(7, payroll.taxDeduction(emp));
        ps.setDouble(8, payroll.UIFDeduction(emp));
        ps.setDouble(9, payroll.netPay(emp));

        return ps.executeUpdate() > 0;
    }
     
     ///get payroll for a specific employee and period
     
     public ResultSet getPayrollForPeriod(String employeeId,String payPeriod) throws SQLException{
           String sql = "SELECT * FROM payroll WHERE employee_id=? AND pay_period=?";
          PreparedStatement ps = conn.prepareStatement(sql);

          ps.setString(1, employeeId);
          ps.setString(2, payPeriod);

        return ps.executeQuery();
     }
     
     public ResultSet getPayrollHistory(String employeeId) throws SQLException {

        String sql = "SELECT * FROM payroll WHERE employee_id=? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, employeeId);
        return ps.executeQuery();
    }
    
    
    
}
