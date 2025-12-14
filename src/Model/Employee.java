/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Model;

/**
 *
 * @author benji
 */
public class Employee {
String ID;
String name; 
String surname;
String jobTitle;
String department;
double hoursWorked;
double overtimeHours;


public Employee(){}

public Employee(String ID,String name,String surname,String jobTitle,String department,double hoursWorked,double overtimeHours){
this.ID=ID;
this.name=name;
this.surname=surname;
this.jobTitle=jobTitle;
this.department=department;
this.hoursWorked=hoursWorked;
this.overtimeHours=overtimeHours;

}


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
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
    
    
public String toString(){
return "=====Employee Details======="
        +"Name:"+name
        +"Surname:"+surname
        +"Job Title:"+jobTitle
        +"Department:"+department
        +"Hours Worked:"+hoursWorked
        +"Overtime:"+overtimeHours;


}



    
}




