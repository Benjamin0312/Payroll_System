package Model;

public class jobRole {

    private int roleId;
    private String roleName;
    private String department;
    private double hourlyRate;
    private double overtimeRate;

    public jobRole() {}

    public jobRole(int roleId, String roleName, String department, double hourlyRate,double overtimeRate) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.department = department;
        this.hourlyRate = hourlyRate;
        this.overtimeRate=overtimeRate;
    }


    public int getRoleId() {
        return roleId;
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
