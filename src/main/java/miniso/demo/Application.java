package miniso.demo;

import java.util.HashMap;
import java.util.Map;

import miniso.JettyConfig;
import miniso.JettyRequest;
import miniso.JettyResponse;
import miniso.JettyRouter;
import miniso.JettyServer;
import miniso.error.NotFound;
import miniso.error.Redirect;

public class Application {

	public static class HelloApi {

		public void say(JettyRequest req, JettyResponse res) {
			String xyz = req.string("xyz", "world");
			Map<String, Object> context = new HashMap<>();
			context.put("xyz", xyz);
			res.json(context);
		}

		public void error(JettyRequest req, JettyResponse res) {
			throw new NotFound("whatever not found");
		}

		public JettyRouter route(JettyRouter router) {
			router.get("/say", this::say);
			router.get("/404", this::error);
			return router;
		}

	}

	public static class HelloUI {

		public void say(JettyRequest req, JettyResponse res) {
			String xyz = req.string("xyz", "world");
			Map<String, Object> context = new HashMap<>();
			context.put("xyz", xyz);
			res.template("say.html", context);
		}

		public void baidu(JettyRequest req, JettyResponse res) {
			throw new Redirect("http://www.baidu.com");
		}

		public JettyRouter route(JettyRouter router) {
			router.get("/say/ui", this::say);
			router.get("/baidu", this::baidu);
			return router;
		}

	}

	public static void main(String[] args) {
		JettyConfig config = new JettyConfig();
		JettyServer server = new JettyServer(config);
		HelloApi helloApi = new HelloApi();
		HelloUI helloUi = new HelloUI();
		server.route(router -> {
			router.child("/hello", helloApi::route).child("/hello", helloUi::route);
			router.get("/", (req, res) -> {
				res.html("home");
			});
		}).error(NotFound.class, (req, res, exc) -> {
			res.template("404.html");
		});
		server.start();
	}

}
