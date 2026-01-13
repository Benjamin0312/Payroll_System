/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;
import Model.*;

/**
 *
 * @author benji
 */
import java.sql.*;
import Model.Employee;
import java.util.ArrayList;
import java.sql.Connection;
import java.sql.DriverManager;
import Config.DBConfig;

public class EmployeeService {
    private Connection conn;

  public EmployeeService(){
  
  try{
    conn = DriverManager.getConnection(
    DBConfig.URL,DBConfig.USER,DBConfig.PASSWORD
    );
    
   }catch(SQLException e){
      e.printStackTrace();
    }
  }
  
  
 
  
 
  
    public void registerEmployee(Employee emp) throws SQLException{
      
  String sql ="INSERT INTO employees(job_role_id,name,surname,jobTitle, department,password) VALUES(?,?,?,?,?,?)";
        
       PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
      
       ps.setInt(1,emp.getJobRoleId());
       ps.setString(2,emp.getName());
       ps.setString(3, emp.getSurname());
       ps.setString(4,emp.getJobTitle());
       ps.setString(5,emp.getDepartment());
       ps.setString(6,emp.getPassword());
       ps.executeUpdate();
       
       ResultSet rs=ps.getGeneratedKeys();
       
      // int id = 0;
       if(!rs.next()){
           //id=rs.getInt(1);
           throw new SQLException("Failed to generate employeeID");
       }
       
       int id=rs.getInt(1);
       
        CompanyServices service = new CompanyServices();
      CompanyInfo companyInfo = service.getCompanyInfo();
       
       String employeeCode=companyInfo.getCompanyCode()+String.format("%04d", id);
        
       String updateID="UPDATE employees SET employee_id=? where id=?";
       PreparedStatement ps2=conn.prepareStatement(updateID);
       ps2.setString(1,employeeCode);
       ps2.setInt(2,id);
       ps2.executeUpdate();
       emp.setID(employeeCode);
       
       
    }
  
   
    public int deleteEmployee(String employeeID) throws SQLException{
    String sql="DELETE FROM employees WHERE employee_id=?";
    
    PreparedStatement ps=conn.prepareStatement(sql);
    ps.setString(1,employeeID);
    
    int affectedRows=ps.executeUpdate();
    
    return affectedRows;
    }
    

    
    public Employee searchEmployee(String employeeID) throws SQLException{
    String sql = "SELECT * FROM employees WHERE employee_id = ?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, employeeID);

    ResultSet rs = ps.executeQuery();

    if (rs.next()) {
        Employee emp = new Employee();
        emp.setID(rs.getString("employee_id"));
        emp.setName(rs.getString("name"));
        emp.setSurname(rs.getString("surname"));
        emp.setJobTitle(rs.getString("jobTitle"));
        emp.setDepartment(rs.getString("department"));
        return emp;
    }

    return null;
        
    }
    
    public ArrayList<Employee> getAllEmployees() throws SQLException{
        
        ArrayList<Employee> employees=new ArrayList<>();
        String sql="SELECT * FROM employees";
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery(sql);
        
        while(rs.next()){
            Employee emp=new Employee();
          
            emp.setID(rs.getString("employee_id"));
            emp.setName(rs.getString("name"));
            emp.setSurname(rs.getString("surname"));
            emp.setJobTitle(rs.getString("jobTitle"));
            emp.setDepartment(rs.getString("department"));
            employees.add(emp);
            
        }
        return employees;
    }
    
    public Employee login(String username, String password) throws SQLException {
    String sql = "SELECT * FROM employees WHERE employee_id=? AND password=?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, username);
    ps.setString(2, password);
  ResultSet rs = ps.executeQuery();
  if (rs.next()) {
        Employee emp = new Employee();
        emp.setID(rs.getString("employee_id"));
        emp.setJobRoleId(rs.getInt("job_role_id"));
        emp.setName(rs.getString("name"));
        emp.setSurname(rs.getString("surname"));
        emp.setJobTitle(rs.getString("jobTitle"));
        emp.setDepartment(rs.getString("department"));
      return emp;
    }
    return null;
}
  
    
    public String createPassword(String name,String surname){
        
        
        return name+surname.toLowerCase();
    }
    
    
    
public void changePassword(String employeeId, String oldPassword, String newPassword) throws SQLException {

 
    String checkSql = "SELECT password FROM employees WHERE employee_id = ?";
    PreparedStatement checkPs = conn.prepareStatement(checkSql);
    checkPs.setString(1, employeeId);

    ResultSet rs = checkPs.executeQuery();

    if (!rs.next()) {
        throw new SQLException("Employee not found");
    }

    String storedPassword = rs.getString("password");

   
    if (!storedPassword.equals(oldPassword)) {
        throw new SQLException("Current password is incorrect");
    }

  
    String updateSql = "UPDATE employees SET password = ? WHERE employee_id = ?";
    PreparedStatement updatePs = conn.prepareStatement(updateSql);
    
    AdminService adminService=new AdminService();
    
    String hashedNewPassword=adminService.hashPassword(newPassword);
    updatePs.setString(1, hashedNewPassword); 
    updatePs.setString(2, employeeId);

    updatePs.executeUpdate();
}


}
