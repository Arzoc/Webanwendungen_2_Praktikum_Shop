package exceptions;

public class InvalidTransactionParametersException extends Exception {

	private static final String default_error = "InvalidTransactionParametersException";
	private static final long serialVersionUID = 0;

	public InvalidTransactionParametersException(String error) {
		super(error);
	}
	
	public InvalidTransactionParametersException() {
		super(InvalidTransactionParametersException.default_error);
	}
	
}
