package exceptions;

public class UserNotFoundException extends Exception {
	
	private static final String default_error = "User not found";

	public UserNotFoundException(String error) {
		super(error);
	}
	
	public UserNotFoundException() {
		super(UserNotFoundException.default_error);
	}
	
}
