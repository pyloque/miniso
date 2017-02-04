package miniso.error;

public class Forbidden extends ResponseError {

	private static final long serialVersionUID = -3664075485301752529L;

	public Forbidden(String body) {
		super(403, body);
	}

}
