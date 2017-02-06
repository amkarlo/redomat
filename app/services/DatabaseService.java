package services;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public final class DatabaseService {
    public static final String CONNECTION_STRING = "jdbc:postgresql://localhost/Redomat";
	public static final String USERNAME = "postgres";
	public static final String PASSWORD = "1234";

    private static PreparedStatement updateStatement(PreparedStatement preparedStatement, Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
        return preparedStatement;
    }
	
    public static ResultSet executeQuery(String query, Object... values) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD); 
            stmt = conn.prepareStatement(query);
            stmt = updateStatement(stmt, values);
            ResultSet rs = stmt.executeQuery();
            conn.close();
            return rs;
        }
        catch (SQLException e) {
             System.out.println(e.toString());
        }
        finally {
			if (conn != null) {
				conn.close();
			}
		}
        return null;
    }

    public static void executeUpdate(String query, Object... values) throws SQLException {
        Connection conn = null;
        PreparedStatement stmt = null;
        try {
            conn = DriverManager.getConnection(CONNECTION_STRING, USERNAME, PASSWORD); 
            stmt = conn.prepareStatement(query);
            stmt = updateStatement(stmt, values);
            stmt.executeUpdate();
        }
        catch (SQLException e) {
             System.out.println(e.toString());
        } 
        finally {
			if (stmt != null) {
				stmt.close();
			}
			if (conn != null) {
				conn.close();
			}
		}
    }
}