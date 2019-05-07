package rest.services;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.glassfish.jersey.server.mvc.Viewable;

import java.net.URI;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@Path("/")
public class Yourshop {

	private Connection connection = null;
	private String rootpath = "/Yourshop";
	private String db_filename = "C:\\Users\\Arzoc\\Desktop\\uni\\webanwendungen2\\git\\Webanwendungen_2_Praktikum_Shop\\src\\database\\yourshop.db";
	
	public Yourshop() {
		super();
	}
	
	private Connection getSQLConnection() throws Exception {
		Class.forName("org.sqlite.JDBC");
		Connection conn = DriverManager.getConnection("jdbc:sqlite:" + this.db_filename);
		return conn;
	}
	
	@POST
	@Path("/login")
	@Produces(MediaType.APPLICATION_JSON)
	public Response login(@FormParam("email") String email, @FormParam("password") String passwordHash) {
		if (this.connection == null) {
			try {
				this.connection = this.getSQLConnection();
			} catch (Exception e) {
				System.out.println(e.toString());
				return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
			}
		}
		PreparedStatement prepStatement = this.connection.prepareStatement("select ")
	}
	
}
