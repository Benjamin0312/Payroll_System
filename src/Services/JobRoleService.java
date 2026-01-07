/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Services;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;
import Model.*;
import UI.*;
import Config.DBConfig;
import java.awt.Color;
import java.awt.Color;
import javax.swing.JOptionPane;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.pdfbox.pdmodel.graphics.state.PDExtendedGraphicsState;
import org.apache.pdfbox.util.Matrix;
import java.io.IOException;
import java.time.LocalDate;

/**
 *
 * @author benji
 */
public class JobRoleService {
    
    private Connection conn;
    
    public JobRoleService(){
    
    try{
       conn = DriverManager.getConnection(
             DBConfig.URL,DBConfig.USER,DBConfig.PASSWORD
    );
        }catch(SQLException e){
            e.printStackTrace();
        }
    }
     
 public ResultSet getPayrollHistory(String employeeId) throws SQLException {

        String sql = "SELECT * FROM payroll WHERE employee_id=? ORDER BY created_at DESC";
        PreparedStatement ps = conn.prepareStatement(sql);

        ps.setString(1, employeeId);
        return ps.executeQuery();
    }
     
public List<JobRole> getAllJobRoles() throws SQLException{
         
       List<JobRole> roles=new ArrayList<>();
         
         String sql="SELECT * FROM job_roles";
         
         try(Statement st=conn.createStatement();
             ResultSet rs=st.executeQuery(sql)){
             
             while(rs.next()){
                 roles.add(new JobRole(
                 rs.getInt("role_id"),
                 rs.getString("role_name"),
                 rs.getString("department"),
                 rs.getDouble("hourly_rate"),
                 rs.getDouble("overtime_rate")));
             }
         }
         
         return roles;
     }

public JobRole  getJobRoleRates(String roleName,String department)throws SQLException{
    String sql="SELECT hourly_rate,overtime_rate FROM job_roles WHERE role_name=? AND department=?";
   
    PreparedStatement ps=conn.prepareStatement(sql);
    ps.setString(1,roleName);
    ps.setString(2,department);
    
    ResultSet rs=ps.executeQuery();
    
    if(rs.next()){
        JobRole role=new JobRole();
        
        role.setRoleName(roleName);
        role.setDepartment(department);
        
        role.setHourlyRate(rs.getDouble("hourly_rate"));
        role.setOvertimeRate(rs.getDouble("overtime_rate"));
        
        
        return role;
    }
    

return null;
}


public double basicSalary(JobRole jobRole,Payroll payroll){
        double basicSalary=jobRole.getHourlyRate()*payroll.getHoursWorked();
        return basicSalary;
    }


public double overtimePay(JobRole jobRole,Payroll payroll){
       double overtimePay=jobRole.getOvertimeRate()*payroll.getOvertimeHours();
       return overtimePay;
    }

public double grossPay(JobRole jobRole,Payroll payroll){
    double grossPay=basicSalary(jobRole,payroll)+overtimePay(jobRole,payroll);
    
    return grossPay;
}
public double UIFDeduction(JobRole jobRole,Payroll payroll){
    double UIFDeduction=0.01*grossPay(jobRole,payroll);
    
  return Math.min(177.12,UIFDeduction);
}

public double taxDeduction(JobRole jobRole,Payroll payroll){
    double tax = 0;
    double gross=grossPay(jobRole,payroll);

         // 0 – 19,758
    if (gross <= 19758) {               
        tax = gross * 0.18;
    } 
         // 19,759 – 30,875
    else if (gross <= 30875) {      
        tax = 19758 * 0.18 + (gross - 19758) * 0.26;
    } 
    // 30,876 – 42,733
    else if (gross <= 42733) {       
        tax = 19758 * 0.18 + (30875-19758) * 0.26 + (gross - 30875) * 0.31;
    } 
    // 42,734 – 56,083
    else if (gross <= 56083) {       
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (gross - 42733)*0.36;
    } 
     // 56,083 – 71,492
    else if (gross <= 71492) {      
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (56083-42733)*0.36 + (gross - 56083)*0.39;
    } 
     // 71,493 – 151,417
    else if (gross <= 151417) {     
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (56083-42733)*0.36 + (71492-56083)*0.39 + (gross-71492)*0.41;
    } 
     // 151,418 and above
    else {                                       
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (56083-42733)*0.36 + (71492-56083)*0.39 + (151417-71492)*0.41 + (gross-151417)*0.45;
    }
    
    tax -=1436.25;
    
    if(tax<0) tax=0;
    
    return tax;
}
public double totalDeductions(JobRole jobRole,Payroll payroll){
    double totalDeductions = UIFDeduction(jobRole,payroll)+taxDeduction(jobRole,payroll);
    
    return totalDeductions;
}
public double netPay(JobRole jobRole,Payroll payroll){
    double netPay=grossPay(jobRole,payroll)-totalDeductions(jobRole,payroll);
    
    return netPay;
}


     public boolean savePayroll(Employee emp, Payroll payroll, String payPeriod,JobRole jobRole) throws SQLException {

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
        ps.setDouble(5, basicSalary(jobRole,payroll));
        ps.setDouble(6, overtimePay(jobRole,payroll));
        ps.setDouble(7, taxDeduction(jobRole,payroll));
        ps.setDouble(8, UIFDeduction(jobRole,payroll));
        ps.setDouble(9, netPay(jobRole,payroll));

        return ps.executeUpdate() > 0;
    }


public void generatePayslip(Employee employee,CompanyInfo companyInfo,JobRole jobRole,Payroll payroll){
    
    LocalDate currentDate=LocalDate.now();
    
    RegisterEmployee registerEmployee=new RegisterEmployee();
    
    
    
       ///Create a an PDDocumnet object which will be used for creating a pdf document
      try (PDDocument document=new PDDocument()) {
          ///creating single page on the pdf document 
             PDPage page=new PDPage();
             document.addPage(page);
             
      
        
         try(PDPageContentStream cs=new PDPageContentStream(document,page,PDPageContentStream.AppendMode.APPEND,false)){
          PDExtendedGraphicsState gs=new PDExtendedGraphicsState();
          
          ///setting the transparency(visibility) of the watermark which is controlled by the alpha constatnt, 0.5f in this case
         gs.setNonStrokingAlphaConstant(0.5f);
         cs.setGraphicsStateParameters(gs);
         
         ///setting the color of the watermark
         cs.setNonStrokingColor(Color.LIGHT_GRAY);
         //beginning of the water mark text
         cs.beginText();
         ////setting the font to be used for this watermark and its size
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),100);
        ////rotating the water mark text by 45 degrees so it can be diagonally placed on the document
         cs.setTextMatrix(Matrix.getRotateInstance(Math.toRadians(45),200,300));
         ///water mark text
         cs.showText("PAYSLIP");
         ///end of the water mark
         cs.endText();
         
        
       }    
                  
         
        /*=================================================================================================
          ADDING PAYSLIP CONTENTS TO THE PDF DOCUMENT USING CONTENT STREAM WHICH SERVES AS A PDF WRITER
         ====================================================================================================
             */

    try (PDPageContentStream cs=new PDPageContentStream(document,page,PDPageContentStream.AppendMode.APPEND,false)) {
         
        //resseting the graphics state to normal color and transparency since we previoulsy made a watermark  
        PDExtendedGraphicsState reset=new PDExtendedGraphicsState();
        ////normall text transparancy
         reset.setNonStrokingAlphaConstant(1f);
         cs.setGraphicsStateParameters(reset);
         ///setting the text color to black fro visible text
         cs.setNonStrokingColor(Color.BLACK);

      /////begin of Payslip contents   
         cs.beginText(); 
     ////setting the font to be used for text in this document and its size
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA),33);
         ///seeting where will this line of text will be situated in the document
         cs.newLineAtOffset(50,770);
         ///text to be displayed on the text
         cs.showText("---------------------------------------------");
        ///end of text
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD),33);
         cs.newLineAtOffset(200,740);
         cs.showText(companyInfo.getCompanyName());
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA),33);
         cs.newLineAtOffset(50,710);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA),20);
         cs.newLineAtOffset(250, 690);
         cs.showText("PAYSLIP");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 33);
         cs.newLineAtOffset(50, 670);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 650);
                       //////getting the employee id                             getting month from the current date object and setting it as payment period
         cs.showText("Employee ID:emp"+employee.getID()+"                                                             Payment Period: "+currentDate.getMonth());
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 630);
         ////               getting employee name                                                               getting current date 
         cs.showText("Employee Name:"+employee.getName()+"                                                                  Date: "+currentDate);
         cs.endText(); 
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 610);
          ////////////////employee's surname'
         cs.showText("Employee Surname: "+employee.getSurname());
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 590);
         ///////////////employee's job position'
         cs.showText("Position: "+employee.getJobTitle()+ "                                                                  Department:"+registerEmployee.getDepartment());                                                       
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 33);
         cs.newLineAtOffset(50, 560);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 20);
         cs.newLineAtOffset(250, 540);
         ////   header for toatal employee's earnings
         cs.showText("Earnings");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 33);
         cs.newLineAtOffset(50, 520);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 500);
                       /////normal hours worked by the employee                                                overtime hours worked by the employee
         cs.showText("Normal worked hours: "+payroll.getHoursWorked()+"                                                Overtime Hours: "+payroll.getOvertimeHours());
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 480);
               // getting the employee's base salary                                                                       employee's pay from overtime work
         cs.showText("Base Pay:R"+netPay(jobRole,payroll)+"                                                               Overtime Pay:R"+overtimePay(jobRole,payroll));
       //  cs.showText("Netpay:R"+salaryCal.calculateNormalPay(this));
         cs.endText(); 
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 460);
         ///an if statement for a bonus payment if its end of the year ussually at november hence it we set it on november using the current date object
         if(currentDate.getMonthValue()==11){
         
          ///// if it is november then the bonus should be 25% of the employee's net pay
           cs.showText("Bonus:R"+netPay(jobRole,payroll)*0.25);
         } else{
            // otherwise bonus is 0
            cs.showText("Bonus:R0.00 ");
         }
         
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50,440);
         ///cs.showText("Total Before deductions:R"+getNetpay());
         ////toatl payment before deductions which is the addition of normal work time payment and overtime payment
         cs.showText("Total Before Dedections:R"+netPay(jobRole,payroll));
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 33);
         cs.newLineAtOffset(50,420);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 20);
         cs.newLineAtOffset(250,400);
         ///deductions header
         cs.showText("Deductions");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 33);
         cs.newLineAtOffset(50,380);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50, 360);
         ////tax deductions 
         cs.showText("TAX:R"+taxDeduction(jobRole,payroll));
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50,340);
         ///UIF deductions
        /// double pension=getNetpay();
         cs.showText("UIF Pension:R"+UIFDeduction(jobRole,payroll));
         cs.endText(); 
         
     
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 12);
         cs.newLineAtOffset(50,320);
         
         cs.showText("Total Deductions:R"+totalDeductions(jobRole,payroll));
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 33);
         cs.newLineAtOffset(50, 300);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 20);
         cs.newLineAtOffset(250, 280);
          //// the payment that the user will receive after all deductions have been applied to their net pay
         cs.showText("Net Pay:R"+netPay(jobRole,payroll));
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.HELVETICA), 33);
         cs.newLineAtOffset(50, 260);
         cs.showText("---------------------------------------------");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE),12);
         cs.newLineAtOffset(50, 230);
         ////a Disclaimer
         cs.showText("DISCLAIMER:This is a computer generated payslip and while great effort  ");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE),12);
         cs.newLineAtOffset(50,220);
         cs.showText("has been made to ensure accuracy,Computer errors out ");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE),12);
         cs.newLineAtOffset(50,210);
         cs.showText("of our control might occur which might result in Figures that are not");
         cs.endText();
   
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE),12);           
         cs.newLineAtOffset(50,200);   
         cs.showText("reflected in your bank Account.Please consult with your HR should such happen");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE),12);           
         cs.newLineAtOffset(50,190);   
         cs.showText("as "+companyInfo.getCompanyName().toUpperCase()+" will not be held accountable for any incorrect figgures");
         cs.endText();
         
         cs.beginText();
         cs.setFont(new PDType1Font(Standard14Fonts.FontName.COURIER_OBLIQUE),12);           
         cs.newLineAtOffset(50,180);   
         cs.showText("neither can any claims be made based on the values shown");
         cs.endText();
         
      
}
      

    document.save(employee.getName().toLowerCase()+"Payslip.pdf");
   
    
    JOptionPane.showMessageDialog(null,"Pay slip succesfully generated");
} catch (IOException e) {
   JOptionPane.showMessageDialog(null,"Database error:"+e.getMessage());
} 
    
}
    
    
    
}
