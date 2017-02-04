package miniso;

@FunctionalInterface
public interface RouterAfterFilter {
	public boolean accept(JettyRequest req, JettyResponse res);
}
