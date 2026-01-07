/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author benji
 */
public class Payroll {
    
    private double hoursWorked;
    private double overtimeHours;

    
    public Payroll(){
        
    }
    
    public Payroll(double hoursWorked, double overtimeHours) {
        this.hoursWorked = hoursWorked;
        this.overtimeHours = overtimeHours;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public double getOvertimeHours() {
        return overtimeHours;
    }

    public void setOvertimeHours(double overtimeHours) {
        this.overtimeHours = overtimeHours;
    }
    
    

   
    
    
    
}
