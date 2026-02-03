package interpreter.variables;

/**
 * A user defined symbol. Stores
 * numbers or Strings.
 */
public class Symbol {

	private Object value;
	private final DataType type;
	private String name;

	public Symbol(Object value, DataType type, String name) {
		this.value = value;
		this.type = type;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public DataType getType() {
		return type;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return name + " = " + value + ", Type: " + type;
	}
}
