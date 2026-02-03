package interpreter.errors;

/**
 * Occurs when a symbol is not properly declared.
 */
public class NotASymbolError extends Error {

	private final int lineNum;
	private final String symbol;

	public NotASymbolError(int lineNum, String symbol) {
		super();

		this.lineNum = lineNum;
		this.symbol = symbol;
	}

	public String getMessage() {
		return "Line " + lineNum + ": " + symbol + " is not a valid symbol declaration.";
	}
}
