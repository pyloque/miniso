package miniso.error;

public class ResponseError extends JettyError {

	private static final long serialVersionUID = -1860982752982441666L;

	private int code;
	private String body;

	public ResponseError(int code, String body) {
		super("");
		this.code = code;
		this.body = body;
	}

	public int getCode() {
		return code;
	}

	public String getBody() {
		return body;
	}

}
