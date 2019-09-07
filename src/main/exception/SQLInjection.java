package main.exception;

public class SQLInjection extends Exception {

	private static final long serialVersionUID = 1L;
	
	public String message = null;
	
	public SQLInjection(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}
