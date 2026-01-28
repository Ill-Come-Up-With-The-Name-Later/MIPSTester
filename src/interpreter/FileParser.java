package interpreter;

import interpreter.errors.InsufficientArgumentError;
import interpreter.errors.InvalidCommandError;

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
				tokens = removeWhiteSpaces(tokens);

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

				if(tokens.length < command.getArgumentCount()) {
					throw new InsufficientArgumentError(lineNumber, tokens[commandIndex]);
				}

				if(tokens.length > commandIndex + command.getArgumentCount() + 1) {
					throw new InsufficientArgumentError(lineNumber, tokens[commandIndex]);
				}

				System.out.println(command + " Tokens: " + Arrays.toString(tokens));
				// TODO: Parse commands and their arguments into instructions to sequence
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	/**
	 * Removes whitespace tokens from
	 * the token array.
	 *
	 * @param tokens An array of tokens
	 * @return <code>tokens</code> without whitespace tokens
	 */
	private String[] removeWhiteSpaces(String[] tokens) {
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

	/**
	 * Determines if a String is valid <code>Command</code>.
	 *
	 * @param command A string
	 * @return If <code>command</code> is valid
	 */
	public boolean validCommand(String command) {
		return Command.getCommand(command) != null;
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
