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
    double ratePerHour;
    double overtimeRate;

    public Payroll(double ratePerHour, double overtimeRate) {
        this.ratePerHour = ratePerHour;
        this.overtimeRate = overtimeRate;
    }
    
    public double basicSalary(Employee employee){
        double basicSalary=ratePerHour*employee.hoursWorked;
        return basicSalary;
    }
    
    public double overtimePay(Employee employee){
       double overtimePay=overtimeRate*employee.overtimeHours;
       return overtimePay;
    }
    
public double grossPay(Employee employee){
    double grossPay=basicSalary(employee)+overtimePay(employee);
    return grossPay;
}
public double UIFDeduction(Employee employee){
    double UIFDeduction=0.01*grossPay(employee);
    
  return Math.min(177.12,UIFDeduction);
}

public double taxDeduction(Employee employee){
    double tax = 0;

         // 0 – 19,758
    if (grossPay(employee) <= 19758) {               
        tax = grossPay(employee) * 0.18;
    } else if (grossPay(employee) <= 30875) {       // 19,759 – 30,875
        tax = 19758 * 0.18 + (grossPay(employee) - 19758) * 0.26;
    } else if (grossPay(employee) <= 42733) {       // 30,876 – 42,733
        tax = 19758 * 0.18 + (30875-19758) * 0.26 + (grossPay(employee) - 30875) * 0.31;
    } else if (grossPay(employee) <= 56083) {       // 42,734 – 56,083
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (grossPay(employee) - 42733)*0.36;
    } else if (grossPay(employee) <= 71492) {       // 56,084 – 71,492
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (56083-42733)*0.36 + (grossPay(employee) - 56083)*0.39;
    } else if (grossPay(employee) <= 151417) {      // 71,493 – 151,417
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (56083-42733)*0.36 + (71492-56083)*0.39 + (grossPay(employee)-71492)*0.41;
    } else {                                        // 151,418+
        tax = 19758*0.18 + (30875-19758)*0.26 + (42733-30875)*0.31 + (56083-42733)*0.36 + (71492-56083)*0.39 + (151417-71492)*0.41 + (grossPay(employee)-151417)*0.45;
    }
    
    tax -=1436.25;
    
    if(tax<0) tax=0;
    
    return tax;
}
public double totalDeductions(Employee employee){
    double totalDeductions = UIFDeduction(employee)+taxDeduction(employee);
    
    return totalDeductions;
}
public double netPay(Employee employee){
    double netPay=grossPay(employee)-totalDeductions(employee);
    
    return netPay;
}
}
