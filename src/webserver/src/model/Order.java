package model;

public class Order {

	private long id, order_id, article_id, quantity;
	
	public Order(long id, long order_id, long article_id, long quantity) {
		super();
		this.id = id;
		this.order_id = order_id;
		this.article_id = article_id;
		this.quantity = quantity;
	}

	public long getId() {
		return id;
	}

	public long getOrder_id() {
		return order_id;
	}

	public long getArticle_id() {
		return article_id;
	}

	public long getQuantity() {
		return quantity;
	}
	
}
