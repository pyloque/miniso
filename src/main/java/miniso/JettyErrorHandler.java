package miniso;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import miniso.error.Redirect;
import miniso.error.ResponseError;

public class JettyErrorHandler {
	private final static Logger LOG = LoggerFactory.getLogger(JettyErrorHandler.class);

	private Map<Class<?>, ExceptionHandler> handlers = new HashMap<>();

	public JettyErrorHandler error(Class<?> clazz, ExceptionHandler handler) {
		this.handlers.put(clazz, handler);
		return this;
	}

	public void handle(JettyRequest req, JettyResponse res, Exception exc) {
		ExceptionHandler handler = getHandler(exc.getClass());
		if (handler == null) {
			this.handleDefault(req, res, exc);
		} else {
			handler.accept(req, res, exc);
		}
	}

	public ExceptionHandler getHandler(Class<?> clazz) {
		ExceptionHandler handler = handlers.get(clazz);
		if (handler != null) {
			return handler;
		}
		if (clazz == Object.class) {
			return null;
		}
		return getHandler(clazz.getSuperclass());
	}

	public void handleDefault(JettyRequest req, JettyResponse res, Exception exc) {
		if (exc instanceof Redirect) {
			Redirect redirect = (Redirect) exc;
			res.redirect(redirect.getCode(), redirect.getLocation());
			return;
		}
		if (exc instanceof ResponseError) {
			ResponseError error = (ResponseError) exc;
			res.sendError(error.getCode(), error.getBody());
			return;
		}
		LOG.error("internal server error", exc);
		res.internalServerError("Internal Server Error:" + exc.getMessage());
	}

}
