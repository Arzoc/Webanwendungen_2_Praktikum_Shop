package model;

public class Account {

	private long id;
	private String first_name, last_name, email, phone, last_login, pwd_hash;
	
	public Account(int id, String first_name, String last_name, String email, String phone, String last_login, String pwd_hash) {
		super();
		this.id = id;
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone = phone;
		this.last_login = last_login;
		this.pwd_hash = pwd_hash;
	}

	public long getId() {
		return id;
	}

	public String getFirst_name() {
		return first_name;
	}

	public String getLast_name() {
		return last_name;
	}

	public String getEmail() {
		return email;
	}

	public String getPhone() {
		return phone;
	}

	public String getLast_login() {
		return last_login;
	}

	public String getPwd_hash() {
		return pwd_hash;
	}
	
}
