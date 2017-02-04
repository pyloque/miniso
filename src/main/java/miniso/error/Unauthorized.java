package miniso.error;

public class Unauthorized extends ResponseError {

	private static final long serialVersionUID = 3528799110076672484L;

	public Unauthorized(String body) {
		super(401, body);
	}

}
