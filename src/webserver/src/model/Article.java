package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

import exceptions.DatabaseException;
import exceptions.InvalidArticleIdException;
import exceptions.InvalidCategoryException;

public class Article extends SQLObject {

	private long id;
	private String article_name, category, descript;
	private float cost;
	
	public Article(long id, String article_name, String category, String descript, float cost) {
		super();
		this.id = id;
		this.article_name = article_name;
		this.category = category;
		this.descript = descript;
		this.cost = cost;
	}
	
	public Article(String article_name, String category, String descript, float cost) {
		super();
		this.article_name = article_name;
		this.category = category;
		this.descript = descript;
		this.cost = cost;
	}
	
	public static Vector<Article> get_entries() throws DatabaseException {
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
						res.getString("descript"),
						res.getFloat("cost")
						));
			}
			Article.closeDatabase(conn);
			return entries;
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw new DatabaseException();
		}
	}

	public static Vector<Article> get_entries(String category) throws DatabaseException {
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
						res.getString("descript"),
						res.getFloat("cost")
						));
			}
			Article.closeDatabase(conn);
			return entries;
		} catch (SQLException e) {
			System.out.println(e.toString());
			throw new DatabaseException();
		}
	}
	
	public static Article get_by_id(long id) throws DatabaseException, InvalidArticleIdException {
		Connection conn = Article.connectDatabase();
		try {
			PreparedStatement prep = conn.prepareStatement("select article_name, category, descript, cost from article where article.id = ?;");
			prep.setLong(1, id);
			ResultSet res = prep.executeQuery();
			if (!res.next()) 
				throw new InvalidArticleIdException();
			return new Article(
					res.getString("article_name"),
					res.getString("category"),
					res.getString("descript"),
					res.getFloat("cost")
					);
		} catch (SQLException e) {
			throw new DatabaseException(e.toString());
		}
	}
	
	/* returns number of articles in specific category */
	public static long get_num_articles_in_category(String category) throws DatabaseException, InvalidCategoryException {
		long num_articles;
		Connection conn = Article.connectDatabase();
		try {
			PreparedStatement prep = conn.prepareStatement("select count(id) as c from article where category = ?;");
			prep.setString(1, category);
			ResultSet res = prep.executeQuery();
			if (!res.next()) 
				throw new InvalidCategoryException();
			num_articles = res.getLong("c");
			Article.closeDatabase(conn);
			return num_articles;
		} catch (SQLException e) {
			throw new DatabaseException(e.toString());
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

	public float getCost() {
		return cost;
	}
	
}
