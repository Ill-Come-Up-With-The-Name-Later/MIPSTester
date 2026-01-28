package misc;

import java.util.ArrayList;

/**
 * The program memory. Indexes must be accessed
 * by 4s. Only multiples of 4 are acceptable indices.
 */
public class Memory {

	public static final Memory GLOBAL_MEMORY = new Memory();

	private final ArrayList<Word> values;

	private Memory() {
		values = new ArrayList<>();

		// 2^13 Words can be stored
		for(int i = 0; i < 8192; i++) {
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
