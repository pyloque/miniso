package miniso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Consumer;

import miniso.error.RouterError;

public class JettyRouter {
	private Map<String, Map<String, RouterHandler>> allRouters = new HashMap<>();
	private List<RouterBeforeFilter> allGlobalBeforeFilters = new ArrayList<>();
	private Map<String, List<RouterBeforeFilter>> allBeforeFilters = new HashMap<>();
	private List<RouterAfterFilter> allGlobalAfterFilters = new ArrayList<>();
	private Map<String, List<RouterAfterFilter>> allAfterFilters = new HashMap<>();

	public void handle(String path, JettyConfig config, JettyRequest request, JettyResponse response) {
		RouterHandler router = getRouter(path, request.getMethod());
		if (router == null && !config.isAccurate()) {
			path = switchSlash(path);
			router = getRouter(path, request.getMethod());
		}
		if (router != null) {
			request.setHandled(true);
			if (invokeGlobalBeforeFilters(request, response) && invokeBeforeFilters(path, config, request, response)) {
				router.accept(request, response);
				if (invokeGlobalAfterFilters(request, response)) {
					invokeAfterFilters(path, config, request, response);
				}
			}
		}
	}

	private String switchSlash(String path) {
		if (path.endsWith("/")) {
			path = path.substring(0, path.length() - 1);
		} else {
			path = path + "/";
		}
		return path;
	}

	private boolean invokeGlobalBeforeFilters(JettyRequest req, JettyResponse res) {
		for (RouterBeforeFilter filter : allGlobalBeforeFilters) {
			if (!filter.accept(req, res)) {
				return false;
			}
		}
		return true;
	}

	private boolean invokeBeforeFilters(String path, JettyConfig config, JettyRequest req, JettyResponse res) {
		List<RouterBeforeFilter> filters = allBeforeFilters.get(path);
		if (filters == null && !config.isAccurate()) {
			path = switchSlash(path);
			filters = allBeforeFilters.getOrDefault(path, Collections.emptyList());
		}
		for (RouterBeforeFilter filter : filters) {
			if (!filter.accept(req, res)) {
				return false;
			}
		}
		return true;
	}

	private boolean invokeGlobalAfterFilters(JettyRequest req, JettyResponse res) {
		for (RouterAfterFilter filter : allGlobalAfterFilters) {
			if (!filter.accept(req, res)) {
				return false;
			}
		}
		return true;
	}

	private boolean invokeAfterFilters(String path, JettyConfig config, JettyRequest req, JettyResponse res) {
		List<RouterAfterFilter> filters = allAfterFilters.get(path);
		if (filters == null && !config.isAccurate()) {
			path = switchSlash(path);
			filters = allAfterFilters.getOrDefault(path, Collections.emptyList());
		}
		for (RouterAfterFilter filter : filters) {
			if (!filter.accept(req, res)) {
				return false;
			}
		}
		return true;
	}

	private RouterHandler getRouter(String path, String method) {
		Map<String, RouterHandler> routers = allRouters.get(path);
		if (routers == null) {
			return null;
		}
		return routers.get(method);
	}

	public JettyRouter before(RouterBeforeFilter filter) {
		allGlobalBeforeFilters.add(filter);
		return this;
	}

	public JettyRouter before(RouterAfterFilter filter) {
		allGlobalAfterFilters.add(filter);
		return this;
	}

	public JettyRouter before(String path, RouterBeforeFilter filter) {
		List<RouterBeforeFilter> filters = allBeforeFilters.get(path);
		if (filters == null) {
			filters = new ArrayList<>();
			allBeforeFilters.put(path, filters);
		}
		filters.add(filter);
		return this;
	}

	public JettyRouter after(String path, RouterAfterFilter filter) {
		List<RouterAfterFilter> filters = allAfterFilters.get(path);
		if (filters == null) {
			filters = new ArrayList<>();
			allAfterFilters.put(path, filters);
		}
		filters.add(filter);
		return this;
	}

	public JettyRouter get(String path, RouterHandler router) {
		return this.route(path, "GET", router);
	}

	public JettyRouter post(String path, RouterHandler router) {
		return this.route(path, "POST", router);
	}

	public JettyRouter put(String path, RouterHandler router) {
		return this.route(path, "PUT", router);
	}

	public JettyRouter delete(String path, RouterHandler router) {
		return this.route(path, "DELETE", router);
	}

	public JettyRouter route(String path, RouterHandler router) {
		for (String method : new String[] { "GET", "POST", "PUT", "DELETE" }) {
			route(path, method, router);
		}
		return this;
	}

	public JettyRouter route(String path, String method, RouterHandler router) {
		Map<String, RouterHandler> routers = allRouters.get(path);
		if (routers == null) {
			routers = new HashMap<>();
			allRouters.put(path, routers);
		}
		if (routers.put(method, router) != null) {
			throw new RouterError(String.format("path=%s method=%s is duplicated", path, method));
		}
		return this;
	}

	public JettyRouter child(String path, Consumer<JettyRouter> consumer) {
		JettyRouter subRouter = new JettyRouter();
		consumer.accept(subRouter);
		Map<String, Map<String, RouterHandler>> otherRouters = subRouter.allRouters;
		for (String subPath : otherRouters.keySet()) {
			Map<String, RouterHandler> routers = otherRouters.get(subPath);
			for (Entry<String, RouterHandler> entry : routers.entrySet()) {
				this.route(path + subPath, entry.getKey(), entry.getValue());
			}
		}
		Map<String, List<RouterBeforeFilter>> otherBeforeFilters = subRouter.allBeforeFilters;
		for (String subPath : otherBeforeFilters.keySet()) {
			List<RouterBeforeFilter> filters = otherBeforeFilters.get(subPath);
			for (RouterBeforeFilter filter : filters) {
				this.before(path + subPath, filter);
			}
		}
		Map<String, List<RouterAfterFilter>> otherAfterFilters = subRouter.allAfterFilters;
		for (String subPath : otherAfterFilters.keySet()) {
			List<RouterAfterFilter> filters = otherAfterFilters.get(subPath);
			for (RouterAfterFilter filter : filters) {
				this.after(path + subPath, filter);
			}
		}
		return this;
	}

}