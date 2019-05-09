package exceptions;

public class DatabaseException extends Exception {

	private static final String default_error = "Database error";

	public DatabaseException(String error) {
		super(error);
	}
	
	public DatabaseException() {
		super(DatabaseException.default_error);
	}
	
}
