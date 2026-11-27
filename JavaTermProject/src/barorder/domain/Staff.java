package barorder.domain;

import java.sql.Date;

// 직원 (staff 테이블 한 행)
public class Staff {
    private int staffNo;
    private String staffName;
    private String phone;
    private String grade;
    private Date hireDate;

    public int getStaffNo() {
        return staffNo;
    }

    public void setStaffNo(int staffNo) {
        this.staffNo = staffNo;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public Date getHireDate() {
        return hireDate;
    }

    public void setHireDate(Date hireDate) {
        this.hireDate = hireDate;
    }

    // 콤보박스에 보여줄 글자
    @Override
    public String toString() {
        return staffName + " (" + grade + ")";
    }
}
