package miniso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.jetty.server.Request;

public class JettyRequest {
	private Request req;

	public JettyRequest(Request req) {
		this.req = req;
	}

	public Request req() {
		return req;
	}

	public String getMethod() {
		return req.getMethod();
	}

	public String string(String name) {
		return crack(name).string();
	}

	public String string(String name, String defaultValue) {
		return crack(name).string(defaultValue);
	}

	public int int32(String name) {
		return crack(name).int32();
	}

	public int int32(String name, int defaultValue) {
		return crack(name).int32(defaultValue);
	}

	public long int64(String name) {
		return crack(name).int64();
	}

	public long int64(String name, long defaultValue) {
		return crack(name).int64(defaultValue);
	}

	public boolean bool(String name) {
		return crack(name).bool();
	}

	public boolean bool(String name, boolean defaultValue) {
		return crack(name).bool(defaultValue);
	}

	public List<String> strings(String name) {
		return crack(name).strings();
	}

	public List<Integer> int32s(String name) {
		return crack(name).int32s();
	}

	public List<Long> int64s(String name) {
		return crack(name).int64s();
	}

	public List<Boolean> bools(String name) {
		return crack(name).bools();
	}

	public ParamCracker crack(String name) {
		String[] values = req.getParameterValues(name);
		List<String> result = null;
		if (values == null) {
			result = Collections.emptyList();
		} else {
			result = new ArrayList<>(values.length);
			for (String value : values) {
				result.add(value);
			}
		}
		return new ParamCracker(name, result);
	}

	public void setHandled(boolean handled) {
		req.setHandled(handled);
	}
	
	public boolean isHandled() {
		return req.isHandled();
	}

}
