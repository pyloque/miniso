package miniso;

import java.io.IOException;
import java.io.StringWriter;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.HttpOutput;
import org.eclipse.jetty.server.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import miniso.error.TemplateError;

public class JettyResponse {
	private final static Logger LOG = LoggerFactory.getLogger(JettyResponse.class);

	private Response response;
	private JettyConfig config;

	public JettyResponse(Response response, JettyConfig config) {
		this.response = response;
		this.config = config;
	}

	public HttpServletResponse response() {
		return response;
	}

	public JettyResponse redirect(int code, String url) {
		try {
			response.sendRedirect(code, url);
		} catch (IOException e) {
			LOG.error("send redirect error", e);
		}
		return this;
	}

	public JettyResponse redirect(String url) {
		return redirect(301, url);
	}

	public JettyResponse sendError(int code, String msg) {
		try {
			response.sendError(code, msg);
		} catch (IOException e) {
			LOG.error("send error error", e);
		}
		return this;
	}

	public JettyResponse notFound(String msg) {
		return sendError(404, msg);
	}

	public JettyResponse badRequest(String msg) {
		return sendError(400, msg);
	}

	public JettyResponse internalServerError(String msg) {
		return sendError(500, msg);
	}

	public JettyResponse unAuthorizedError(String msg) {
		return sendError(401, msg);
	}

	public JettyResponse forbidden(String msg) {
		return sendError(403, msg);
	}

	public JettyResponse ok(String contentType, String body) {
		response.setContentLength(body.length());
		response.setCharacterEncoding("utf8");
		response.setContentType(contentType);
		HttpOutput output = response.getHttpOutput();
		try {
			output.print(body);
			output.flush();
		} catch (IOException e) {
			LOG.error("send ok error", e);
		}
		return this;
	}

	public JettyResponse html(String body) {
		return ok("text/html", body);
	}

	public JettyResponse json(Object o) {
		return ok("application/json", JSON.toJSONString(o));
	}

	public JettyResponse customize(Consumer<Response> consumer) {
		consumer.accept(response);
		return this;
	}

	public void template(String template, Object context) {
		try {
			StringWriter stringWriter = new StringWriter();
			Template tpl = freemarker().getTemplate(template);
			tpl.process(context, stringWriter);
			html(stringWriter.toString());
		} catch (IOException | TemplateException e) {
			LOG.error("render template {} error", template, e);
			throw new TemplateError(String.format("render template %s error", template), e);
		}
	}

	public void template(String template) {
		this.template(template, null);
	}

	private Configuration freemarker;

	private Configuration freemarker() {
		if (freemarker == null) {
			freemarker = new Configuration(Configuration.VERSION_2_3_23);
			freemarker.setClassForTemplateLoading(this.getClass(), config.getTemplateDir());
		}
		return freemarker;
	}

}
