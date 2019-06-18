package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DatabaseException;
import other.UserDependableConfiguration;

public class SQLObject {

	private static final String sql_qual = "jdbc:sqlite:";
	
	public SQLObject () {
		super();
	}
	
	public static Connection connectDatabase() throws DatabaseException {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection(SQLObject.sql_qual + UserDependableConfiguration.sqlfilename);
		} catch (SQLException | ClassNotFoundException e) {
			System.out.println(e.toString());
			throw new DatabaseException(e.toString());
		}
	}
	
	public static void closeDatabase(Connection conn) throws SQLException {
			conn.close();
	}
	
}
