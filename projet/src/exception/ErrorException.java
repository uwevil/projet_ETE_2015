package exception;

public class ErrorException extends Exception {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ErrorException(String message)
	{
		System.err.println(message);
	}
}
