package miniso.error;

public class Redirect extends ResponseError {

	private static final long serialVersionUID = 8508886373092315414L;
	private String location;

	public Redirect(int code, String location) {
		super(code, "");
		this.location = location;
	}

	public Redirect(String location) {
		this(301, location);
	}

	public String getLocation() {
		return location;
	}

}
