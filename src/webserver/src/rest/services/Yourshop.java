package rest.services;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.glassfish.jersey.server.mvc.Viewable;

import java.sql.Connection;
import java.sql.DriverManager;

@Path("/Yourshop")
public class Yourshop {

	private Connection connection = null;
	private final Logger logger = LogManager.getLogger(Yourshop.class);
	private String db_filename = "yourshop.db";
	
	public Yourshop() {
		super();
		System.out.println("\n\n\n\nconst\n\n\n\n");
		this.logger.error("here");
	}
	
	private Connection getSQLConnection() throws Exception {
		Connection conn = DriverManager.getConnection("jdbs:sqlite:" + this.db_filename);
		return conn;
	}
	
	@GET
	@Produces(MediaType.TEXT_HTML)
	@Path("index")
	public Response getIndex() {
		System.out.println("\n\nhere\n\n");
		if (this.connection == null) {
			try {
				this.connection = this.getSQLConnection();
			} catch (Exception e) {
				this.logger.error("Failed to get sql connection: " + e.toString());
				return Response
						.status(Response.Status.INTERNAL_SERVER_ERROR)
						.entity(new Viewable("/internal_server_error.html"))
						.build();
			}
		}
		return Response
				.status(Response.Status.OK)
				.entity(new Viewable("/index.html"))
				.build();
	}
	
}
