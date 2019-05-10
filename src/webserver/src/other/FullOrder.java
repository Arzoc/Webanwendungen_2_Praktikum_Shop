package other;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.Instant;
import java.util.Vector;

import exceptions.DatabaseException;
import exceptions.InvalidTransactionParametersException;
import model.Creditcard;
import model.Order;
import model.Paypal;
import model.SQLObject;

public class FullOrder {
	
	Vector<Order> orders;
	String buydate;
	
	public FullOrder(Vector<Order> orders, String buydate) {
		super();
		this.orders = orders;
		this.buydate = buydate;
	}
	
	public static void insert_transaction(CheckoutParameters params, String account_email) throws DatabaseException, InvalidTransactionParametersException {
		if (params.getPaypal_email() != null && !params.getPaypal_email().trim().equals("")) {
			FullOrder.insert_transaction_paypal(params.getPaypal_email(), account_email.trim());
		} else if (params.getCard_number() != null && !params.getCard_number().trim().equals("")) {
			FullOrder.insert_transaction_creditcard(params.getCard_number(), account_email.trim());
		} else if (params.getPayment_new() != null) {
			CheckoutParameters.NewPaymentMethod method = params.getPayment_new();
			if (method.getPaypal_email() != null && !method.getPaypal_email().equals("")) {
				Paypal.insert_new(new Paypal(0, method.getPaypal_email().trim()));
				FullOrder.insert_transaction_paypal(method.getPaypal_email().trim(), account_email.trim());
			} else if (method.getCreditcard_card_number() != null && !method.getCreditcard_card_number().equals("")) {
				Creditcard.insert_new(new Creditcard(
						0,
						method.getCreditcard_card_number().trim(),
						method.getCreditcard_expire().trim(),
						method.getCreditcard_first_name().trim(),
						method.getCreditcard_last_name().trim()
						));
				FullOrder.insert_transaction_creditcard(method.getCreditcard_card_number().trim(), account_email.trim());
			} else throw new InvalidTransactionParametersException();
		} else {
			throw new InvalidTransactionParametersException();
		}
		//TODO get order_history_id
		long order_history_id = 0;
		for (CheckoutParameters.Article article : params.getArticles()) {
			Order.insert_new(new Order(0, order_history_id, article.getArticle_id(), article.getQuantity(), 0));
		}
	}

	private static void insert_transaction_paypal(String paypal_email, String account_email) throws DatabaseException {
		Connection conn = SQLObject.connectDatabase();
		String insertString;
		PreparedStatement prep;
		insertString = "insert into order_history (order_state, buydate, payment_paypal_id, account_id) values "
				+ "(0, ?, (select paypal.id from paypal where paypal.email = ?), (select account.id from account where account.email = ?))";
		try {
			prep = conn.prepareStatement(insertString);
			prep.setString(1, Long.toString(Instant.now().getEpochSecond()));
			prep.setString(2, paypal_email.trim());
			prep.setString(3, account_email.trim());
			prep.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException();
		}
	}
	
	private static void insert_transaction_creditcard(String card_number, String account_email) throws DatabaseException {
		Connection conn = SQLObject.connectDatabase();
		String insertString;
		PreparedStatement prep;
		insertString = "insert into order_history (order_state, buydate, payment_creditcard_id, account_id) values"
				+ "(0, ?, (select creditcard.id from creditcard where creditcard.card_number = ?), (select account.id from account where account.email = ?))";
		try {
			prep = conn.prepareStatement(insertString);
			prep.setString(1, Long.toString(Instant.now().getEpochSecond()));
			prep.setString(2, card_number.trim());
			prep.setString(3, account_email.trim());
			prep.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException();
		}
	}
	
}
