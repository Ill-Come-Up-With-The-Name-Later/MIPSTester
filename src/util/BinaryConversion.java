package util;

import java.util.Arrays;

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
	 * Converts hexadecimal to a base-10 integer.
	 *
	 * @param hex A hexadecimal number
	 * @return The base-10 version of <code>hex</code>
	 */
	public static int hexToInt(String hex) {
		return Integer.parseUnsignedInt(hex, 16);
	}

	/**
	 * Converts hexadecimal to a base-10 long.
	 *
	 * @param hex A hexadecimal number
	 * @return The base-10 version of <code>hex</code>
	 */
	public static long hexToLong(String hex) {
		return Long.parseUnsignedLong(hex, 16);
	}

	/**
	 * Converts a base-10 integer to octal.
	 *
	 * @param num A base-10 integer
	 * @return The octal version of <code>num</code>
	 */
	public static int intToOctal(int num) {
		return Integer.parseUnsignedInt(Integer.toOctalString(num), 10);
	}

	/**
	 * Converts a base-10 long to octal.
	 *
	 * @param num A base-10 long
	 * @return The octal version of <code>num</code>
	 */
	public static long longToOctal(long num) {
		return Long.parseUnsignedLong(Long.toOctalString(num), 10);
	}

	/**
	 * Converts octal to a base-10 int.
	 *
	 * @param octal An octal number
	 * @return The base-10 version of <code>octal</code>
	 */
	public static int octalToInt(String octal) {
		return Integer.parseUnsignedInt(octal, 8);
	}

	/**
	 * Converts octal to a base-10 long.
	 *
	 * @param octal An octal number
	 * @return The base-10 version of <code>octal</code>
	 */
	public static long octalToLong(String octal) {
		return Long.parseUnsignedLong(octal, 8);
	}

	/**
	 * Converts a String into binary using
	 * 8 bits for each character and storing
	 * the bits in 32-bit chunks.
	 *
	 * @param string The String
	 * @return The String in binary
	 */
	public static String[] stringToCompressedBinary(String string) {
		String[] binary = new String[string.length() % 4 == 0 ?
						string.length() / 4 : string.length() / 4 + 1];

		Arrays.fill(binary, "");

		int index = 0;
		int byteIndex = 0;

		for(int i = 0; i < string.length(); i++) {
			byteIndex += 1;
			String charBinary = BinaryConversion.intToBinary(string.charAt(i)).substring(24, 32);;

			binary[index] += charBinary;

			if(i == string.length() - 1 && byteIndex < 4) {
				binary[index] += BinaryConversion.intToBinary('\0').substring(24, 32);
			}

			if(i == string.length() - 1) {
				binary[index] += "0".repeat(32 - binary[index].length());
			}

			if(byteIndex == 4) {
				byteIndex = 0;

				index++;
			}
		}

		return binary;
	}
}
