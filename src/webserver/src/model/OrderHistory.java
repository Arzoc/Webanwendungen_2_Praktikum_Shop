package model;

public class OrderHistory {

	private long id, order_state, account_id;

	public OrderHistory(long id, long account_id, long order_state) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.order_state = order_state;
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
}
