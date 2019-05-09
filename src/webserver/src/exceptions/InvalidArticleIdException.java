package exceptions;

public class InvalidArticleIdException extends Exception {


	private static final String default_error = "Database error";

	public InvalidArticleIdException(String error) {
		super(error);
	}
	
	public InvalidArticleIdException() {
		super(InvalidArticleIdException.default_error);
	}
	
}
