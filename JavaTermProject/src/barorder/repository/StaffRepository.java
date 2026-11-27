package barorder.repository;

import barorder.domain.Staff;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// staff 테이블 CRUD
public class StaffRepository {

    // 조회 : 이름에 searchWord가 들어간 직원
    public ArrayList<Staff> select(String searchWord) {
        ArrayList<Staff> list = new ArrayList<Staff>();
        String sql = "select staff_no, staff_name, phone, grade, hire_date from staff "
                + "where staff_name like ? order by staff_no";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchWord + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Staff staff = new Staff();
                staff.setStaffNo(rs.getInt("staff_no"));
                staff.setStaffName(rs.getString("staff_name"));
                staff.setPhone(rs.getString("phone"));
                staff.setGrade(rs.getString("grade"));
                staff.setHireDate(rs.getDate("hire_date"));
                list.add(staff);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 추가 : 직원번호는 (가장 큰 번호 + 1)로 자동 부여
    public void insert(Staff staff) {
        String sql = "insert into staff (staff_no, staff_name, phone, grade, hire_date) "
                + "values ((select nvl(max(staff_no), 0) + 1 from staff), ?, ?, ?, ?)";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, staff.getStaffName());
            pstmt.setString(2, staff.getPhone());
            pstmt.setString(3, staff.getGrade());
            pstmt.setDate(4, staff.getHireDate());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 수정
    public void update(Staff staff) {
        String sql = "update staff set staff_name=?, phone=?, grade=?, hire_date=? where staff_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, staff.getStaffName());
            pstmt.setString(2, staff.getPhone());
            pstmt.setString(3, staff.getGrade());
            pstmt.setDate(4, staff.getHireDate());
            pstmt.setInt(5, staff.getStaffNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 삭제
    public void delete(int staffNo) {
        String sql = "delete from staff where staff_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, staffNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
