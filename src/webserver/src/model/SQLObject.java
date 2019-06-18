package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import exceptions.DatabaseException;

public class SQLObject {

	private static final String sqlfilename = "..\\..\\..\\database\\yourshop.db";
	private static final String sql_qual = "jdbc:sqlite:";
	
	public SQLObject () {
		super();
	}
	
	public static Connection connectDatabase() throws DatabaseException {
		try {
			Class.forName("org.sqlite.JDBC");
			return DriverManager.getConnection(SQLObject.sql_qual + SQLObject.sqlfilename);
		} catch (SQLException | ClassNotFoundException e) {
			throw new DatabaseException();
		}
	}
	
	public static void closeDatabase(Connection conn) throws SQLException {
			conn.close();
	}
	
}
