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
private String employee_id;
private String name; 
private String surname;
private String jobTitle;
private String department;
private String password;
private int jobRoleId;

//6022 6342 4506



public Employee(){}

public Employee(String employee_id,String name,String surname,String jobTitle,String department,String password,int jobRoleId){
this.employee_id=employee_id;
this.name=name;
this.surname=surname;
this.jobTitle=jobTitle;
this.department=department;
this.password=password;
this.jobRoleId=jobRoleId;
}

public void setJobRoleId(int jobRoleId){
    this.jobRoleId=jobRoleId;
    
}
public int getJobRoleId(){
   return jobRoleId;
}
    public String getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(String employee_id) {
        this.employee_id = employee_id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getID() {
        return employee_id;
    }

    public void setID(String ID) {
        this.employee_id = ID;
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

    
    
public String toString(){
 return "===== Employee Details =====\n"
     + "ID: " + employee_id + "\n"
     + "Name: " + name + " " + surname + "\n"
     + "Job Title: " + jobTitle + "\n"
     + "Department: " + department;


}



    
}




