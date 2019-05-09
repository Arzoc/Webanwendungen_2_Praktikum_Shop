package exceptions;

public class InvalidPasswordException extends Exception {
	
	private static final String default_error = "Invalid password";

	public InvalidPasswordException(String error) {
		super(error);
	}
	
	public InvalidPasswordException() {
		super(InvalidPasswordException.default_error);
	}
	
}
