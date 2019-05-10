package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exceptions.DatabaseException;

public class Paypal extends SQLObject {

	private long id;
	private String email;
	
	public Paypal(long id, String email) {
		super();
		this.id = id;
		this.email = email;
	}
	
	// paypal.id not used
	public static void insert_new(Paypal paypal) throws DatabaseException {
		Connection conn = Paypal.connectDatabase();
		try {
			PreparedStatement prep = conn.prepareStatement("insert into paypal (email) values (?);");
			prep.setString(1, paypal.getEmail());
			prep.executeUpdate();
		} catch (SQLException e) { // TODO maybe check what the error was -> payment already exists
			throw new DatabaseException();
		}
	}

	public long getId() {
		return id;
	}
	
	public String getEmail() {
		return email;
	}
	
}
