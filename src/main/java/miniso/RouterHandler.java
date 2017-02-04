package miniso;

@FunctionalInterface
public interface RouterHandler {

	public void accept(JettyRequest req, JettyResponse res);

}
