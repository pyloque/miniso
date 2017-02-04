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

	public JettyConfig setHost(String host) {
		this.host = host;
		return this;
	}

	public int getPort() {
		return port;
	}

	public JettyConfig setPort(int port) {
		this.port = port;
		return this;
	}

	public int getAcceptors() {
		return acceptors;
	}

	public JettyConfig setAcceptors(int acceptors) {
		this.acceptors = acceptors;
		return this;
	}

	public int getSelectors() {
		return selectors;
	}

	public JettyConfig setSelectors(int selectors) {
		this.selectors = selectors;
		return this;
	}

	public int getWorkers() {
		return workers;
	}

	public JettyConfig setWorkers(int workers) {
		this.workers = workers;
		return this;
	}

	public String getStaticDir() {
		return staticDir;
	}

	public JettyConfig setStaticDir(String staticDir) {
		this.staticDir = staticDir;
		return this;
	}

	public String getStaticPrefix() {
		return staticPrefix;
	}

	public JettyConfig setStaticPrefix(String staticPrefix) {
		this.staticPrefix = staticPrefix;
		return this;
	}

	public String getTemplateDir() {
		return templateDir;
	}

	public JettyConfig setTemplateDir(String templateDir) {
		this.templateDir = templateDir;
		return this;
	}

	public boolean isAccurate() {
		return accurate;
	}

	public JettyConfig setAccurate(boolean accurate) {
		this.accurate = accurate;
		return this;
	}

}
