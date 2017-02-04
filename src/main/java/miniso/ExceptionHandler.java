package miniso;

@FunctionalInterface
public interface ExceptionHandler {

	public void accept(JettyRequest req, JettyResponse res, Exception exc);

}
