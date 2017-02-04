package miniso.error;

public class JettyError extends RuntimeException {

	private static final long serialVersionUID = -4459134787736871545L;

	public JettyError(String message) {
		super(message);
	}
	
	public JettyError(String message, Throwable e) {
		super(message, e);
	}
}
