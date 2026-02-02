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
		return String.valueOf(Integer.parseUnsignedInt(binary, 2));
	}

	/**
	 * Converts a long to a 64 bit binary String.
	 *
	 * @param num The number
	 * @return The binary representation of <code>num</code>
	 */
	public static String longToBinary(long num) {
		String binary = Long.toBinaryString(num);

		return "0".repeat(64 - binary.length())
						+ binary;
	}

	/**
	 * Converts a binary value to a base 10
	 * long.
	 *
	 * @param binary A binary value
	 * @return The base 10 equivalent of <code>binary</code>
	 */
	public static String binaryToLong(String binary) {
		return String.valueOf(Long.parseUnsignedLong(binary, 2));
	}

	/**
	 * Splits a 64-bit binary String. The first String
	 * is the most significant 32 bits and the second
	 * String will be the least significant.
	 *
	 * @param binary The binary representation
	 * @return A split of <code>binary</code> into two
	 * 				 32-bit chunks
	 */
	public static String[] split64BitBinary(String binary) {
		String mostSignificant = binary.substring(0, binary.length() / 2);
		String leastSignificant = binary.substring(binary.length() / 2);

		return new String[] { mostSignificant, leastSignificant };
	}
}
