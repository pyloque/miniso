package miniso;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.handler.AbstractHandler;

import miniso.error.NotFound;

public class JettyHandler extends AbstractHandler {
	private JettyRouter router;
	private JettyConfig config;
	private JettyErrorHandler errorHandler;

	public JettyHandler(JettyRouter router, JettyErrorHandler errorHandler, JettyConfig config) {
		this.router = router;
		this.errorHandler = errorHandler;
		this.config = config;
	}

	@Override
	public void handle(String path, Request baseRequest, HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		JettyRequest req = new JettyRequest(baseRequest);
		JettyResponse res = new JettyResponse((Response) response, config);
		try {
			router.handle(path, config, req, res);
		} catch (Exception exc) {
			errorHandler.handle(req, res, exc);
			return;
		}
		if (!req.isHandled()) {
			errorHandler.handle(req, res, new NotFound(String.format("no route for path %s", path)));
		}
	}

}
