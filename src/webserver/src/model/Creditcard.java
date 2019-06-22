package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exceptions.DatabaseException;

public class Creditcard extends SQLObject {

	private long id;
	private String card_number, expire, first_name, last_name;
	
	public Creditcard(long id, String card_number, String expire, String first_name, String last_name) {
		super();
		this.id = id;
		this.card_number = card_number;
		this.expire = expire;
		this.first_name = first_name;
		this.last_name= last_name;
	}
	
	public Creditcard(String card_number, String expire, String first_name, String last_name) {
		super();
		this.card_number = card_number;
		this.expire = expire;
		this.first_name = first_name;
		this.last_name= last_name;
	}
	
	public static void insert_new(Creditcard credit) throws DatabaseException {
		Connection conn = Paypal.connectDatabase();
		try {
			PreparedStatement prep = conn.prepareStatement("insert into creditcard (card_number, expire, first_name, last_name) values (?, ?, ?, ?);");
			prep.setString(1, credit.getCard_number().trim());
			prep.setString(2, credit.getExpire().trim());
			prep.setString(3, credit.getFirst_name().trim());
			prep.setString(4, credit.getLast_name().trim());
			prep.executeUpdate();
		} catch (SQLException e) { // TODO maybe check what the error was -> payment already exists
			throw new DatabaseException(e.toString());
		}
	}

	public long getId() {
		return id;
	}

	public String getCard_number() {
		return card_number;
	}

	public String getExpire() {
		return expire;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}
	
}
