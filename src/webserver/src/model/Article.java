package model;

public class Article {

	private long id;
	private String article_name, category, descript;
	
	public Article(long id, String article_name, String category, String descript) {
		super();
		this.id = id;
		this.article_name = article_name;
		this.category = category;
		this.descript = descript;
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
