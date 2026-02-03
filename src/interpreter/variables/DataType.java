package interpreter.variables;

public enum DataType {

	WORD(".word"),
	ASCII(".asciiz"),
	;

	private final String id;

	DataType(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
