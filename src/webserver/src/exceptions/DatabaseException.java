package exceptions;

public class DatabaseException extends Exception {

	private static final String default_error = "Database error";
	private static final long serialVersionUID = 0;

	public DatabaseException(String error) {
		super(error);
	}
	
	public DatabaseException() {
		super(DatabaseException.default_error);
	}
	
}
