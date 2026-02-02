package interpreter.errors;

/**
 * Occurs if an instruction fails to parse yet
 * causes no other errors.
 */
public class InstructionParseError extends Error {

	private final int lineNum;

	public InstructionParseError(int lineNum) {
		super();
		this.lineNum = lineNum;
	}

	@Override
	public String getMessage() {
		return "Line " + lineNum + ": Failed to parse instruction.";
	}
}
