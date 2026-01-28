package interpreter.errors;

/**
 * Occurs when an argument is not valid
 * for the command it is passed to.
 */
public class ImproperArgumentError extends Error {

	private final int lineNum;
	private final String argument;
	private final String command;

	public ImproperArgumentError(int lineNum, String argument, String command) {
		this.lineNum = lineNum;
		this.argument = argument;
		this.command = command;
	}

	@Override
	public String getMessage() {
		return "Line " + lineNum + ": Argument: " + argument
						+ " is not valid for the command " + command;
	}
}
