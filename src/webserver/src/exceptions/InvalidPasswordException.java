package exceptions;

public class InvalidPasswordException extends Exception {
	
	private static final String default_error = "Invalid password";
	private static final long serialVersionUID = 0;

	public InvalidPasswordException(String error) {
		super(error);
	}
	
	public InvalidPasswordException() {
		super(InvalidPasswordException.default_error);
	}
	
}
