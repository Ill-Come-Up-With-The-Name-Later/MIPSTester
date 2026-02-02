package interpreter.errors;

public class InstructionParseError extends Error {

	public InstructionParseError() {
		super();
	}

	@Override
	public String getMessage() {
		return "Failed to parse instruction.";
	}
}
