package rest.services;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;

import exceptions.DatabaseException;
import exceptions.InvalidPasswordException;
import exceptions.InvalidTokenException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import model.Account;
import model.Order;
import model.SQLObject;
import other.FullOrder;
import other.JwtManager;

@Path("/users")
public class Users {

	@Context
	UriInfo uriInfo;
	
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Response login(@FormParam("email") String email, @FormParam("password") String passwordHash) {
		JwtManager jwt = JwtManager.getInstance();
		Account a = new Account();
		a.setEmail(email.trim());
		try {
			a = Account.fill(a);
			/* if password in clear form, uncomment this */
			//String passwordHash = Account.generatePasswordHash(password);
			a.checkPassword(passwordHash);
			String token = jwt.issueToken(a.getEmail(), uriInfo.getAbsolutePath().toString().trim());
			return Response.status(Response.Status.OK).header("Authorization", "Bearer " + token).build();
		} catch (UserNotFoundException | InvalidPasswordException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (DatabaseException /*| NoSuchAlgorithmException*/ e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@POST
	@Path("/create")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Response create(
			@FormParam("firstname") String first_name,
			@FormParam("lastname") String last_name,
			@FormParam("email") String email,
			@FormParam("phone") String phone,
			@FormParam("password") String password,
			@Context HttpHeaders headers
			) {
		String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && !authHeader.trim().equals(""))
			return Response.status(Response.Status.OK).entity("{ \"return\": 2; \"msg\": \"already token supplied\"; }").build();
		Account account = new Account();
		try {
			Account.insertNew(account);
			return Response.status(Response.Status.OK).entity("{ return: 0 }").build();
		} catch (UserAlreadyExistsException e) {
			return Response.status(Response.Status.OK).entity("{ \"return\": 1; \"msg\": \"UserAlreadyExists\"; }").build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/order_history")
	@Produces(MediaType.APPLICATION_JSON)
	public Response order_history(@Context HttpHeaders headers) {
		JwtManager jwt = JwtManager.getInstance();
		String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		try {
			if (authHeader == null) 
				throw new InvalidTokenException();
			String token = jwt.validateToken(authHeader);
			String email = jwt.getEmail(token);
			String json = this.get_formatted_order_history(email);
			return Response.status(Response.Status.OK).entity(json).build();
		} catch (InvalidTokenException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	private String get_formatted_order_history(String email) throws DatabaseException {
		try {
			Gson gson = new Gson();
			Connection conn = SQLObject.connectDatabase();
			PreparedStatement prep = conn.prepareStatement("select * from order_history,account where order_history.account_id = account.id and account.email = ?;");
			prep.setString(1, email.trim());
			ResultSet order_ids = prep.executeQuery();
			Vector<FullOrder> orders = new Vector<FullOrder>();
			ResultSet tmpRes;
			Vector<Order> tmpOrders;
			while (order_ids.next()) {
				tmpOrders = new Vector<Order>();
				prep = conn.prepareStatement("select * from orders where order_id = ?;");
				prep.setLong(1, order_ids.getLong("id"));
				tmpRes = prep.executeQuery();
				while (tmpRes.next()) {
					tmpOrders.add(new Order(
							tmpRes.getLong("id"),
							tmpRes.getLong("order_id"),
							tmpRes.getLong("article_id"),
							tmpRes.getLong("quantity"),
							tmpRes.getFloat("cost_at_purchse")
							));
				}
				orders.add(new FullOrder(
						tmpOrders,
						order_ids.getString("buydate")
						));
			}
			SQLObject.closeDatabase(conn);
			return gson.toJson(orders);
		} catch (SQLException e) {
			throw new DatabaseException();
		}		
	}
	
}
