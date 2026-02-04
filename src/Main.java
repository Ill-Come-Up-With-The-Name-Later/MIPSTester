import interpreter.file.FileParser;
import misc.Memory;
import program.Program;

public class Main {

	public static void main(String[] args) {
		FileParser.GLOBAL.readFile("program.txt");
		Program.MAIN_PROGRAM.run();

		System.out.println("Post-execution");
		System.out.println(Memory.GLOBAL_MEMORY);
	}
}
