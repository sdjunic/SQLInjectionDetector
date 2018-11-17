package main.exception;

public class SQLInjection extends Exception {

	public String message = null;
	
	public SQLInjection(String message) {
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message;
	}
	
}
