package ui;

import interpreter.file.FileParser;
import misc.Memory;
import misc.Register;
import misc.Registers;
import program.Program;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * The command line for the interpreter.
 */
public class CommandInterface {

	/**
	 * Reads input from the command line.
	 */
	public static void readCommands() {
		Scanner sc = new Scanner(System.in);

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
							throw new IllegalArgumentException("Wrong number of arguments");
						}

						FileParser.GLOBAL.readFile(tokens[1]);
						Program.MAIN_PROGRAM.run();
						break;
					case "print-memory":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("Wrong number of arguments");
						}

						switch (tokens[1]) {
							case "bin":
								System.out.println(Memory.GLOBAL_MEMORY);
								break;
							case "hex":
								Memory.GLOBAL_MEMORY.printMemoryInHex();
								break;
							case "dec":
								Memory.GLOBAL_MEMORY.printMemoryInBase10();
								break;
						}
						break;
					case "print-registers":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("Wrong number of arguments");
						}

						switch (tokens[1]) {
							case "bin":
								Registers.printRegisters();
								break;
							case "hex":
								Registers.printRegistersHex();
								break;
							case "dec":
								Registers.printRegistersBase10();
								break;
						}
						break;
					case "print-stack":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("Wrong number of arguments");
						}

						switch (tokens[1]) {
							case "bin":
								System.out.println(Memory.STACK_MEMORY);
								break;
							case "hex":
								Memory.STACK_MEMORY.printMemoryInHex();
								break;
							case "dec":
								Memory.STACK_MEMORY.printMemoryInBase10();
								break;
						}
						break;
					case "write-output-file":
						if (tokens.length != 2) {
							throw new IllegalArgumentException("Wrong number of arguments");
						}

						Program.MAIN_PROGRAM.outputToFile(tokens[1]);
						break;
					case "print-register":
						if (tokens.length != 3) {
							throw new IllegalArgumentException("Wrong number of arguments");
						}

						Register register = Registers.getFromString(tokens[1]);

						if (register == null) {
							throw new IllegalArgumentException("Register not found");
						}

						switch (tokens[2]) {
							case "bin":
								System.out.println(register.getValueString());
								break;
							case "hex":
								System.out.println(register.getHexValueString());
								break;
							case "dec":
								System.out.println(register.getIntegerOfValues());
								break;
						}

						break;
					default:
						throw new IllegalArgumentException("Command does not exist: " + tokens[0]);
				}
			} catch(NoSuchElementException e) {
				System.exit(0);
			} catch(IllegalArgumentException e) {
				System.out.println(e.getMessage());
			}
		}
	}
}
