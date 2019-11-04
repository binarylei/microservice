package binarylei.shardingjdbc.jdbc;

import binarylei.shardingjdbc.entity.User;
import org.junit.Test;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JdbcTest {
    @Test
    public void testJdbc() throws IOException {
        Connection conn = null;
        Statement stmt = null;
        User userInfo = new User();

        try {
            // 注册 JDBC 驱动
            Class.forName("com.mysql.jdbc.Driver");

            // 打开连接
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ds0",
                    "root", "123456");

            // 执行查询
            stmt = conn.createStatement();
            String sql = "SELECT user_id, name, age FROM user where user_id = 1";
            ResultSet rs = stmt.executeQuery(sql);

            // 获取结果集
            while (rs.next()) {
                Long userId = rs.getLong("user_id");
                String name = rs.getString("name");
                String age = rs.getString("age");
                userInfo.setUserId(userId);
            }
            System.out.println(userInfo);

            rs.close();
            stmt.close();
            conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
            } catch (SQLException se2) {
            }
            try {
                if (conn != null) conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
    }

}
