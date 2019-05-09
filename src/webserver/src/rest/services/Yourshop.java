package rest.services;

import javax.crypto.KeyGenerator;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.ws.rs.core.UriInfo;

import exceptions.InvalidPasswordException;
import exceptions.UserNotFoundException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import model.Account;

import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;

@Path("/")
public class Yourshop {

	@Context
	UriInfo uriInfo;
	
	public Yourshop() {
		super();
	}
	
	
	
	
}
