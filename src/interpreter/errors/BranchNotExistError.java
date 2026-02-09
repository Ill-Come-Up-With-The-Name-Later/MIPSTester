package interpreter.errors;

/**
 * Occurs when referencing a non-existent branch
 */
public class BranchNotExistError extends Error {

	public BranchNotExistError() {
		super();
	}

	@Override
	public String getMessage() {
		return "Branch does not exist.";
	}
}
