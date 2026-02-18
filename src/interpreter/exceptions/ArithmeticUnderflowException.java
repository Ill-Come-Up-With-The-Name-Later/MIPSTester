package interpreter.exceptions;

/**
 * Occurs when there is underflow
 * from signed arithmetic operations.
 */
public class ArithmeticUnderflowException extends ArithmeticException {

	private final int op1;
	private final int op2;

	public ArithmeticUnderflowException(int op1, int op2) {
		super();

		this.op1 = op1;
		this.op2 = op2;
	}

	@Override
	public String getMessage() {
		return "Underflow from " + op1 + " and " + op2;
	}
}
