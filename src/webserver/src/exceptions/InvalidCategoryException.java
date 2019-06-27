package exceptions;

public class InvalidCategoryException extends Exception {

	private static final String default_error = "InvalidArticleIdException";
	private static final long serialVersionUID = 0;

	public InvalidCategoryException(String error) {
		super(error);
	}
	
	public InvalidCategoryException() {
		super(InvalidCategoryException.default_error);
	}
	
}
