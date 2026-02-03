package interpreter.errors;

public class DuplicateBranchError extends Error {

	private final String branch;
	private final int lineNum;

	public DuplicateBranchError(String branch, int lineNumber) {
		this.branch = branch;
		this.lineNum = lineNumber;
	}

	@Override
	public String getMessage() {
		return "Line: " + lineNum + ": Duplicate branch: " + branch;
	}
}
