package model;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import exceptions.DatabaseException;

public class OrderHistory extends SQLObject {

	private long id, order_state, payment_paypal_id, payment_creditcard_id, account_id;
	private String buydate;

	public OrderHistory(long id, long order_state, String buydate, long payment_paypal_id, long payment_creditcard_id, long account_id) {
		super();
		this.id = id;
		this.order_state = order_state;
		this.buydate = buydate;
		this.payment_creditcard_id = payment_creditcard_id;
		this.payment_paypal_id = payment_paypal_id;
	}

	/* get all entries of table order_history */
	public static Vector<OrderHistory> getEntries(String email) throws DatabaseException {
		Vector<OrderHistory> entries = new Vector<OrderHistory>();
		Connection conn = Account.connectDatabase();
		try {
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery(
					"select * from order_history,account,creditcard,paypal where order_history.account_id = account.id and ");
			while (res.next()) {
				entries.add(new OrderHistory(
						res.getLong("id"),
						res.getLong("order_state"),
						res.getString("buydate"),
						res.getLong("paymend_paypal_id"),
						res.getLong("payment_creditcard_id"),
						res.getLong("account_id")
						));
			}
			Account.closeDatabase(conn);
			return entries;
		} catch (SQLException e) {
			throw new DatabaseException();
		}
	}
	
	public long getAccount_id() {
		return account_id;
	}
	
	public long getId() {
		return id;
	}

	public long getOrder_state() {
		return order_state;
	}
	
	public String getBuydate() {
		return buydate;
	}

	public long getPayment_paypal_id() {
		return payment_paypal_id;
	}

	public long getPayment_creditcard_id() {
		return payment_creditcard_id;
	}
	
}
