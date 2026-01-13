package Services;

import java.sql.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import Config.DBConfig;

public class AdminService {

    private Connection conn;

    public AdminService() {
        try {
            conn = DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] bytes = md.digest(password.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) sb.append(String.format("%02x", b));
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not found");
        }
    }
    
public boolean adminLogin(String username, String password) throws SQLException {

    String sql = "SELECT password_hash FROM admins WHERE username=?";
    PreparedStatement ps = conn.prepareStatement(sql);
    ps.setString(1, username);

    ResultSet rs = ps.executeQuery();

    if (!rs.next()) return false;

    return rs.getString("password_hash")
             .equals(hashPassword(password));
}

    
    public boolean adminExists() throws SQLException {
        String sql = "SELECT COUNT(*) FROM admins";
        try (Statement st = conn.createStatement(); 
            ResultSet rs = st.executeQuery(sql)) {
            rs.next();
            return rs.getInt(1) > 0;
        }
    }

    public void createBootstrapAdmin() throws SQLException {
        if (adminExists()) return;

        String username = "admin";
        String bootstrapPassword = "admin@123";

        String sql = "INSERT INTO admins (username, password_hash, must_reset_password) VALUES (?, ?, true)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(bootstrapPassword));
            ps.executeUpdate();
        }
    }


    public enum AdminLoginResult {
        INVALID,
        MUST_RESET,
        SUCCESS }

    public AdminLoginResult authenticate(String username, String password) throws SQLException {
        
        String sql = "SELECT password_hash, must_reset_password FROM admins WHERE username = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) return AdminLoginResult.INVALID;

            String storedHash = rs.getString("password_hash");
            boolean mustReset = rs.getBoolean("must_reset_password");

            if (!storedHash.equals(hashPassword(password))) return AdminLoginResult.INVALID;

            return mustReset ? AdminLoginResult.MUST_RESET : AdminLoginResult.SUCCESS;
        }
    }

   
    public void resetPassword(String username, String newPassword) throws SQLException {
        String sql = "UPDATE admins SET password_hash = ?, must_reset_password = false WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, hashPassword(newPassword));
            ps.setString(2, username);
            ps.executeUpdate();
        }
    }

    public void changePassword(String username, String oldPassword, String newPassword) throws SQLException {
    AdminLoginResult result = authenticate(username, oldPassword);
if (result == AdminLoginResult.INVALID) {
    throw new IllegalArgumentException("Old password is incorrect");
}

        resetPassword(username, newPassword);
    }
    
 
    private String generateUsername(String name) {
        return name.toLowerCase().replaceAll("\\s+", "") + "@admin";
    }

    public void registerAdmin(String name, String password) throws SQLException {
        String username = generateUsername(name);

        String sql = "INSERT INTO admins(username, password_hash,must_reset_password) VALUES (?, ?, false)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            ps.setString(2, hashPassword(password));
            ps.executeUpdate();
        }
    }

    public int deleteAdmin(String username) throws SQLException {
        String sql = "DELETE FROM admins WHERE username = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, username);
            
            
            return ps.executeUpdate();
        }
    }

    public List<String> getAllAdmins() throws SQLException {
        List<String> admins = new ArrayList<>();
        String sql = "SELECT username FROM admins";
        try (Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) admins.add(rs.getString("username"));
        }
        return admins;
    }


  public void completeFirstLogin(String bootstrapUsername, String name, String newPassword)throws SQLException {

    String newUsername = generateUsername(name);

    String sql = """
        UPDATE admins
        SET username = ?, password_hash = ?, must_reset_password = false
        WHERE username = ?
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, newUsername);
        ps.setString(2, hashPassword(newPassword));
        ps.setString(3, bootstrapUsername);
        ps.executeUpdate();
    }
}

    
    
   public void addJobRole(String roleName,String department,double hourlyRate,double overtimeRate) throws SQLException {

    
    String sql = """
        INSERT INTO job_roles (role_name, department, hourly_rate, overtime_rate)
        VALUES (?, ?, ?, ?)
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, roleName);
        ps.setString(2, department);
        ps.setDouble(3, hourlyRate);
        ps.setDouble(4, overtimeRate);
        ps.executeUpdate();
    }
}

public boolean jobRoleExistCheck(String roleName, String department) throws SQLException {

    String sql = """
        SELECT COUNT(*)
        FROM job_roles
        WHERE role_name = ?
          AND department = ?
    """;

    try (PreparedStatement ps = conn.prepareStatement(sql)) {
        ps.setString(1, roleName);
        ps.setString(2, department);

        ResultSet rs = ps.executeQuery();
        rs.next();

        return rs.getInt(1) > 0;
    }
}

    
  
}
