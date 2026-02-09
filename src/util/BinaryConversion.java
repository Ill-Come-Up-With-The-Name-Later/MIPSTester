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
	public static int binaryToInt(String binary) {
		return Integer.parseUnsignedInt(binary, 2);
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
	public static long binaryToLong(String binary) {
		return Long.parseUnsignedLong(binary, 2);
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

	/**
	 * Converts a String to binary.
	 *
	 * @param string The string
	 * @return The binary representation of String's
	 * 				 characters
	 */
	public static String[] stringToBinary(String string) {
		String[] binary = new String[string.length()];

		for(int i = 0; i < string.length(); i++) {
			int n = string.charAt(i);

			binary[i] = intToBinary(n);
		}

		return binary;
	}

	/**
	 * Converts binary to a String using
	 * character codes.
	 *
	 * @param binary A binary representation of a String
	 * @return A String
	 */
	public static String binaryToString(String[] binary) {
		StringBuilder builder = new StringBuilder();

		for(String s : binary) {
			builder.append((char) binaryToInt(s));
		}

		return builder.toString();
	}

	/**
	 * Converts binary to hexadecimal.
	 *
	 * @param binary A binary String
	 * @return The hexadecimal conversion of
	 * 				 <code>binary</code>
	 */
	public static String binaryToHex(String binary) {
		return Integer.toHexString(binaryToInt(binary));
	}

	/**
	 * Converts a base-10 integer to hexadecimal.
	 *
	 * @param num A base-10 number
	 * @return The hexadecimal conversion of
	 * 				 <code>num</code>
	 */
	public static String intToHex(int num) {
		return Integer.toHexString(num);
	}

	/**
	 * Converts a base-10 long to hexadecimal.
	 *
	 * @param num A base-10 number
	 * @return The hexadecimal conversion of
	 * 				 <code>num</code>
	 */
	public static String longToHex(long num) {
		return Long.toHexString(num);
	}

	/**
	 * Converts hexadecimal to 32-bit binary.
	 *
	 * @param hex A hexadecimal number
	 * @return A binary conversion of
	 * 				 <code>hex</code>
	 */
	public static String hexToBinaryInt(String hex) {
		String binary = Integer.toBinaryString(Integer.parseUnsignedInt(hex, 16));
		return "0".repeat(32 - binary.length()).concat(binary);
	}

	/**
	 * Converts hexadecimal to 64-bit binary.
	 *
	 * @param hex A hexadecimal number
	 * @return A binary conversion of
	 * 				 <code>hex</code>
	 */
	public static String hexToBinaryLong(String hex) {
		String binary = Long.toBinaryString(Long.parseUnsignedLong(hex, 16));
		return "0".repeat(64 - binary.length()).concat(binary);
	}

	/**
	 * Converts hexadecimal to an integer.
	 *
	 * @param hex A hexadecimal number
	 * @return The base-10 version of <code>hex</code>
	 */
	public static int hexToInt(String hex) {
		return Integer.parseUnsignedInt(hex, 16);
	}

	/**
	 * Converts hexadecimal to a long.
	 *
	 * @param hex A hexadecimal number
	 * @return The base-10 version of <code>hex</code>
	 */
	public static long hexToLong(String hex) {
		return Long.parseUnsignedLong(hex, 16);
	}
}
