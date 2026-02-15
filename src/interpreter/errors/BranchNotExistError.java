package interpreter.errors;

import interpreter.instructions.Branch;

/**
 * Occurs when referencing a non-existent branch.
 */
public class BranchNotExistError extends Error {

	private final Branch branch;

	public BranchNotExistError(Branch branch) {
		super();
		this.branch = branch;
	}

	@Override
	public String getMessage() {
		return "Branch does not exist: " + branch.getName();
	}
}
