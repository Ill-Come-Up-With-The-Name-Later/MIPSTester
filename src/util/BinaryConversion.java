package util;

public class BinaryConversion {

	/**
	 * Converts a base 10 integer to binary
	 *
	 * @param num A base 10 integer
	 * @return The binary representation of <code>num</code>
	 */
	public static String intToBinary(int num) {
		String binary = Integer.toBinaryString(num);
		int len = binary.length();

		return "0".repeat(32 - len) +
						binary;
	}

	/**
	 * Converts a binary value to a base 10
	 * integer.
	 *
	 * @param binary A binary value
	 * @return The base 10 equivalent of <code>binary</code>
	 */
	public static String binaryToInt(String binary) {
		return String.valueOf(Integer.parseInt(binary, 2));
	}
}
