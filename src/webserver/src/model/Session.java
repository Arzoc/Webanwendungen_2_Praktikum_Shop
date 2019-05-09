package model;


/* actually not needed because we use token based auth... but for user tracking... */
public class Session extends SQLObject {
	
	private long id, account_id;
	private String cookie;
	private boolean isActive;
	
	public Session(long id, long account_id, String cookie, boolean isActive) {
		super();
		this.id = id;
		this.account_id = account_id;
		this.cookie = cookie;
		this.isActive = isActive;
	}

	public long getId() {
		return id;
	}

	public long getAccount_id() {
		return account_id;
	}

	public String getCookie() {
		return cookie;
	}

	public boolean isActive() {
		return isActive;
	}

}
