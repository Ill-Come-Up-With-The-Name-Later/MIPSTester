import interpreter.file.FileParser;
import misc.Memory;
import misc.Word;
import program.Program;

public class Main {

	public static void main(String[] args) {
		Word word = new Word();
		word.storeNum(123);
		Memory.GLOBAL_MEMORY.setWord(word, 12);

		System.out.println("Pre-execution");
		System.out.println(Memory.GLOBAL_MEMORY);

		FileParser.GLOBAL.readFile("program.txt");
		Program.MAIN_PROGRAM.run();

		System.out.println("Post-execution");
		System.out.println(Memory.GLOBAL_MEMORY);
	}
}
