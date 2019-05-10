package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import exceptions.DatabaseException;

public class Order extends SQLObject {

	private long id, order_history_id, article_id, quantity;
	private float cost_at_purchase;
	
	public Order(long id, long order_history_id, long article_id, long quantity, float cost_at_purchase) {
		super();
		this.id = id;
		this.order_history_id = order_history_id;
		this.article_id = article_id;
		this.quantity = quantity;
		this.cost_at_purchase = cost_at_purchase;
	}
	
	// quantity, article_id, order_history_id needed -> rest irrelevant
	public static void insert_new(Order order) throws DatabaseException {
		Connection conn = Order.connectDatabase();
		try {
			PreparedStatement prep = conn.prepareStatement("insert into orders (quantity, order_history_id, article_id, cost_at_purchase) values "
					+ "(?, ?, ?, (select cost from article where article.id = ?));"
					);
			prep.setLong(1, order.getQuantity());
			prep.setLong(2, order.getOrder_history_id());
			prep.setLong(3, order.getArticle_id());
			prep.setLong(4, order.getArticle_id());
			prep.executeUpdate();
		} catch (SQLException e) {
			throw new DatabaseException();
		}
	}

	public long getId() {
		return id;
	}

	public long getOrder_history_id() {
		return order_history_id;
	}

	public long getArticle_id() {
		return article_id;
	}

	public long getQuantity() {
		return quantity;
	}

	public float getCost_at_purchase() {
		return cost_at_purchase;
	}
	
}
