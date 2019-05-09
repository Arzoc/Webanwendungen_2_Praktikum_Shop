package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import exceptions.DatabaseException;

public class OrderHistory extends SQLObject {

	private long id, order_state, account_id;
	private String buydate;

	public OrderHistory(long id, long account_id, long order_state, String buydate) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.order_state = order_state;
		this.buydate = buydate;
	}

	public Vector<OrderHistory> getEntries(String email) throws DatabaseException {
		Vector<OrderHistory> entries = new Vector<OrderHistory>();
		Connection conn = Account.connectDatabase();
		try {
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery("select * from order_history,account where order_history.account_id = account.id and ");
			while (res.next()) {
				entries.add(new OrderHistory(
						res.getLong("id"),
						res.getLong("account_id"),
						res.getLong("order_state"),
						res.getString("buydate")
						));
			}
			Account.closeDatabase(conn);
			return entries;
		} catch (SQLException e) {
			throw new DatabaseException();
		}
	}

	public long getId() {
		return id;
	}

	public long getOrder_state() {
		return order_state;
	}

	public long getAccount_id() {
		return account_id;
	}
	
	public String getBuydate() {
		return buydate;
	}
}
