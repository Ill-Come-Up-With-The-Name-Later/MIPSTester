package misc;

import util.BinaryConversion;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * The program memory. Indexes must be accessed
 * by 4s. Only multiples of 4 are acceptable indices.
 */
public class Memory {

	public static final Memory GLOBAL_MEMORY = new Memory();
	public static final Memory STACK_MEMORY = new Memory(4096);

	private final ArrayList<Word> values;

	private Memory() {
		// 2^15 words can be stored
		this(16384);
	}

	private Memory(int size) {
		values = new ArrayList<>(size);

		for(int i = 0; i < size; i++) {
			values.add(null);
		}
	}

	/**
	 * Gets the <code>Word</code> at an index.
	 * The index must be a multiple of 4.
	 *
	 * @param index An index
	 * @return The <code>Word</code> at <code>index</code>
	 */
	public Word getWord(int index) {
		if(index % 4 != 0) {
			throw new IllegalArgumentException("Index must be a multiple of 4");
		}

		int trueIndex = index / 4;
		return values.get(trueIndex);
	}

	/**
	 * Sets the <code>Word</code> at an index.
	 *
	 * @param word The <code>Word</code> to write
	 * @param index The index to write <code>word</code> to
	 */
	public void setWord(Word word, int index) {
		if(index % 4 != 0) {
			throw new IllegalArgumentException("Index must be a multiple of 4");
		}

		int trueIndex = index / 4;
		values.set(trueIndex, word);
	}

	/**
	 * Finds the start and end byte index
	 * of the first available block of memory of
	 * a certain length.
	 *
	 * @param length The required number of bytes, must be
	 *               a multiple of 4
	 * @return The start and end index in memory that is
	 * 				 of the required length, or [-1, -1] if there
	 * 				 is none
	 */
	public int[] findAvailableMemory(int length) {
		if(length % 4 != 0) {
			throw new IllegalArgumentException("Length must be a multiple of 4");
		}

		if(length <= 0) {
			throw new IllegalArgumentException("Length must be greater than 0");
		}

		if(length > values.size() * 4) {
			throw new IllegalArgumentException("Cannot use more memory than is available");
		}

		int[] indices = new int[2];
		Arrays.fill(indices, -1);

		int start = 0;
		int end = 0;

		for(int i = 0; i < values.size() * 4; i += 4) {
			if(getWord(i) == null) {
				end += 4;

				if(end - start == length) {
					indices[0] = start;
					indices[1] = end - 1;

					return indices;
				}
			} else {
				start = i + 4;
				end = i + 4;
			}
		}

		return indices;
	}

	/**
	 * Stores a String into memory. Assumes
	 * the appropriate memory is already available
	 * and will overwrite anything that was in an address
	 * prior.
	 *
	 * @param string The String to store
	 * @param startIndex The start of the String in memory
	 */
	public void storeString(String string, int startIndex) {
		if(startIndex % 4 != 0) {
			throw new IllegalArgumentException("Index must be a multiple of 4");
		}

		String[] binary = BinaryConversion.stringToBinary(string);
		Word[] words = new Word[binary.length];

		for(int i = 0; i < binary.length; i++) {
			Word word = new Word();
			word.storeStringNum(binary[i]);
			words[i] = word;
		}

		int currentIndex = startIndex / 4;

		for(Word word : words) {
			setWord(word, currentIndex);
			currentIndex += 4;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < values.size(); i++) {
			if(values.get(i) != null) {
				sb.append("Memory[").append(i * 4).append("] = ").append(values.get(i).toString()).append("\n");
			}
		}

		return sb.toString();
	}
}
