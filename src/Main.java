import interpreter.file.FileParser;
import misc.Memory;
import program.Program;
import util.BinaryConversion;

import java.util.Arrays;

public class Main {

	public static void main(String[] args) {
		FileParser.GLOBAL.readFile(args[0]);
		Program.MAIN_PROGRAM.run();

		System.out.println("Post-execution");

		System.out.println(Memory.GLOBAL_MEMORY);
		System.out.println(Memory.STACK_MEMORY);
	}
}
