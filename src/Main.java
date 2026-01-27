import interpreter.Memory;
import interpreter.Word;
import util.BinaryConversion;

public class Main {

	public static void main(String[] args) {
		for(int i = 0; i <= 100; i++) {
			Word word = new Word();
			word.storeStringNum(BinaryConversion.intToBinary(i));

			Memory.GLOBAL_MEMORY.setWord(word, i * 4);
		}

		System.out.println(Memory.GLOBAL_MEMORY);
	}
}
