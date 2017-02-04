package miniso.error;

public class TemplateError extends JettyError {

	private static final long serialVersionUID = -112848834670376383L;

	public TemplateError(String message, Throwable e) {
		super(message, e);
	}

}
