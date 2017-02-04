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
		connector.setPort(8080);
		server.addConnector(connector);
		this.router = new JettyRouter();
		this.handlers = new HandlerList();
		if (config.getStaticDir() != null) {
			ResourceHandler staticHandler = new ResourceHandler();
			staticHandler.setBaseResource(Resource.newClassPathResource(config.getStaticDir()));
			staticHandler.setDirectoriesListed(false);
			ContextHandler ctxHandler = new ContextHandler(config.getStaticPrefix());
			ctxHandler.setHandler(staticHandler);
			handlers.addHandler(ctxHandler);
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
