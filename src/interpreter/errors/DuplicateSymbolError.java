package interpreter.errors;

import interpreter.variables.Symbol;

public class DuplicateSymbolError extends Error {

	private final int lineNum;
	private final Symbol symbol;

	public DuplicateSymbolError(int lineNumber, Symbol symbol) {
		this.lineNum = lineNumber;
		this.symbol = symbol;
	}

	@Override
	public String getMessage() {
		return "Line: " + lineNum + ": Duplicate symbol: " + symbol.getName();
	}
}
