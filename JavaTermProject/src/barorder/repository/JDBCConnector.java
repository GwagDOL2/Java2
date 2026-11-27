package barorder.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Oracle DB 연결을 만들어 주는 클래스
public class JDBCConnector {
    private static final String DRIVER_PATH = "oracle.jdbc.driver.OracleDriver";
    private static final String URL = "jdbc:oracle:thin:@localhost:1521/xe";
    private static final String USER_NAME = "c##mkj";
    private static final String PASSWORD = "1234";

    public static Connection getConnection() {
        try {
            // 1. JDBC Driver 클래스 메모리 로드
            Class.forName(DRIVER_PATH);
            // 2. DB 연결
            return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("JDBC Driver 클래스를 찾을 수 없습니다. (ojdbc 라이브러리 확인)", e);
        } catch (SQLException e) {
            throw new RuntimeException("DB 연결에 실패했습니다. (계정/비밀번호/Oracle 실행 여부 확인)", e);
        }
    }

    public static void main(String[] args) {
        getConnection();
        System.out.println("Connection Done Successfully");
    }
}
