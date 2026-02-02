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
}
