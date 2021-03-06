package rest.services;

import java.util.List;
import java.util.Vector;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;

import exceptions.DatabaseException;
import exceptions.InvalidArticleIdException;
import exceptions.InvalidCategoryException;
import model.Article;

@Path("/articles")
public class Articles {
	
	private static final int ARTICLES_LIST_DEFAULT_PER_PAGE = 50;
	
	@GET
	@Path("by-category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response articles_by_category(
			@QueryParam("category") String category, 
			@QueryParam("per_page") int per_page, 
			@QueryParam("page") int page) {
		Gson gson = new Gson();
		try {
			Vector<Article> entries;
			if (category != null)
				entries = Article.get_entries(category);
			else 
				entries = Article.get_entries();

			if (page == 0) 
				page = 1;
			if (per_page == 0) {
				per_page = ARTICLES_LIST_DEFAULT_PER_PAGE;
			}
			int start = per_page * (page - 1);
			int end = per_page * page;
			if (end > entries.size())
				end = entries.size();
			if (start > entries.size())
				return Response.status(Response.Status.OK).entity("[]").build();
			List<Article> selectedEntries = entries.subList(start, end);

			String json = gson.toJson(selectedEntries);
			return Response.status(Response.Status.OK).entity(json).build();
		} catch (DatabaseException e) {
			System.out.println(e.toString());
			return Response.status(Response.Status.OK).entity("{\"return\": 1, \"msg\": \"DatabaseException\"}").build();
		}
	}
	
	@GET
	@Path("num-items-in-category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response items_in_category(@QueryParam("category") String category) {
		long num_articles;
		try {
			num_articles = Article.get_num_articles_in_category(category);
			return Response.status(Response.Status.OK).entity("{ \" return\": 0, \"num_articles\" : " + Long.toString(num_articles) + " }").build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		} catch (InvalidCategoryException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		}
	}
	
	@GET
	@Path("/by-id")
	@Produces(MediaType.APPLICATION_JSON)
	public Response by_id(@QueryParam("article_id") long article_id) {
		Gson gson = new Gson();
		try {
			Article article = Article.get_by_id(article_id);
			return Response.status(Response.Status.OK).entity(gson.toJson(article)).build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("{ \"return\": 1 \"msg\": \"DatabaseError\" }").build();
		} catch (InvalidArticleIdException e) {
			return Response.status(Response.Status.BAD_REQUEST).entity("{ \"return\": 2, \"msg\": \"InvalidArticleId\" }").build();
		}
	}
}
