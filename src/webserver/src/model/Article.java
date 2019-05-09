package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import exceptions.DatabaseException;

public class Article extends SQLObject {

	private long id;
	private String article_name, category, descript;
	
	public Article(long id, String article_name, String category, String descript) {
		super();
		this.id = id;
		this.article_name = article_name;
		this.category = category;
		this.descript = descript;
	}
	
	public static Vector<Article> getEntries() throws DatabaseException {
		Vector<Article> entries = new Vector<Article>();
		Connection conn = Article.connectDatabase();
		try {
			Statement statement = conn.createStatement();
			ResultSet res = statement.executeQuery("select * from article order by rowid desc");
			while (res.next()) {
				entries.add(new Article(
						res.getLong("id"),
						res.getString("article_name"),
						res.getString("category"),
						res.getString("descript")
						));
			}
			Article.closeDatabase(conn);
			return entries;
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw new DatabaseException();
		}
	}

	public static Vector<Article> getEntries(String category) throws DatabaseException {
		Vector<Article> entries = new Vector<Article>();
		Connection conn = Article.connectDatabase();
		try {
			PreparedStatement prep = conn.prepareStatement("select * from article where category = ? order by rowid desc");
			prep.setString(1, category.trim());
			ResultSet res = prep.executeQuery();
			while (res.next()) {
				entries.add(new Article(
						res.getLong("id"),
						res.getString("article_name"),
						res.getString("category"),
						res.getString("descript")
						));
			}
			Article.closeDatabase(conn);
			return entries;
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw new DatabaseException();
		}
	}
	
	public long getId() {
		return id;
	}

	public String getArticle_name() {
		return article_name;
	}

	public String getCategory() {
		return category;
	}

	public String getDescript() {
		return descript;
	}
	
}
