package miniso.error;

public class ParamError extends JettyError {

	private static final long serialVersionUID = -3372698551994910001L;

	public ParamError(String message) {
		super(message);
	}
	
	public ParamError(String message, Throwable e) {
		super(message, e);
	}

}
