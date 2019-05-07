package model;

public class Creditcard {

	private long id, account_id;
	private String card_number, expire, first_name, last_name;
	
	public Creditcard(long id, long account_id, String card_number, String expire, String first_name, String last_name) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.card_number = card_number;
		this.expire = expire;
		this.first_name = first_name;
		this.last_name= last_name;
	}

	public long getId() {
		return id;
	}

	public long getAccount_id() {
		return account_id;
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
