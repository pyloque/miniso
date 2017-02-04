#Miniso
Based On Jetty! Cheap But Works!

```java
import miniso.JettyConfig;
import miniso.JettyServer;

public class Application {

	public static void main(String[] args) {
		JettyConfig config = new JettyConfig();
		config.setPort(8080);
		JettyServer server = new JettyServer(config);
		server.route(router -> {
			router.get("/hello", (req, res) -> {
				res.html("hello, world!");
			});
		});
		server.start();
	}

}
```