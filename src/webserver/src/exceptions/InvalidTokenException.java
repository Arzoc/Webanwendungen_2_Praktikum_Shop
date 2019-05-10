package exceptions;

public class InvalidTokenException extends Exception {

	private static final String default_error = "Invalid password";
	private static final long serialVersionUID = 0;

	public InvalidTokenException(String error) {
		super(error);
	}
	
	public InvalidTokenException() {
		super(InvalidTokenException.default_error);
	}
	
}
