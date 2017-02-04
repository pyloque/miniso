#Miniso
Cheap But Works! Maybe the Simplest Web Framework Of Java Universe !


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
1. jetty
2. freemarker
3. fastjson

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

## Error Handling
```java
JettyServer server = new JettyServer();
server.route(router-> {})
      .error(NotFound.class, (req, res, exc) -> {
	      res.template("404.html");
      })
      .error(BadRequest.class, (req, res, exc) -> {
          res.template("400.html");
	  });
```

## Output Style
```java
JettyServer server = new JettyServer();
server.route(router -> {
    router.get("/hello/json", (req, res) -> {
        Map<String, Object> context = new HashMap<>();
        context.put("hello", "world");
        res.json(context);
    });
    router.get("/hello/tpl", (req, res) -> {
        Map<String, Object> context = new HashMap<>();
        context.put("hello", "world");
        res.template("hello.ftl", context);
    });
    router.get("/hello/text", (req, res) -> {
        res.html("hello, world!");
    });
});
```

## Request Filter Ordering
```java
JettyServer server = new JettyServer();
server.route(router -> {})
	  .before((req, res) -> {}) // global before filter
	  .after((req, res) -> {})  // global after filter
	  .before(path, (req, res) -> {})  // before filter of specified path
	  .after(path, (req, res) -> {}) // after filter of specified path

# if any filter return false, subsequently filters and route handle will not be executed
```
1. global before filters
2. before filters of specified path
3. route handle of specified path
4. global after filters
5. after filters of specified path

## Request Parameter Checking
```java
JettyServer server = new JettyServer();
server.route(router -> {
	router.get("/hello", (req, res) -> {
		int a = req.int32("a");
		long b = req.int64("b", 1L);
		boolean c = req.bool("c");
		String d = req.string("d");
		List<Integer> e = req.int32s("e");
		int f = req.validator("f").checkInt32(value -> {
			return value >= 0;
		});
		List<String> g = req.validator("g").checkStrings(value -> {
			return value.length > 10;
		});
		// illegals will throw ParamError
	});
});
```

## Notice
1. https not supported yet!
2. restful style url matching not supported!
3. too simple, too naive!
