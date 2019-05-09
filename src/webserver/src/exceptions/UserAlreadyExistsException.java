package exceptions;

public class UserAlreadyExistsException extends Exception {

	private static final String default_error = "User already exists";

	public UserAlreadyExistsException(String error) {
		super(error);
	}
	
	public UserAlreadyExistsException() {
		super(UserAlreadyExistsException.default_error);
	}
	
}
