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
import model.Article;

@Path("/articles")
public class Articles {
	
	@GET
	@Path("by-category")
	@Produces(MediaType.APPLICATION_JSON)
	public Response articlesByCategory(
			@QueryParam("category") String category, 
			@QueryParam("per_page") int per_page, 
			@QueryParam("page") int page,
			@QueryParam("token") String token) {
		Gson gson = new Gson();
		try {
			Vector<Article> entries;
			if (category != null)
				entries = Article.getEntries(category);
			else 
				entries = Article.getEntries();
			
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
			return Response.status(Response.Status.OK).entity("{ return: 1; msg: DatabaseException; }").build();
		}
	}
}
