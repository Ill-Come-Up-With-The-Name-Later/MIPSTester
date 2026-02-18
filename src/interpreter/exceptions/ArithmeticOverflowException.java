package interpreter.exceptions;

/**
 * Occurs when there is overflow
 * from signed arithmetic operations.
 */
public class ArithmeticOverflowException extends ArithmeticException {

	private final int op1;
	private final int op2;

	public ArithmeticOverflowException(int op1, int op2) {
		super();

		this.op1 = op1;
		this.op2 = op2;
	}

	@Override
	public String getMessage() {
		return "Overflow from " + op1 + " and " + op2;
	}
}
