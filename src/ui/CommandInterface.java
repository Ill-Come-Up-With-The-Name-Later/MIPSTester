package ui;

import interpreter.file.FileParser;
import misc.Memory;
import misc.Register;
import misc.Registers;
import program.Program;

import java.util.Arrays;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The command line for the interpreter.
 */
public class CommandInterface {

	public static String[] commands = new String[]{ "quit", "run", "run-and-output", "print-memory", "print-registers",
					"print-stack", "write-output-file", "print-register", "help", "run-from" };

	/**
	 * Reads input from the command line.
	 */
	public static void readCommands() {
		Scanner sc = new Scanner(System.in);

		System.out.print("Type \"help\" for a list of commands.");

		while(true) {
			System.out.print("\nEnter Command: ");

			try {
				String command = sc.nextLine();
				String[] tokens = command.split(" ");

				switch (tokens[0]) {
					case "quit":
						return;
					case "run":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}

						FileParser.GLOBAL.readFile(tokens[1]);
						Program.MAIN_PROGRAM.run();
						break;
					case "run-and-output":
						if (tokens.length != 3) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}
						FileParser.GLOBAL.readFile(tokens[1]);
						Program.MAIN_PROGRAM.run();
						Program.MAIN_PROGRAM.outputToFile(tokens[2]);

						break;
					case "run-from":
						if (tokens.length != 4) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}

						FileParser.GLOBAL.readFile(tokens[1]);

						int start = Integer.parseInt(tokens[2]);
						int end = Integer.parseInt(tokens[3]);

						Program.MAIN_PROGRAM.run(start, end);
						break;
					case "print-memory":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}

						switch (tokens[1]) {
							case "bin":
								System.out.println(Memory.GLOBAL_MEMORY);
								break;
							case "hex":
								Memory.GLOBAL_MEMORY.printMemoryInHex();
								break;
							case "octal":
								Memory.GLOBAL_MEMORY.printMemoryInOctal();
								break;
							case "dec":
								Memory.GLOBAL_MEMORY.printMemoryInBase10();
								break;
						}
						break;
					case "print-registers":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}

						switch (tokens[1]) {
							case "bin":
								Registers.printRegisters();
								break;
							case "hex":
								Registers.printRegistersHex();
								break;
							case "octal":
								Registers.printRegistersOctal();
								break;
							case "dec":
								Registers.printRegistersBase10();
								break;
						}
						break;
					case "print-stack":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}

						switch (tokens[1]) {
							case "bin":
								System.out.println(Memory.STACK_MEMORY);
								break;
							case "hex":
								Memory.STACK_MEMORY.printMemoryInHex();
								break;
							case "octal":
								Memory.STACK_MEMORY.printMemoryInOctal();
								break;
							case "dec":
								Memory.STACK_MEMORY.printMemoryInBase10();
								break;
						}
						break;
					case "write-output-file":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}

						Program.MAIN_PROGRAM.outputToFile(tokens[1]);
						break;
					case "print-register":
						if (tokens.length != 3) {
							throw new IllegalArgumentException("\nWrong number of arguments");
						}

						Register register = Registers.getFromString(tokens[1]);

						if (register == null) {
							throw new IllegalArgumentException("\nRegister not found");
						}

						switch (tokens[2]) {
							case "bin":
								System.out.println(register.getValueString());
								break;
							case "hex":
								System.out.println(register.getHexValueString());
								break;
							case "octal":
								System.out.println(register.getOctalValueString());
								break;
							case "dec":
								System.out.println(register.getIntegerOfValues());
								break;
						}

						break;
					case "help":
						System.out.println(Arrays.toString(commands));
						break;
					default:
						System.out.print("Type \"help\" for a list of commands.");
						throw new IllegalArgumentException("\nCommand does not exist: " + tokens[0]);
				}
			} catch(NoSuchElementException e) {
				System.exit(0);
			} catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
