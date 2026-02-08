import interpreter.file.FileParser;
import program.Program;

public class Main {

	public static void main(String[] args) {
		FileParser.GLOBAL.readFile(args[0]);
		Program.MAIN_PROGRAM.run();
		Program.MAIN_PROGRAM.outputToFile("output.txt");
	}
}
