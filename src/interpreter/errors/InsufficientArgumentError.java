package interpreter.errors;

/**
 * Occurs if a command is given an incorrect
 * amount of arguments.
 */
public class InsufficientArgumentError extends Error {

	private final int lineNum;
	private final String message;
	private final int expectedArgumentCount;
	private final int foundArguments;

	public InsufficientArgumentError(int lineNum, String message, int expected, int found) {
		super(message);
		this.lineNum = lineNum;
		this.message = message;
		this.expectedArgumentCount = expected;
		this.foundArguments = found;
	}

	@Override
	public String getMessage() {
		return "Line " + lineNum + ": '" + message + "' insufficient arguments provided." +
						" Expected " + expectedArgumentCount + " argument(s). Found " + foundArguments + ".";
	}
}
