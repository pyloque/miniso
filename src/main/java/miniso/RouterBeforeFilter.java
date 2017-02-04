package miniso;

@FunctionalInterface
public interface RouterBeforeFilter {
	public boolean accept(JettyRequest req, JettyResponse res);
}
