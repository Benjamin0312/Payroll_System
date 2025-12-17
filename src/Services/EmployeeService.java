/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

/**
 *
 * @author benji
 */
import java.sql.*;
import Model.Employee;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;

public class EmployeeService {
    private Connection conn;

  public EmployeeService(){
  
  try{
    conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/payroll_system","root","2003B@njamin");  
    
   }catch(SQLException e){
      e.printStackTrace();
    }
  }
    
    public void addEmployee(Employee emp) throws SQLException{
      
  String sql ="INSERT INTO employees(name,surname,jobTitle, department) VALUES(?,?,?,?)";
        
       PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
      
       ps.setString(1,emp.getID());
       ps.setString(2,emp.getName());
       ps.setString(3, emp.getSurname());
       ps.setString(4,emp.getJobTitle());
       ps.setString(5,emp.getDepartment());
       ps.executeUpdate();
       
       ResultSet rs=ps.getGeneratedKeys();
       
       int id = 0;
       if(rs.next()){
           id=rs.getInt(1);
       }
       
       String employeeCode="EMP"+String.format("%04d", id);
        
       String updateID="UPDATE employees SET employee_id=? where id=?";
       PreparedStatement ps2=conn.prepareStatement(updateID);
       ps2.setString(1,employeeCode);
       ps2.setInt(2,id);
       ps2.executeUpdate();
       emp.setID(employeeCode);
       
       
    }
    
    public void deleteEmployee(Employee emp){
        
    }
    
    public void updateEmployee(Employee emp){
        
    }
    
    public void searchEmployee(Employee emp){
        
    }
    
    public void getAllEmployees(Employee emp){
        
    }
}
