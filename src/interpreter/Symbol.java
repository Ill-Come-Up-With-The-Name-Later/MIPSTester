package interpreter;

/**
 * A user defined symbol. Stores
 * numbers or Strings.
 */
public class Symbol {

	private Object value;
	private String name;

	public Symbol(Object value, String name) {
		this.value = value;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}
}
