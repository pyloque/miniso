package miniso.error;

public class BadRequest extends ResponseError {

	private static final long serialVersionUID = 4321231208911229858L;

	public BadRequest(String body) {
		super(400, body);
	}

}
