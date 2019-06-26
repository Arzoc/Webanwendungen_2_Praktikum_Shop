package model;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import java.nio.charset.StandardCharsets;

import exceptions.DatabaseException;
import exceptions.InvalidPasswordException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;

public class Account extends SQLObject {

	private long id = -1;
	private String first_name, last_name, email, phone, last_login, pwd_hash;
	
	public static class ViewableAccount {
		private String first_name, last_name, email, phone, last_login;
		
		public ViewableAccount(String email, String first_name, String last_name, String phone, String last_login) {
			this.email = email;
			this.first_name = first_name;
			this.last_name = last_name;
			this.phone = phone;
			this.last_login = last_login;
		}
	}
	
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
	
	public Account(String first_name, String last_name, String email, String phone, String last_login, String pwd_hash) {
		super();
		this.first_name = first_name;
		this.last_name = last_name;
		this.email = email;
		this.phone = phone;
		this.last_login = last_login;
		this.pwd_hash = pwd_hash;
	}
	
	public Account() {
		super();
	}
	
	/* find with unique key in order (id, email) and fill found information in account */
	public static Account fill(Account acc) throws UserNotFoundException, DatabaseException {
		PreparedStatement prep;
		Connection conn = SQLObject.connectDatabase();
		ResultSet res;
		Account account;
		try {
			if (acc.getId() != -1) {
				prep = conn.prepareStatement("select * from account where account.id = ?;");
				prep.setLong(1, acc.getId());
				res = prep.executeQuery();
				if (res.next() == false) 
					throw new UserNotFoundException();
				account = new Account(
						res.getInt("id"),
						res.getString("first_name"),
						res.getString("last_name"),
						res.getString("email"),
						res.getString("phone"),
						res.getString("last_login"),
						res.getString("pwd_hash")
						);
				return account;
			} 
			else if (acc.email != null) {
				prep = conn.prepareStatement("select * from account where account.email = ?;");
				prep.setString(1, acc.email);
				res = prep.executeQuery();
				if (res.next() == false)
					throw new UserNotFoundException();
				account = new Account(
						res.getInt("id"),
						res.getString("first_name"),
						res.getString("last_name"),
						res.getString("email"),
						res.getString("phone"),
						res.getString("last_login"),
						res.getString("pwd_hash")
						);
				return account;
			}
			else
				throw new UserNotFoundException();
		} catch (SQLException e) {
			throw new DatabaseException(e.toString());
		}
	}

	/* get all entries of table account */
	public static Vector<Account> getEntries() throws DatabaseException {
		Vector<Account> entries = new Vector<Account>();
		Connection conn = Account.connectDatabase();
		try {
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery("select * from account order by rowid desc");
			while (res.next()) {
				entries.add(new Account(
						res.getInt("id"),
						res.getString("first_name"),
						res.getString("last_name"),
						res.getString("email"),
						res.getString("phone"),
						res.getString("last_login"),
						res.getString("pwd_hash")
						));
			}
			Account.closeDatabase(conn);
			return entries;
		} catch (SQLException e) {
			throw new DatabaseException(e.toString());
		}
	}
	
	/* hash @param password in sha256 */
	public static String generatePasswordHash(String password) throws NoSuchAlgorithmException {
		MessageDigest digest = MessageDigest.getInstance("SHA-256");
		byte[] encoded = digest.digest(password.getBytes(StandardCharsets.UTF_8));
		StringBuilder sb = new StringBuilder();
        for (int i = 0; i < encoded.length; i++) {
            sb.append(Integer.toString((encoded[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
	}
	
	/* insert @param account into table account */
	public static void insertNew(Account account) throws UserAlreadyExistsException, DatabaseException {
		Connection conn = Account.connectDatabase();
		Account tmp = new Account();
		tmp.setEmail(account.getEmail().trim());
		try {
			Account.fill(tmp);
			throw new UserAlreadyExistsException();
		} catch (UserNotFoundException e) {
			/* No user with this email found -> great! */
		}
		try {
			PreparedStatement prep = conn.prepareStatement("insert into account (email, first_name, last_name, phone, pwd_hash) values (?, ?, ?, ?, ?);");
			prep.setString(1, account.email.trim());
			prep.setString(2, account.first_name.trim());
			prep.setString(3, account.last_name.trim());
			prep.setString(4, account.phone.trim());
			prep.setString(5,  account.pwd_hash.trim());
			prep.executeUpdate();
			Account.closeDatabase(conn);
		} catch (SQLException e) {
			throw new DatabaseException(e.toString());
		}
	}
	
	/* verify the @param password for this account */
	public void checkPassword(String password) throws InvalidPasswordException {
		if (!this.pwd_hash.trim().toUpperCase().equals(password.trim().toUpperCase()))
			throw new InvalidPasswordException();
		return;
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

	public void setId(long id) {
		this.id = id;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
}
