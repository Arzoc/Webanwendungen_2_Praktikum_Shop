package model;

public class Paypal extends SQLObject {

	private long id, account_id;
	private String email;
	
	public Paypal(long id, long account_id, String email) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.email = email;
	}

	public long getId() {
		return id;
	}

	public long getAccount_id() {
		return account_id;
	}

	public String getEmail() {
		return email;
	}
	
}
