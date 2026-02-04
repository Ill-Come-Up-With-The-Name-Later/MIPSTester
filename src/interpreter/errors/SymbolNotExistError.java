package interpreter.errors;

/**
 * Occurs if a symbol is invoked that is not
 * declared.
 */
public class SymbolNotExistError extends Error {

	private final int lineNum;
	private final String symbol;

	public SymbolNotExistError(int lineNum, String symbol) {
		super();

		this.lineNum = lineNum;
		this.symbol = symbol;
	}

	public SymbolNotExistError() {
		lineNum = -1;
		symbol = null;
	}

	@Override
	public String getMessage() {
		if(lineNum == -1) {
			return "Symbol Not Found";
		}

		return "Line " + lineNum + ": " + symbol + " does not exist.";
	}
}
