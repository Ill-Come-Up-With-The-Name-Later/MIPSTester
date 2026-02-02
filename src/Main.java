import interpreter.FileParser;
import misc.Registers;

public class Main {

	public static void main(String[] args) {
		FileParser.GLOBAL.readFile("program.txt");
	}
}
