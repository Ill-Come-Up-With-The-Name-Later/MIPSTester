package interpreter.variables;

/**
 * A user defined symbol. Stores
 * numbers or Strings, or empty
 * space for arrays.
 */
public class Symbol {

	private String value;
	private final DataType type;
	private String name;

	public Symbol(String value, DataType type, String name) {
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

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return name + " = " + value + ", Type: " + type;
	}
}
