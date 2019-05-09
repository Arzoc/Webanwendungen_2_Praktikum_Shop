package exceptions;

public class InvalidTokenException extends Exception {

	private static final String default_error = "Invalid password";

	public InvalidTokenException(String error) {
		super(error);
	}
	
	public InvalidTokenException() {
		super(InvalidTokenException.default_error);
	}
	
}
