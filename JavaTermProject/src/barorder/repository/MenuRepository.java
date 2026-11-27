package barorder.repository;

import barorder.domain.Category;
import barorder.domain.Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// menu 테이블 CRUD + category 조회
public class MenuRepository {

    // 조회 : selectedIndex 0=메뉴명, 1=분류명 으로 검색
    public ArrayList<Menu> select(String searchWord, int selectedIndex) {
        ArrayList<Menu> list = new ArrayList<Menu>();
        String[] columnName = {"m.menu_name", "c.category_name"};
        String sql = "select m.menu_no, m.menu_name, m.price, m.sale_status, m.category_code, c.category_name "
                + "from menu m, category c "
                + "where m.category_code = c.category_code "
                + "and " + columnName[selectedIndex] + " like ? "
                + "order by m.menu_no";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, "%" + searchWord + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Menu menu = new Menu();
                menu.setMenuNo(rs.getInt("menu_no"));
                menu.setMenuName(rs.getString("menu_name"));
                menu.setPrice(rs.getInt("price"));
                menu.setSaleStatus(rs.getString("sale_status"));
                menu.setCategoryCode(rs.getString("category_code"));
                menu.setCategoryName(rs.getString("category_name"));
                list.add(menu);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 메뉴분류 전체 (콤보박스용)
    public ArrayList<Category> selectCategory() {
        ArrayList<Category> list = new ArrayList<Category>();
        String sql = "select category_code, category_name from category order by category_code";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Category category = new Category();
                category.setCategoryCode(rs.getString("category_code"));
                category.setCategoryName(rs.getString("category_name"));
                list.add(category);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return list;
    }

    // 추가 : 메뉴번호는 (가장 큰 번호 + 1)로 자동 부여
    public void insert(Menu menu) {
        String sql = "insert into menu (menu_no, menu_name, price, sale_status, category_code) "
                + "values ((select nvl(max(menu_no), 0) + 1 from menu), ?, ?, ?, ?)";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, menu.getMenuName());
            pstmt.setInt(2, menu.getPrice());
            pstmt.setString(3, menu.getSaleStatus());
            pstmt.setString(4, menu.getCategoryCode());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 수정
    public void update(Menu menu) {
        String sql = "update menu set menu_name=?, price=?, sale_status=?, category_code=? where menu_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, menu.getMenuName());
            pstmt.setInt(2, menu.getPrice());
            pstmt.setString(3, menu.getSaleStatus());
            pstmt.setString(4, menu.getCategoryCode());
            pstmt.setInt(5, menu.getMenuNo());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // 삭제
    public void delete(int menuNo) {
        String sql = "delete from menu where menu_no=?";
        try (Connection con = JDBCConnector.getConnection();
             PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setInt(1, menuNo);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
