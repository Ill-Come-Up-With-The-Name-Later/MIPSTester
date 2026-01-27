import interpreter.Memory;
import interpreter.Register;
import interpreter.Word;
import util.BinaryConversion;

public class Main {

	public static void main(String[] args) {
		Word word = new Word();
		word.storeStringNum(BinaryConversion.intToBinary(600000));

		Memory.GLOBAL_MEMORY.setWord(word, 160);

		Register r = new Register();
		r.storeStringNum(BinaryConversion.intToBinary(160));

		System.out.println(Memory.GLOBAL_MEMORY.getWord(r.getIntegerOfValues()));
		System.out.println(Memory.GLOBAL_MEMORY);
	}
}
