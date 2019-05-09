package rest.services;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.KeyGenerator;
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

import exceptions.DatabaseException;
import exceptions.InvalidPasswordException;
import exceptions.InvalidTokenException;
import exceptions.UserAlreadyExistsException;
import exceptions.UserNotFoundException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import model.Account;

@Path("/users")
public class Users {

	@Context
	UriInfo uriInfo;
	
	private KeyGenerator keyGen;
	
	@POST
	@Path("/login")
	@Consumes(MediaType.APPLICATION_FORM_URLENCODED)
	@Produces(MediaType.TEXT_HTML)
	public Response login(@FormParam("email") String email, @FormParam("password") String password) {
		Account a = new Account();
		a.setEmail(email.trim());
		try { 
			a = Account.fill(a);
			String passwordHash = Account.generatePasswordHash(password);
			a.checkPassword(passwordHash);
			String token = this.issueToken(a.getEmail());
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
			@FormParam("password") String password,
			@Context HttpHeaders headers
			) {
		String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		if (authHeader != null || !authHeader.trim().equals(""))
			return Response.status(Response.Status.OK).entity("{ return: 2; msg: already token supplied; }").build();
		Account account = new Account();
		try {
			Account.insertNew(account);
			return Response.status(Response.Status.OK).entity("{ return: 0 }").build();
		} catch (UserAlreadyExistsException e) {
			return Response.status(Response.Status.OK).entity("{ return: 1; msg: UserAlreadyExists; }").build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	@GET
	@Path("/order_history")
	@Produces(MediaType.APPLICATION_JSON)
	public Response order_history(@Context HttpHeaders headers) {
		String authHeader = headers.getHeaderString(HttpHeaders.AUTHORIZATION);
		try {
			this.validateToken(authHeader);
			//TODO
			return Response.status(Response.Status.NOT_IMPLEMENTED).build();
		} catch (InvalidTokenException e) {
			Response.status(Response.Status.UNAUTHORIZED).build();
		}
		return Response.status(Response.Status.OK).build();
	}
	
	/* on invalid type value, return `now`, which invalidates token */
	private static Date getTimeInFuture(long var, int type) {
		switch (type) {
		case 0: /* seconds */
			return new Date(Calendar.getInstance().getTimeInMillis() + (var * 1000));
		case 1: /* minutes */
			return new Date(Calendar.getInstance().getTimeInMillis() + (var * 60000));
		case 2: /* hours */
			return new Date(Calendar.getInstance().getTimeInMillis() + (var * 3600000));
		default:
			return new Date();
		}
	}

	private String issueToken(String email) {
		Key key = this.keyGen.generateKey();
		return Jwts.builder()
				.setSubject(email)
				.setIssuer(uriInfo.getAbsolutePath().toString())
				.setIssuedAt(new Date())
				.setExpiration(Users.getTimeInFuture(30, 1))
				.signWith(SignatureAlgorithm.HS512, key)
				.compact();
	}
	
	private void validateToken(String authHeader) throws InvalidTokenException {
		String token = authHeader.substring("Bearer".length()).trim();
		try {
			Key key = this.keyGen.generateKey();
			Jwts.parser().setSigningKey(key).parseClaimsJws(token);
		} catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException | IllegalArgumentException e) {
			throw new InvalidTokenException();
		}
	}
	
}
