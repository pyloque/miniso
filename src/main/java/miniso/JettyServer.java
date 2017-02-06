package miniso;

import java.util.function.Consumer;

import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.NetworkTrafficServerConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.ContextHandler;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.util.resource.Resource;
import org.eclipse.jetty.util.thread.QueuedThreadPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import miniso.error.JettyError;

public class JettyServer {
	private final static Logger LOG = LoggerFactory.getLogger(JettyServer.class);

	private Server server;
	private HandlerList handlers;
	private JettyRouter router;
	private JettyErrorHandler errorHandler;

	public JettyServer(JettyConfig config) {
		server = new Server(new QueuedThreadPool(config.getWorkers()));
		ServerConnector connector = new NetworkTrafficServerConnector(server, null, null, null, config.getAcceptors(),
				config.getSelectors(), new HttpConnectionFactory());
		connector.setPort(config.getPort());
		server.addConnector(connector);
		this.router = new JettyRouter();
		this.handlers = new HandlerList();
		if (config.getClasspathStaticDir() != null) {
			ResourceHandler classpathResourceHandler = new ResourceHandler();
			classpathResourceHandler.setBaseResource(Resource.newClassPathResource(config.getClasspathStaticDir()));
			classpathResourceHandler.setDirectoriesListed(false);
			ContextHandler staticHandler = new ContextHandler(config.getStaticPrefix());
			staticHandler.setHandler(classpathResourceHandler);
			handlers.addHandler(staticHandler);
		}
		if (config.getExternalStaticDir() != null) {
			ResourceHandler externalResourceHandler = new ResourceHandler();
			externalResourceHandler.setResourceBase(config.getExternalStaticDir());
			externalResourceHandler.setDirectoriesListed(false);
			ContextHandler staticHandler = new ContextHandler(config.getStaticPrefix());
			staticHandler.setHandler(externalResourceHandler);
			handlers.addHandler(staticHandler);
		}
		this.errorHandler = new JettyErrorHandler();
		this.handlers.addHandler(new JettyHandler(router, errorHandler, config));
	}

	public JettyServer() {
		this(new JettyConfig());
	}

	public JettyServer route(Consumer<JettyRouter> consumer) {
		consumer.accept(router);
		return this;
	}

	public JettyServer error(Class<?> clazz, ExceptionHandler handler) {
		errorHandler.error(clazz, handler);
		return this;
	}

	public void start() {
		server.setHandler(handlers);
		try {
			server.start();
		} catch (Exception e) {
			LOG.error("start jetty server error", e);
			throw new JettyError("start jetty server error", e);
		}
	}

}
