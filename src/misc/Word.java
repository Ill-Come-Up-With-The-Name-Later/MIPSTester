package misc;

import util.BinaryConversion;

/**
 * A 4-byte chunk of memory.
 */
public class Word {

	private int[] values;

	public Word() {
		values = new int[32];
	}

	/**
	 * Set the value at an index
	 * of the values array.
	 *
	 * @param value A value
	 * @param index The index to place <code>value</code> at
	 */
	public void setValue(int value, int index) {
		if(value == 1 || value == 0) {
			values[index] = value;
		} else {
			throw new IllegalArgumentException("Value must be a binary digit");
		}
	}

	/**
	 * Sets the values array to a new one.
	 *
	 * @param values The array of new values
	 */
	public void setValues(int[] values) {
		if(values.length != 32) {
			throw new IllegalArgumentException("Values array length should be 32");
		}

		this.values = values;
	}

	/**
	 * Gets the value at an index
	 *
	 * @param index An index
	 * @return The value at <code>index</code>
	 */
	public int getValue(int index) {
		return values[index];
	}

	/**
	 * Gets the entire values array.
	 *
	 * @return The entire values array
	 */
	public int[] getValues() {
		return values;
	}

	/**
	 * Gets a String that represents
	 * the value held in <code>values</code>.
	 *
	 * @return The value in <code>values</code> as a
	 * 				 String.
	 */
	public String getValueString() {
		StringBuilder sb = new StringBuilder();

		for(int value : values) {
			sb.append(value);
		}

		return sb.toString();
	}

	/**
	 * Gets the value of this <code>Word</code> in hexadecimal.
	 *
	 * @return The value in this in hexadecimal
	 */
	public String getHexValueString() {
		return BinaryConversion.hexToBinaryInt(getValueString());
	}

	/**
	 * Converts the value of the
	 * <code>Register</code> to a base-10 integer.
	 *
	 * @return The base-10 integer equivalent
	 * 				 of the value in this <code>Word</code>
	 */
	public int getIntegerOfValues() {
		return Integer.parseInt(BinaryConversion.binaryToInt(getValueString()));
	}

	/**
	 * Stores a base-10 number.
	 *
	 * @param number The number to store
	 */
	public void storeNum(int number) {
		storeStringNum(BinaryConversion.intToBinary(number));
	}

	/**
	 * Stores a string representation of a binary number
	 * into the <code>Word</code>.
	 *
	 * @param num The String representation
	 */
	public void storeStringNum(String num) {
		if(num.length() != 32) {
			throw new IllegalArgumentException("Value must be a 32 bit binary digit");
		}

		for(int i = 0; i < 32; i++) {
			setValue(Integer.parseInt(String.valueOf(num.charAt(i))), i);
		}
	}

	/**
	 * Copies the value of this <code>Word</code>
	 * into another.
	 *
	 * @return Another <code>Word</code> with the same
	 * 				 value
	 */
	public Word copy() {
		Word word = new Word();
		word.values = this.values;
		return word;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 32; i++) {
			sb.append(values[i]);
		}

		return sb.toString();
	}
}
