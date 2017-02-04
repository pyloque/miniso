#Miniso
Cheap But Works!


## Hello World
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

## Dependencies
1. Jetty
2. Freemarker

## Config
```java
JettyConfig config = new JettyConfig();
config.setHost("localhost").setPort(8080)
	  .setAcceptors(1)  // accept connection threads
	  .setSelectors(2)  // read write connection threads
	  .setWorkers(10)  // handle http request threads
	  .setStaticDir("/static") // static resource directory in classpath
	  .setStaticPrefix("/static")  // static resource url prefix
	  .setTemplateDir("/pages");  // freemarker template directory in classpath
```

## Route
```java
JettyServer server = new JettyServer();
server.route(router -> {
	router.get("/hello", (req, res) -> {});
	router.post("/hello", (req, res) -> {});
	router.child("/math", math -> {  // sub modules
		math.get("/add", (req, res) -> {});
		math.get("/sub", (req, res) -> {});
		math.get("/mul", (req, res) -> {});
		math.get("/div", (req, res) -> {});
	});
	Company company = new Company();
	router.child("/company", company::route) // sub modules using class
});
class Company {
	public void hire(Request req, Response res) {}
	public void dismiss(Request req, Response res) {}

	public void route(JettyRouter router) {
		router.get("/hire", this::hire);
		router.get("/dismiss", this::dismiss);
	}
}
```
