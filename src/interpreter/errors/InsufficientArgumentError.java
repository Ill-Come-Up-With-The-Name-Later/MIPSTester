package interpreter.errors;

public class InsufficientArgumentError extends Error {

	private final int lineNum;
	private final String message;

	public InsufficientArgumentError(int lineNum, String message) {
		super(message);
		this.lineNum = lineNum;
		this.message = message;
	}

	@Override
	public String getMessage() {
		return "Line " + lineNum + ": '" + message + "' insufficient arguments provided";
	}
}
