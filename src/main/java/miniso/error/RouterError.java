package miniso.error;

public class RouterError extends JettyError {

	private static final long serialVersionUID = -6015918366956625291L;

	public RouterError(String message) {
		super(message);
	}

	public RouterError(String message, Throwable e) {
		super(message, e);
	}

}
