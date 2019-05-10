package exceptions;

public class InvalidArticleIdException extends Exception {


	private static final String default_error = "InvalidArticleIdException";
	private static final long serialVersionUID = 0;

	public InvalidArticleIdException(String error) {
		super(error);
	}
	
	public InvalidArticleIdException() {
		super(InvalidArticleIdException.default_error);
	}
	
}
