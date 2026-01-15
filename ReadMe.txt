#System Features
- Admin and employee authentication
- Job role and salary management
- Automated payroll calculation
- PDF payslip generation
- Payroll history tracking

                                   *****************************HOW THE SYSTEM WORKS*************************************
#1. User Roles
The system supports two types of users:
- Administrators
- Employees

Each role has different access levels

#2. Admin Authentication (Bootstrap Login)

- On first run, the system checks if an admin exists.
- If no admin is found, a **bootstrap admin** is created automatically.
- Default admin credentials:

  - Username:admin  
  - Password:admin@123  

- On first login, the admin is forced to change the password.
- After password reset, the admin can:
  - Register employees
  - Create job roles
  - Set salary and overtime rates
  - View payroll history
  - Manage company information


#3. Employee Registration & Default Password

- Employees are registered by an admin.
- Each employee is assigned:
  - A unique Employee ID using the company code (e.g.Univeristy of cape town code being UCT employee id would be UCT001,UCT0002...etc)
  - A job role linked to salary rates
- Default employee password:
  - Password:default password is the employee's surname@their department in small caps mhlanga@finance, smith@it which can be changed lat

#4. Employee Login & Payroll

- Employees log in using:
  - Employee ID
  - Password
- After login, employees can:
  - Enter hours worked
  - Enter overtime hours
  - Generate their payslip

Payroll calculations are automatically based on:
- Job role hourly rate
- Overtime rate
- UIF deductions
- Tax brackets



#5. Payslip Generation

- Payslips are generated as PDF files
- Each payslip includes:
  - Company details
  - Employee details
  - Earnings
  - Deductions
  - Net pay
- Payslips are also saved to the database for history tracking.


#6. Company Information

- Company details (name, tax number, registration number) are stored once.
- These details automatically appear on all generated payslips.

>PLEASE NOTE:Default credentials are for demonstration purposes only and should be changed.


