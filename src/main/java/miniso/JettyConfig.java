package miniso;

public class JettyConfig {

	private String host = "0.0.0.0";
	private int port = 8080;
	private int acceptors = 1;
	private int selectors = 2;
	private int workers = 10;
	private String staticDir = "/static";
	private String staticPrefix = "/static";
	private String templateDir = "/pages";
	private boolean accurate = false;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getAcceptors() {
		return acceptors;
	}

	public void setAcceptors(int acceptors) {
		this.acceptors = acceptors;
	}

	public int getSelectors() {
		return selectors;
	}

	public void setSelectors(int selectors) {
		this.selectors = selectors;
	}

	public int getWorkers() {
		return workers;
	}

	public void setWorkers(int workers) {
		this.workers = workers;
	}

	public String getStaticDir() {
		return staticDir;
	}

	public void setStaticDir(String staticDir) {
		this.staticDir = staticDir;
	}

	public String getStaticPrefix() {
		return staticPrefix;
	}

	public void setStaticPrefix(String staticPrefix) {
		this.staticPrefix = staticPrefix;
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public void setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
	}

	public boolean isAccurate() {
		return accurate;
	}

	public void setAccurate(boolean accurate) {
		this.accurate = accurate;
	}

}
