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

import java.security.NoSuchAlgorithmException;

import com.google.gson.Gson;

import exceptions.DatabaseException;
import exceptions.InvalidPasswordException;
import exceptions.InvalidTokenException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import model.Account;
import model.Creditcard;
import model.Order;
import model.Paypal;
import model.SQLObject;
import other.FullOrder;
import other.JwtManager;
import other.PaymentMethodsCombined;

@Path("/users")
public class Users {

	@Context
	UriInfo uriInfo;
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Response login(@FormParam("email") String email, @FormParam("password") String password) {
		JwtManager jwt = JwtManager.getInstance();
		Account a = new Account();
		a.setEmail(email.trim());
		try {
			a = Account.fill(a);
			password = Account.generatePasswordHash(password);
			a.checkPassword(password);
			String token = jwt.issueToken(a.getEmail(), uriInfo.getAbsolutePath().toString().trim());
			return Response.status(Response.Status.OK).header("Authorization", "Bearer " + token).build();
		} catch (UserNotFoundException | InvalidPasswordException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (DatabaseException | NoSuchAlgorithmException e) {
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
			@FormParam("password") String passwordHash,
			@Context HttpHeaders headers
			) {
		String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authHeader != null && !authHeader.trim().equals(""))
			return Response.status(Response.Status.OK).entity("{ \"return\": 2; \"msg\": \"already token supplied\"; }").build();
		Account account = new Account(
				first_name.trim(),
				last_name.trim(),
				email.trim(),
				phone.trim(),
				Long.toString(System.currentTimeMillis() / 1000L),
				passwordHash.trim());
		try {
			Account.insertNew(account);
			return Response.status(Response.Status.OK).entity("{ \"return\": 0 }").build();
		} catch (UserAlreadyExistsException e) {
			return Response.status(Response.Status.OK).entity("{ \"return\": 1, \"msg\": \"UserAlreadyExists\" }").build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/payment-methods")
	@Produces(MediaType.APPLICATION_JSON)
	public Response payment_methods(@Context HttpHeaders headers) {
		JwtManager jwt = JwtManager.getInstance();
		String auth_header = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		try {
			if (auth_header == null)
				throw new InvalidTokenException();
			String token = jwt.validateToken(auth_header);
			String email = jwt.getEmail(token);
			String json = this.get_formatted_payment_methods(email);
			return Response.status(Response.Status.OK).header("Authorization", "Bearer " + token).entity(json).build();
		} catch (InvalidTokenException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/order-history")
	@Produces(MediaType.APPLICATION_JSON)
	public Response order_history(@Context HttpHeaders headers) {
		JwtManager jwt = JwtManager.getInstance();
		String auth_header = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		try {
			if (auth_header == null) 
				throw new InvalidTokenException();
			String token = jwt.validateToken(auth_header);
			String email = jwt.getEmail(token);
			String json = this.get_formatted_order_history(email);
			return Response.status(Response.Status.OK).header("Authorization", "Bearer " + token).entity(json).build();
		} catch (InvalidTokenException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("account-info")
	@Produces(MediaType.APPLICATION_JSON)
	public Response get_account_info(@Context HttpHeaders headers) {
		JwtManager jwt = JwtManager.getInstance();
		String auth_header = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		try {
			if (auth_header == null)
				throw new InvalidTokenException();
			String token = jwt.validateToken(auth_header);
			String email = jwt.getEmail(token);
			String json = this.get_formatted_user_info(email);
			return Response.status(Response.Status.OK).header("Authorization", "Bearer " + token).entity(json).build();
		} catch (UserNotFoundException e) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (InvalidTokenException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	/* find every order to fill in a full order structure, containing single orders at one buy */
	private String get_formatted_order_history(String email) throws DatabaseException {
		try {
			Gson gson = new Gson();
			Connection conn = SQLObject.connectDatabase();
			PreparedStatement prep = conn.prepareStatement("select order_history.id, order_history.buydate from order_history, account where order_history.account_id = account.id and account.email = ?;");
			prep.setString(1, email.trim());
			ResultSet order_ids = prep.executeQuery();
			Vector<FullOrder> orders = new Vector<FullOrder>();
			ResultSet tmpRes;
			Vector<Order> tmpOrders;
			while (order_ids.next()) {
				tmpOrders = new Vector<Order>();
				prep = conn.prepareStatement("select * from orders where order_history_id = ?;");
				prep.setLong(1, order_ids.getLong("id"));
				tmpRes = prep.executeQuery();
				while (tmpRes.next()) {
					tmpOrders.add(new Order(
							tmpRes.getLong("article_id"),
							tmpRes.getLong("quantity"),
							tmpRes.getFloat("cost_at_purchase")
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
			System.out.println(e.toString());
			throw new DatabaseException(e.toString());
		}		
	}
	
	/* return all payment methods formatted a a json string ready to send */
	/* TODO maybe don't find user with email, instead user Account.fill */
	private String get_formatted_payment_methods(String email) throws DatabaseException {
		Vector<Paypal> paypals;
		Vector<Creditcard> creditcards;
		Gson gson = new Gson();
		Connection conn = SQLObject.connectDatabase();
		try {
			PreparedStatement prep = conn.prepareStatement(
					"select paypal.email from account, order_history, paypal where account.id = order_history.account_id and order_history.payment_paypal_id = paypal.id and account.email = ?;");
			prep.setString(1, email.trim());
			ResultSet res = prep.executeQuery();
			paypals = new Vector<Paypal>();
			while (res.next()) {
				paypals.add(new Paypal(res.getString("email")));
			}
			prep = conn.prepareStatement(" select creditcard.card_number, creditcard.expire, creditcard.first_name, creditcard.last_name from account, order_history, creditcard where order_history.account_id = account.id and account.email = ? and order_history.payment_creditcard_id = creditcard.id;");
			prep.setString(1, email.trim());
			res = prep.executeQuery();
			creditcards = new Vector<Creditcard>();
			while (res.next()) {
				creditcards.add(new Creditcard(
							res.getString("card_number"),
							res.getString("expire"),
							res.getString("first_name"),
							res.getString("last_name")));
			}
			SQLObject.closeDatabase(conn);
		} catch (SQLException e) {
			throw new DatabaseException(e.toString());
		}
		return gson.toJson(new PaymentMethodsCombined(paypals, creditcards));
	}
	
	private String get_formatted_user_info(String email) throws DatabaseException, UserNotFoundException {
		Gson gson = new Gson();
		Account account = new Account();
		account.setEmail(email);
		Account.fill(account);
		Account.ViewableAccount va = new Account.ViewableAccount(
				account.getEmail(), 
				account.getFirst_name(), 
				account.getLast_name(), 
				account.getPhone(), 
				account.getLast_login()
				);
		return gson.toJson(va);
	}
}
