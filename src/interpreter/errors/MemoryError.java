package interpreter.errors;

import interpreter.variables.Symbol;

/**
 * Occurs if the program cannot find suitable
 * memory to place a symbol.
 */
public class MemoryError extends Error {

	private final Symbol symbol;

	public MemoryError(Symbol symbol) {
		super();
		this.symbol = symbol;
	}

	@Override
	public String getMessage() {
		return "Couldn't find memory for " + symbol;
	}
}
