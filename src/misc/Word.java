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
	 * Converts the value of the
	 * <code>Word</code> to a base 10 integer.
	 *
	 * @return The base 10 integer equivalent
	 * 				 of the value in this <code>Word</code>
	 */
	public int getIntegerOfValues() {
		return Integer.parseInt(BinaryConversion.binaryToInt(String.valueOf(Integer.parseInt(getValueString()))));
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

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < 32; i++) {
			sb.append(values[i]);
		}

		return sb.toString();
	}
}
