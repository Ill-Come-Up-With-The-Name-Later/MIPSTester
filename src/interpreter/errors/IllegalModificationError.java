package interpreter.errors;

/**
 * Occurs if a command attempts to modify
 * a <code>Register</code> it cannot modify
 */
public class IllegalModificationError extends Error {

	public IllegalModificationError() {
		super();
	}

	@Override
	public String getMessage() {
		return "Register cannot be modified by command";
	}
}
