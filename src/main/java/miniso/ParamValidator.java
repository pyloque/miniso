package miniso;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import miniso.error.ParamError;

public class ParamValidator {

	private String name;
	private List<String> values;

	public ParamValidator(String name, List<String> values) {
		this.name = name;
		this.values = values;
	}

	public ParamValidator size(int size) {
		assertIf(values.size() == size, String.format("param %s size illegal", name));
		return this;
	}

	public ParamValidator one() {
		return size(1);
	}

	public ParamValidator check(Function<String, Boolean> func, String msg) {
		for (String value : values) {
			if (!func.apply(value)) {
				if (msg == null) {
					msg = String.format("param %s with value=%s check error", name, value);
				}
				throw new ParamError(msg);
			}
		}
		return this;
	}

	public ParamValidator check(Function<String, Boolean> func) {
		return check(func, null);
	}

	public String string() {
		if (values.isEmpty()) {
			return null;
		}
		return values.get(0);
	}

	public String string(String defaultValue) {
		String value = string();
		if (value == null) {
			return defaultValue;
		}
		return value;
	}
	
	public String checkString(Function<String, Boolean> func, String msg) {
		String value = string();
		if (!func.apply(value)) {
			if (msg == null) {
				msg = String.format("param %s with value=%s check string error", name, value);
			}
			throw new ParamError(msg);
		}
		return value;
	}

	public int int32() {
		String value = string();
		if (value == null) {
			throw new ParamError(String.format("%s should not be empty", name));
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ParamError(String.format("%s should be integer", name));
		}
	}

	public int checkInt32(Function<Integer, Boolean> func, String msg) {
		int value = int32();
		if (!func.apply(value)) {
			if (msg == null) {
				msg = String.format("param %s with value=%s check int32 error", name, value);
			}
			throw new ParamError(msg);
		}
		return value;
	}

	public int checkInt32(Function<Integer, Boolean> func) {
		return checkInt32(func, null);
	}

	public int int32(int defaultValue) {
		String value = string();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Integer.parseInt(value);
		} catch (NumberFormatException e) {
			throw new ParamError(String.format("%s should be integer", name));
		}
	}

	public List<Integer> checkInt32s(Function<Integer, Boolean> func, String msg) {
		List<Integer> values = int32s();
		for (int value : values) {
			if (!func.apply(value)) {
				if (msg == null) {
					msg = String.format("param %s with value=%s check int32s error", name, value);
				}
				throw new ParamError(msg);
			}
		}
		return values;
	}

	public List<Integer> checkInt32s(Function<Integer, Boolean> func) {
		return checkInt32s(func);
	}

	public long int64() {
		String value = string();
		if (value == null) {
			throw new ParamError(String.format("%s should not be empty", name));
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new ParamError(String.format("%s should be long integer", name));
		}
	}

	public long checkInt64(Function<Long, Boolean> func, String msg) {
		long value = int64();
		if (!func.apply(value)) {
			if (msg == null) {
				msg = String.format("param %s with value=%s check int32 error", name, value);
			}
			throw new ParamError(msg);
		}
		return value;
	}

	public long checkInt64(Function<Long, Boolean> func) {
		return checkInt64(func, null);
	}

	public long int64(long defaultValue) {
		String value = string();
		if (value == null) {
			return defaultValue;
		}
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			throw new ParamError(String.format("%s should be long integer", name));
		}
	}

	public List<Long> checkInt64s(Function<Long, Boolean> func, String msg) {
		List<Long> values = int64s();
		for (long value : values) {
			if (!func.apply(value)) {
				if (msg == null) {
					msg = String.format("param %s with value=%s check int64s error", name, value);
				}
				throw new ParamError(msg);
			}
		}
		return values;
	}

	public List<Long> checkInt64s(Function<Long, Boolean> func) {
		return checkInt64s(func, null);
	}

	private final static String[] falses = new String[] { "false", "off", "", "0" };

	public boolean bool() {
		String value = string();
		if (value == null) {
			throw new ParamError(String.format("%s should not be empty", name));
		}
		value = value.toLowerCase();
		for (String v : falses) {
			if (value.equals(v)) {
				return false;
			}
		}
		return true;
	}

	public boolean bool(boolean defaultValue) {
		String value = string();
		if (value == null) {
			return defaultValue;
		}
		value = value.toLowerCase();
		for (String v : falses) {
			if (value.equals(v)) {
				return false;
			}
		}
		return true;
	}

	public List<String> strings() {
		return values;
	}
	
	public List<String> checkStrings(Function<String, Boolean> func, String msg) {
		List<String> values = strings();
		for (String value : values) {
			if (!func.apply(value)) {
				if (msg == null) {
					msg = String.format("param %s with value=%s check strings error", name, value);
				}
				throw new ParamError(msg);
			}
		}
		return values;
	}

	public List<Integer> int32s() {
		List<String> strs = this.strings();
		List<Integer> values = new ArrayList<>(strs.size());
		for (String str : strs) {
			try {
				int val = Integer.parseInt(str);
				values.add(val);
			} catch (NumberFormatException e) {
				throw new ParamError(String.format("%s should be integer", name));
			}
		}
		return values;
	}

	public List<Long> int64s() {
		List<String> strs = this.strings();
		List<Long> values = new ArrayList<>(strs.size());
		for (String str : strs) {
			try {
				long val = Long.parseLong(str);
				values.add(val);
			} catch (NumberFormatException e) {
				throw new ParamError(String.format("%s should be integer", name));
			}
		}
		return values;
	}

	public List<Boolean> bools() {
		List<String> strs = this.strings();
		List<Boolean> values = new ArrayList<>(strs.size());
		for (String str : strs) {
			str = str.toLowerCase();
			for (String v : falses) {
				if (str.equals(v)) {
					values.add(false);
				} else {
					values.add(true);
				}
			}
		}
		return values;
	}

	private void assertIf(boolean result, String msg) {
		if (!result) {
			throw new ParamError(msg);
		}
	}

}
