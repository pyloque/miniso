package miniso.error;

public class NotFound extends ResponseError {

	private static final long serialVersionUID = 2050639224609984123L;

	public NotFound(String body) {
		super(404, body);
	}

}
