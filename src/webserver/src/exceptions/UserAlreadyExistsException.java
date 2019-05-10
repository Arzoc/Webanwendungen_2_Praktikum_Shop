package exceptions;

public class UserAlreadyExistsException extends Exception {

	private static final String default_error = "User already exists";
	private static final long serialVersionUID = 0;

	public UserAlreadyExistsException(String error) {
		super(error);
	}
	
	public UserAlreadyExistsException() {
		super(UserAlreadyExistsException.default_error);
	}
	
}
