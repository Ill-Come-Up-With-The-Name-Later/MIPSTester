package interpreter.errors;

/**
 * Occurs if a command is not valid.
 */
public class InvalidCommandError extends Error {

	private final int lineNum;
	private final String message;

	public InvalidCommandError(int lineNum, String message) {
		super(message);
		this.lineNum = lineNum;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Line " + lineNum + ": '" + message + "' is not a valid command";
	}
}
