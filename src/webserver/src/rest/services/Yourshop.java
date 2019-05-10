package rest.services;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.UriInfo;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import exceptions.DatabaseException;
import exceptions.InvalidTokenException;
import exceptions.InvalidTransactionParametersException;
import other.CheckoutParameters;
import other.FullOrder;
import other.JwtManager;

@Path("/")
public class Yourshop {

	@Context
	UriInfo uriInfo;
	
	public Yourshop() {
		super();
	}
	
	@POST
	@Path("/checkout")
	public Response checkout(
			@Context HttpHeaders httpHeaders, 
			@FormParam("checkout_params") String params) {
		Gson gson = new Gson();
		JwtManager jwt = JwtManager.getInstance();
		String authHeaders = httpHeaders.getHeaderString(HttpHeaders.AUTHORIZATION);
		try {
			String token = jwt.validateToken(authHeaders);
			String email = jwt.getEmail(token);
			CheckoutParameters c = gson.fromJson(params, CheckoutParameters.class);
			FullOrder.insert_transaction(c, email);
			return Response.status(Response.Status.OK).build();
		} catch (InvalidTokenException e) {
			return Response.status(Response.Status.UNAUTHORIZED).build();
		} catch (JsonSyntaxException | InvalidTransactionParametersException e ) {
			return Response.status(Response.Status.BAD_REQUEST).build();
		} catch (DatabaseException e) {
			return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}
	}
	
	
}
