package interpreter.errors;

/**
 * Occurs if a command is not valid.
 */
public class InvalidCommandError extends Error {

	private final String message;

	public InvalidCommandError(String message) {
		super(message);
		this.message = message;
	}

	@Override
	public String getMessage() {
		return message + " is not a valid command";
	}
}
