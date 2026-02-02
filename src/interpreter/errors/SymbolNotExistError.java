package interpreter.errors;

public class SymbolNotExistError extends Error {

	private int lineNum;
	private String symbol;

	public SymbolNotExistError(int lineNum, String symbol) {
		super();

		this.lineNum = lineNum;
		this.symbol = symbol;
	}

	public String getMessage() {
		return "Line " + lineNum + ": " + symbol + " does not exist.";
	}
}
