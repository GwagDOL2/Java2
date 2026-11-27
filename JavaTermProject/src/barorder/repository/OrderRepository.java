package barorder.repository;

import barorder.domain.Order;
import barorder.domain.OrderItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;

// orders 테이블 CRUD + order_item(주문 상세) 조회/추가/삭제
public class OrderRepository {

    // 주문 조회 : 담당직원 이름과 합계 금액(가격 x 수량의 합)을 같이 가져온다
    public ArrayList<Order> select() {
        ArrayList<Order> list = new ArrayList<Order>();
        String sql = "select o.order_no, o.order_time, o.guest_count, o.pay_method, o.pay_status, "
                + "o.seat_no, o.staff_no, s.staff_name, "
                + "(select nvl(sum(m.price * i.quantity), 0) from order_item i, menu m "
                + " where i.menu_no = m.menu_no and i.order_no = o.order_no) total_price "
                + "from orders o, staff s "
                + "where o.staff_no = s.staff_no "
                + "order by o.order_no desc";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Order order = new Order();
                order.setOrderNo(rs.getInt("order_no"));
                order.setOrderTime(rs.getTimestamp("order_time"));
                order.setGuestCount(rs.getInt("guest_count"));
                order.setPayMethod(rs.getString("pay_method"));
                order.setPayStatus(rs.getString("pay_status"));
                order.setSeatNo(rs.getInt("seat_no"));
                order.setStaffNo(rs.getInt("staff_no"));
                order.setStaffName(rs.getString("staff_name"));
                order.setTotalPrice(rs.getInt("total_price"));
                list.add(order);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 주문 추가 : 주문번호는 자동 부여, 주문일시는 현재 시각(SYSDATE)
    public void insert(Order order) {
        String sql = "insert into orders (order_no, order_time, guest_count, pay_method, pay_status, seat_no, staff_no) "
                + "values ((select nvl(max(order_no), 0) + 1 from orders), sysdate, ?, ?, ?, ?, ?)";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, order.getGuestCount());
            setPayMethod(pstmt, 2, order.getPayMethod());
            pstmt.setString(3, order.getPayStatus());
            pstmt.setInt(4, order.getSeatNo());
            pstmt.setInt(5, order.getStaffNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주문 수정 (주문일시는 바꾸지 않는다)
    public void update(Order order) {
        String sql = "update orders set guest_count=?, pay_method=?, pay_status=?, seat_no=?, staff_no=? "
                + "where order_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, order.getGuestCount());
            setPayMethod(pstmt, 2, order.getPayMethod());
            pstmt.setString(3, order.getPayStatus());
            pstmt.setInt(4, order.getSeatNo());
            pstmt.setInt(5, order.getStaffNo());
            pstmt.setInt(6, order.getOrderNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주문 삭제 : 자식인 order_item을 먼저 지우고 orders를 지운다 (둘 다 성공해야 반영)
    public void delete(int orderNo) {
        try (Connection con = JDBCConnector.getConnection()) {
            con.setAutoCommit(false);
            try (PreparedStatement pstmt1 = con.prepareStatement("delete from order_item where order_no=?");
                 PreparedStatement pstmt2 = con.prepareStatement("delete from orders where order_no=?")) {
                pstmt1.setInt(1, orderNo);
                pstmt1.executeUpdate();
                pstmt2.setInt(1, orderNo);
                pstmt2.executeUpdate();
                con.commit();
            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주문 상세 조회 : 한 주문에 담긴 메뉴들
    public ArrayList<OrderItem> selectItem(int orderNo) {
        ArrayList<OrderItem> list = new ArrayList<OrderItem>();
        String sql = "select i.order_no, i.menu_no, m.menu_name, m.price, i.quantity, i.memo "
                + "from order_item i, menu m "
                + "where i.menu_no = m.menu_no and i.order_no = ? "
                + "order by i.menu_no";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderNo);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                OrderItem item = new OrderItem();
                item.setOrderNo(rs.getInt("order_no"));
                item.setMenuNo(rs.getInt("menu_no"));
                item.setMenuName(rs.getString("menu_name"));
                item.setPrice(rs.getInt("price"));
                item.setQuantity(rs.getInt("quantity"));
                item.setMemo(rs.getString("memo"));
                list.add(item);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 주문에 메뉴 담기
    public void insertItem(OrderItem item) {
        String sql = "insert into order_item (order_no, menu_no, quantity, memo) values (?, ?, ?, ?)";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, item.getOrderNo());
            pstmt.setInt(2, item.getMenuNo());
            pstmt.setInt(3, item.getQuantity());
            pstmt.setString(4, item.getMemo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 주문에서 메뉴 빼기
    public void deleteItem(int orderNo, int menuNo) {
        String sql = "delete from order_item where order_no=? and menu_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, orderNo);
            pstmt.setInt(2, menuNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 결제수단은 비어 있을 수 있다(NULL 허용)
    private void setPayMethod(PreparedStatement pstmt, int index, String payMethod) throws SQLException {
        if (payMethod == null) {
            pstmt.setNull(index, Types.VARCHAR);
        } else {
            pstmt.setString(index, payMethod);
        }
    }
}
