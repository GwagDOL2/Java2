package book_management.repository;

import book_management.domain.Book;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class BookRepository {

    //    도서 검색 (selectedIndex : 0=도서명, 1=출판사, 2=저자명)
    public ArrayList<Book> select(String searchWord, int selectedIndex){
        Connection con = JDBCConnector.getConnection();
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Book> bookList = new ArrayList<Book>();
        String[] columnName = {"b.name", "p.publish_name", "b.author"};
        String sql = "select b.isbn, b.name, b.author, b.price, b.inventory, "
                + "c.category_name, p.publish_name "
                + "from book b, category c, publish p "
                + "where b.category_id = c.category_id and b.publish_id = p.publish_id "
                + "and " + columnName[selectedIndex] + " like ? "
                + "order by b.isbn";
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, "%" + searchWord + "%");
            rs = pstmt.executeQuery();
            while (rs.next()){
                Book book = new Book();
                book.setIsbn(rs.getInt("isbn"));
                book.setName(rs.getString("name"));
                book.setAuthor(rs.getString("author"));
                book.setPrice(rs.getInt("price"));
                book.setInventory(rs.getInt("inventory"));
                book.setCategory_name(rs.getString("category_name"));
                book.setPublish_name(rs.getString("publish_name"));
                bookList.add(book);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return bookList;
    }

    //    도서 추가
    public void insert(Book book){
        Connection con = JDBCConnector.getConnection();
        PreparedStatement pstmt = null;
        String sql = "insert into book values(?, ?, ?, ?, ?, ?, ?)";

//      출판사명 id 정수값으로 변경
        int publish_id = publishId(con, book.getPublish_name());
//      카테고리 id 정수값으로 변경
        int category_id = categoryId(book.getCategory_name());

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, String.valueOf(book.getIsbn()));
            pstmt.setString(2, book.getName());
            pstmt.setString(3, book.getAuthor());
            pstmt.setInt(4, book.getPrice());
            pstmt.setInt(5, book.getInventory());
            pstmt.setInt(6, category_id);
            pstmt.setInt(7, publish_id);

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //    도서 수정
    public void update(Book book){
        Connection con = JDBCConnector.getConnection();
        PreparedStatement pstmt = null;
        String sql = "update book set name=?, author=?, price=?, inventory=?, "
                + "category_id=?, publish_id=? where isbn=?";

//      출판사명 id 정수값으로 변경
        int publish_id = publishId(con, book.getPublish_name());
//      카테고리 id 정수값으로 변경
        int category_id = categoryId(book.getCategory_name());

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, book.getName());
            pstmt.setString(2, book.getAuthor());
            pstmt.setInt(3, book.getPrice());
            pstmt.setInt(4, book.getInventory());
            pstmt.setInt(5, category_id);
            pstmt.setInt(6, publish_id);
            pstmt.setString(7, String.valueOf(book.getIsbn()));

            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //    도서 삭제
    public void delete(String isbn){
        Connection con = JDBCConnector.getConnection();
        PreparedStatement pstmt = null;
        String sql = "delete from book where isbn=?";
        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, isbn);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (pstmt != null) pstmt.close();
                if (con != null) con.close();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    //    카테고리 이름 -> id 정수값
    private int categoryId(String category_name){
        int category_id = 0;
        switch (category_name){
            case "IT도서":
                category_id = 10;
                break;
            case "소설":
                category_id = 20;
                break;
            case "수필":
                category_id = 30;
                break;
            case "시":
                category_id = 40;
                break;
        }
        return category_id;
    }

    //    출판사 이름 -> id 정수값 (PUBLISH 테이블에서 조회)
    private int publishId(Connection con, String publish_name){
        int publish_id = 0;
        String sql = "select publish_id from publish where publish_name = ?";
        try (PreparedStatement pstmt = con.prepareStatement(sql)) {
            pstmt.setString(1, publish_name);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) publish_id = rs.getInt("publish_id");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return publish_id;
    }
}
