package util;

import interpreter.exceptions.ArithmeticOverflowException;
import interpreter.exceptions.ArithmeticUnderflowException;

public class MathHelper {

	/**
	 * Multiplies two 32-bit integers. Accounts
	 * for the possibility of creating a 64-bit result.
	 *
	 * @param a A number
	 * @param b A number
	 * @return The result of multiplication
	 */
	public static long multiply(int a, int b) {
		return (long) a * (long) b;
	}

	/**
	 * Turns an Integer into its unsigned form.
	 *
	 * @param a An integer
	 * @return The unsigned form of integer
	 */
	public static long toUnsigned(int a) {
		if(a >= 0) {
			return a;
		}

		return (((long)Integer.MAX_VALUE * 2)) - (Math.abs(a) - 2);
	}

	/**
	 * The length of a String in bytes.
	 *
	 * @param string The String
	 * @return The number of bytes needed for the String
	 */
	public static int binaryLength(String string) {
		String[] binary = BinaryConversion.stringToCompressedBinary(string);

		return binary.length * 4;
	}

	/**
	 * Verifies that adding or subtracting two 32-bit numbers
	 * won't overflow or underflow. Will throw an exception
	 * for overflow/underflow.
	 *
	 * @param op1 The first number
	 * @param op2 The second number
	 */
	public static void verifyAddSubtract(int op1, int op2) {
		long sum = op1 + op2;

		int signDiffer = op1 ^ op2;
		signDiffer = signDiffer <= 0 ? 1 : 0; // If signs differ, signDiffer = 1

		if(signDiffer == 0) {
			long sumSignDiffer = sum ^ op1;
			sumSignDiffer = sumSignDiffer <= 0 ? 1 : 0; // If signs differ, sumSignDiffer = 1

			if(sumSignDiffer != 0 && (~op1 < 0)) {
				throw new ArithmeticOverflowException(op1, op2);
			} else if(sumSignDiffer != 0 && (~op1 > 0)) {
				throw new ArithmeticUnderflowException(op1, op2);
			}
		}
	}
}
