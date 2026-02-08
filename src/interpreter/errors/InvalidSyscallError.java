package interpreter.errors;

/**
 * Occurs if a syscall is called with an invalid
 * code.
 */
public class InvalidSyscallError extends Error {

	private final int call;

	public InvalidSyscallError(int call) {
		this.call = call;
	}

	@Override
	public String getMessage() {
		return call + " is not a valid system call.";
	}
}
