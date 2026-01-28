package interpreter;

import interpreter.errors.InvalidCommandError;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
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
		try {
			Scanner scanner = new Scanner(file);

			while(scanner.hasNextLine()) {
				String line = scanner.nextLine();

				// Disregard this line if there is only a comment
				if(line.startsWith("#")) {
					continue;
				}

				// Disregard empty lines
				if(line.isEmpty()) {
					continue;
				}

				// Get the command on this line
				// Account for a branch declaration before a command
				String[] tokens = line.split(" ");
				Command command = null;

				if(tokens[0].endsWith(":")) {
					Branch branch = new Branch(activeBranch, tokens[0].substring(0, tokens[0].length() - 1));
					activeBranch = branch;
					System.out.println("Branch is now: " + activeBranch.getName());

					if(tokens.length > 1) {
						command = Command.getCommand(tokens[1]);
					} else {
						continue;
					}
				}

				if(command == null && tokens.length > 1) {
					command = Command.getCommand(tokens[0]);
				}

				if(command == null) {
					throw new InvalidCommandError(tokens[0]);
				}

				System.out.println(command);
			}
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
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
