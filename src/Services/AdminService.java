/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;
import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import Config.DBConfig;
/**
 *
 * @author benji
 */
public class AdminService {
    private Connection conn;
    
    public AdminService(){
        try{
          conn=  DriverManager.getConnection(DBConfig.URL,DBConfig.USER,DBConfig.PASSWORD);
            
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
    
      private String hashPassword(String password){
        try{
            MessageDigest md= MessageDigest.getInstance("SHA-256");
            byte[] hashedBytes= md.digest(password.getBytes());
            
            StringBuilder sb= new StringBuilder();
            for(byte b:hashedBytes){
                sb.append(String.format("%02x",b));
            }
            return sb.toString();
        }catch(NoSuchAlgorithmException e){
            throw new RuntimeException("Hashing algorithm not found");
    
        }
      }
    
    public boolean adminExist()throws SQLException{
        String sql= "SELECT COUNT(* FROM admins";
        Statement st=conn.createStatement();
        ResultSet rs=st.executeQuery(sql);
        
        if(rs.next()){
            return rs.getInt(1)>0;
        }
        return false;
    }
    
    public void createDefaultAdmin() throws SQLException{
        String userName="admin";
        String password="admin123";
        
        String hashedPassword=hashPassword(password);
        
        String sql="INSERT INTO admins(username,password_hash) VALUES(?,?)";
        PreparedStatement ps=conn.prepareStatement(sql);
    
    ps.setString(1,userName);
    ps.setString(2,password);
    ps.executeUpdate();
    }
    
    public boolean authenticate(String username,String password) throws SQLException{
        String sql= "SELECT password_hash FROM admins WHERE username=?";
        PreparedStatement ps= conn.prepareStatement(sql);
    ps.setString(1, username);
    
    ResultSet rs=ps.executeQuery();
    
    if(rs.next()){
        
        String storedHash= rs.getString("password_hash");
        String inputHash= hashPassword(password);
        return storedHash.equals(inputHash);
    }
    return false;
    }
    
  

}
