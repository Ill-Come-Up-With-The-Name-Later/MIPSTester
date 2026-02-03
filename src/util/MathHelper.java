package util;

public class MathHelper {

	/**
	 * Multiplies two 32-bit integers. Accounts
	 * for the possibility of creating a 64-bit result.
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
		String[] binary = BinaryConversion.stringToBinary(string);

		return binary.length * 4;
	}
}
