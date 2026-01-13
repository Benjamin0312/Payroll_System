package Model;

public class JobRole {

    
    private int jobRoleId;
    private String companyName;
    private String roleName;
    private String department;
    private double hourlyRate;
    private double overtimeRate;

    public JobRole() {}

    public JobRole(int jobRoleId, String companyName, String roleName, String department, double hourlyRate, double overtimeRate) {
        this.jobRoleId = jobRoleId;
        this.companyName = companyName;
        this.roleName = roleName;
        this.department = department;
        this.hourlyRate = hourlyRate;
        this.overtimeRate = overtimeRate;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }
    

    public void setJobRoleId(int jobRoleId) {
        this.jobRoleId = jobRoleId;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public void setHourlyRate(double hourlyRate) {
        this.hourlyRate = hourlyRate;
    }

    public void setOvertimeRate(double overtimeRate) {
        this.overtimeRate = overtimeRate;
    }

    public JobRole(int jobRoleId, String roleName, String department, double hourlyRate,double overtimeRate) {
        this.jobRoleId =jobRoleId;
        this.roleName = roleName;
        this.department = department;
       
    }


    public int getJobRoleId() {
        return jobRoleId;
    }
    public String getRoleName() {
        return roleName;
    }
    public String getDepartment() {
        return department;
    }
    public double getHourlyRate() {
        return hourlyRate;
    }
   public double getOvertimeRate(){
       return overtimeRate;
   }
    

    @Override
    public String toString() {
        return roleName + " (" + department + ")";
    }
}
