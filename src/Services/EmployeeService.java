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
    
    public void addEmployee(Employee emp) throws SQLException{
      
  String sql ="INSERT INTO employees(name,surname,jobTitle, department) VALUES(?,?,?,?)";
        
       PreparedStatement ps=conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS);
      
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
    
    public boolean deleteEmployee(String employeeID) throws SQLException{
    String sql="DELETE FROM employees WHERE employee_id=?";
    
    PreparedStatement ps=conn.prepareStatement(sql);
    ps.setString(1,employeeID);
    
    int affectedRows=ps.executeUpdate();
    
    return affectedRows>0;
    }
    
    public boolean updateEmployee(Employee emp) throws SQLException{
    String sql = "UPDATE employees SET name=?, surname=?, jobTitle=?, department=? WHERE employee_id=?";
    PreparedStatement ps = conn.prepareStatement(sql);

    ps.setString(1, emp.getName());
    ps.setString(2, emp.getSurname());
    ps.setString(3, emp.getJobTitle());
    ps.setString(4, emp.getDepartment());
    ps.setString(5, emp.getID());

    int rowsAffected = ps.executeUpdate();
    return rowsAffected > 0;
        
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
}
