package barorder.repository;

import barorder.domain.Seat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// seat 테이블 CRUD
public class SeatRepository {

    // 조회 : 전체 좌석
    public ArrayList<Seat> select() {
        ArrayList<Seat> list = new ArrayList<Seat>();
        String sql = "select seat_no, capacity, area, seat_status from seat order by seat_no";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Seat seat = new Seat();
                seat.setSeatNo(rs.getInt("seat_no"));
                seat.setCapacity(rs.getInt("capacity"));
                seat.setArea(rs.getString("area"));
                seat.setSeatStatus(rs.getString("seat_status"));
                list.add(seat);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 추가 : 좌석번호는 (가장 큰 번호 + 1)로 자동 부여
    public void insert(Seat seat) {
        String sql = "insert into seat (seat_no, capacity, area, seat_status) "
                + "values ((select nvl(max(seat_no), 0) + 1 from seat), ?, ?, ?)";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, seat.getCapacity());
            pstmt.setString(2, seat.getArea());
            pstmt.setString(3, seat.getSeatStatus());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 수정
    public void update(Seat seat) {
        String sql = "update seat set capacity=?, area=?, seat_status=? where seat_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, seat.getCapacity());
            pstmt.setString(2, seat.getArea());
            pstmt.setString(3, seat.getSeatStatus());
            pstmt.setInt(4, seat.getSeatNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 삭제
    public void delete(int seatNo) {
        String sql = "delete from seat where seat_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, seatNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
