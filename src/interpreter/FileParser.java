package interpreter;

import interpreter.errors.ImproperArgumentError;
import interpreter.errors.InsufficientArgumentError;
import interpreter.errors.InvalidCommandError;
import misc.Register;
import misc.Registers;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

/**
 * Reads the actual program file and establishes
 * the branches and instruction order.
 */
public class FileParser {

	public static final FileParser GLOBAL = new FileParser();

	private final Branch mainBranch;
	private Branch activeBranch;
	private ArrayList<Branch> branches;
	private ArrayList<Instruction> instructions;

	private FileParser() {
		branches = new ArrayList<>();
		instructions = new ArrayList<>();

		mainBranch = new Branch();
		activeBranch = mainBranch;

		branches.add(mainBranch);
	}

	/**
	 * Reads the program file and parses all
	 * instructions and branches.
	 *
	 * @param fileName The program file to read
	 */
	public void readFile(String fileName) {
		File file = new File(fileName);
		int lineNumber = 0;

		try {
			Scanner scanner = new Scanner(file);

			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNumber++;

				// Disregard this line if there is only a comment
				// or if the line empty
				if(line.startsWith("#") || line.isEmpty()) {
					continue;
				}

				// Get the command on this line
				// Account for a branch declaration before a command
				String[] tokens = line.split("[(, *)]");
				tokens = cleanTokens(tokens);

				Command command = null;
				int commandIndex = 0;

				if(tokens[0].endsWith(":")) {
					Branch branch = new Branch(activeBranch, tokens[0].substring(0, tokens[0].length() - 1));
					activeBranch = branch;
					System.out.println("Branch is now: " + activeBranch.getName() +
									", Previous: " + branch.getPrevious().getName());

					if(tokens.length > 1) {
						command = Command.getCommand(tokens[1]);
						commandIndex = 1;
					} else {
						continue;
					}
				}

				if(command == null && tokens.length > 1) {
					command = Command.getCommand(tokens[0]);
				}

				if(command == null) {
					throw new InvalidCommandError(lineNumber, tokens[commandIndex]);
				}

				int argumentStartIndex = commandIndex + 1;
				String[] arguments = Arrays.copyOfRange(tokens, argumentStartIndex, tokens.length);

				if(arguments.length != command.getArgumentCount()) {
					throw new InsufficientArgumentError(lineNumber, command.getName(),
									command.getArgumentCount(), arguments.length);
				}

				// TODO: Parse commands and their arguments into instructions to sequence
				System.out.println(command + " | Tokens: " + Arrays.toString(tokens));
				System.out.println("Arguments: " + Arrays.toString(arguments));


			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	/**
	 * Gets a <code>Register</code> from a string.
	 *
	 * @param argument A string argument
	 * @return The corresponding <code>Register</code> or <code>null</code>
	 */
	private Register getRegisterFromArgument(String argument) {
		if(argument.startsWith("$")) {
			return Registers.getFromString(argument.substring(1));
		}

		return null;
	}

	/**
	 * Gets an integer offset for load word
	 * and store word.
	 *
	 * @return The offset or <code>null</code>
	 */
	private Integer getOffsetFromArgument(String argument) {
		String[] tokens = cleanOffsetArgument(argument).split(" ");

		Register register = Registers.getFromString(tokens[1].substring(1));

		if(register == null) {
			return null;
		}

		return Integer.parseInt(tokens[0]) + register.getIntegerOfValues();
	}

	/**
	 * Strips parentheses from offset arguments
	 *
	 * @param argument A string
	 * @return <code>argument</code> with parentheses removed
	 * */
	private String cleanOffsetArgument(String argument) {
		StringBuilder builder = new StringBuilder();

		for(Character c : argument.toCharArray()) {
			if(c == '(' || c == ')') {
				builder.append(' ');
				continue;
			}

			builder.append(c);
		}

		return builder.toString().trim();
	}

	/**
	 * Cleans the token array.
	 *
	 * @param tokens An array of tokens
	 * @return <code>tokens</code> without whitespace tokens
	 */
	private String[] cleanTokens(String[] tokens) {
		ArrayList<String> list = new ArrayList<>();

		for(String token : tokens) {
			if(token.startsWith("#")) {
				break;
			}

			if(!token.isEmpty()) {
				list.add(token);
			}
		}

		String[] result = new String[list.size()];

		for(int i = 0; i < list.size(); i++) {
			result[i] = list.get(i);
		}

		return result;
	}

	public ArrayList<Branch> getBranches() {
		return branches;
	}

	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}

	public Branch getMainBranch() {
		return mainBranch;
	}
}
