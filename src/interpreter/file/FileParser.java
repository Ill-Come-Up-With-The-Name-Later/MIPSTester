package interpreter.file;

import interpreter.errors.*;
import interpreter.instructions.Branch;
import interpreter.instructions.Command;
import interpreter.instructions.Instruction;
import interpreter.variables.DataType;
import interpreter.variables.Symbol;
import misc.Register;
import misc.Registers;
import program.Program;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.Scanner;

/**
 * Reads the actual program file and establishes
 * the branches and instruction order.
 */
public class FileParser {

	public static final FileParser GLOBAL = new FileParser();

	private final Branch mainBranch;
	private Branch activeBranch;
	private final ArrayList<Branch> branches;
	private final ArrayList<Instruction> instructions;
	private final ArrayList<Symbol> symbols;

	private FileParser() {
		branches = new ArrayList<>();
		instructions = new ArrayList<>();
		symbols = new ArrayList<>();

		mainBranch = createBranch(null, "-", true);
		activeBranch = mainBranch;
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

		Section currentSection = Section.TEXT;

		try {
			Scanner scanner = new Scanner(file);

			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineNumber++;

				// Disregard this line if there is only a comment
				// or if the line empty
				if (line.startsWith("#") || line.isEmpty()) {
					continue;
				}

				if(line.startsWith(".data")) {
					currentSection = Section.DATA;
					continue;
				}

				if(line.startsWith(".text")) {
					currentSection = Section.TEXT;
					continue;
				}

				// User variables
				if(currentSection == Section.DATA) {
					Symbol symbol = getSymbol(line, lineNumber);

					if(symbolExists(symbol.getName())) {
						throw new DuplicateSymbolError(lineNumber, symbol);
					}

					symbols.add(symbol);

					continue;
				}

				// Assembly instructions

				// Get the command on this line
				// Account for a branch declaration before a command
				String[] tokens = line.split("[(, *)]");
				tokens = cleanTokens(tokens);

				Command command = null;
				int commandIndex = 0;

				if(tokens[0].endsWith(":")) {
					Branch branch = getBranchFromArgument(tokens[0].substring(0, tokens[0].length() - 1));

					if(branch == null) {
						branch = createBranch(activeBranch, tokens[0].substring(0, tokens[0].length() - 1), true);
					} else {
						instructions.add(new Instruction(Command.CREATE_BRANCH, branch));
					}

					activeBranch = branch;

					if (tokens.length > 1) {
						command = Command.getCommand(tokens[1]);
						commandIndex = 1;
					} else {
						continue;
					}
				}

				if (command == null && tokens.length > 1) {
					command = Command.getCommand(tokens[0]);
				}

				if(tokens.length == 1) {
					command = Command.getCommand(tokens[0]);
				}

				if (command == null) {
					throw new InvalidCommandError(lineNumber, tokens[commandIndex]);
				}

				int argumentStartIndex = commandIndex + 1;
				String[] arguments = Arrays.copyOfRange(tokens, argumentStartIndex, tokens.length);

				if (arguments.length != command.getArgumentCount()) {
					throw new InsufficientArgumentError(lineNumber, command.getName(),
									command.getArgumentCount(), arguments.length);
				}

				//System.out.println(command + " | Tokens: " +
				//				Arrays.toString(tokens) + " | Arguments: " + Arrays.toString(arguments));

				//Instruction parsed = validateCommand(command, arguments, lineNumber);
				Instruction parsed = validateCommand2(command, arguments, lineNumber);;

				if (parsed == null) {
					throw new InstructionParseError(lineNumber);
				}

				instructions.add(parsed);
				activeBranch.addInstruction(parsed);
				//System.out.println(parsed);
			}

			// Store everything for the program
			Program.MAIN_PROGRAM.setBranches(branches);
			Program.MAIN_PROGRAM.setInstructions(instructions);
			Program.MAIN_PROGRAM.setSymbols(symbols);
		} catch (FileNotFoundException e) {
			System.out.println("File not found");
		}
	}

	/**
	 * Creates a <code>Symbol</code> from a line.
	 *
	 * @param line The line
	 * @param lineNumber The line number in the program
	 * @return A parsed <code>Symbol</code>
	 */
	private Symbol getSymbol(String line, int lineNumber) {
		String[] tokens = line.split(" ");

		if(tokens.length < 3) {
			throw new NotASymbolError(lineNumber, tokens[0]);
		}

		DataType type = null;

		for(DataType dataType : DataType.values()) {
			if(tokens[1].equals(dataType.getId())) {
				type = dataType;
			}
		}

		if(type != DataType.ASCII && tokens.length > 3) {
			throw new NotASymbolError(lineNumber, tokens[0]);
		}

		if(type == null) {
			throw new NotASymbolError(lineNumber, tokens[0]);
		}

		if(type == DataType.ASCII && !tokens[2].startsWith("\"") && !tokens[2].endsWith("\"")) {
			throw new NotASymbolError(lineNumber, tokens[0]);
		}

		if((type == DataType.WORD || type == DataType.SPACE) && !stringNumeric(tokens[2])) {
			throw new NotASymbolError(lineNumber, tokens[0]);
		}

		if(type == DataType.SPACE && (Integer.parseInt(tokens[2]) <= 0 || Integer.parseInt(tokens[2]) % 4 != 0)) {
			throw new NotASymbolError(lineNumber, tokens[0]);
		}

		String value = tokens[2];

		if(type == DataType.ASCII) {
			String s = concatenate(Arrays.copyOfRange(tokens, 2, tokens.length));
			value = s.substring(1, s.length() - 2);
		}

		// Token comes out as "name:" so we use a substring
		return new Symbol(value, type, tokens[0].substring(0, tokens[0].length() - 1));
	}

	/**
	 * Concatenates all Strings in an array of
	 * Strings.
	 *
	 * @param tokens The array of Strings
	 * @return A combined String
	 */
	private String concatenate(String[] tokens) {
		StringBuilder sb = new StringBuilder();
		for(String token : tokens) {
			if(token.startsWith("#")) {
				return sb.toString();
			}

			sb.append(token);
			sb.append(" ");
		}

		return sb.toString();
	}

	/**
	 * Creates a new <code>Banch</code>.
	 *
	 * @param previous The previous <code>Branch</code>
	 * @param name The name of the new <code>Branch</code>
	 * @return The <code>Branch</code> created
	 */
	private Branch createBranch(Branch previous, String name, boolean addInstruction) {
		Branch branch = new Branch(previous, name);
		branches.add(branch);

		if(addInstruction) {
			instructions.add(new Instruction(Command.CREATE_BRANCH, branch));
		}

		return branch;
	}

	/**
	 * Gets the <code>Symbol</code> matching the argument
	 *
	 * @param argument A String argument
	 * @return The <code>Symbol</code> or <code>null</code>
	 */
	private Symbol getSymbolFromArgument(String argument) {
		for(Symbol symbol : symbols) {
			if(argument.equals(symbol.getName())) {
				return symbol;
			}
		}

		return null;
	}

	/**
	 * Determines if a String is also a number
	 *
	 * @param string A String
	 * @return If <code>string</code> is a number
	 */
	private boolean stringNumeric(String string) {
		try {
			Integer.parseInt(string);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	/**
	 * Validates a command and its arguments.
	 * Converts into an <code>Instruction</code>
	 * ready for sequencing.
	 *
	 * @param command    The command
	 * @param arguments  The arguments for <code>command</code>
	 * @param lineNumber The line number of the command
	 * @return An executable <code>Instruction</code> of the command
	 */
	private Instruction validateCommand(Command command, String[] arguments, int lineNumber) {
		switch (command) {
			case LOAD_WORD:
				Register rd1 = getRegisterFromArgument(arguments[0]);
				Integer o1 = getNumberFromArgument(arguments[1]);
				Register rs1 = getRegisterFromArgument(arguments[2]);

				if (rd1 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(o1 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (rs1 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.LOAD_WORD, new Register[]{ rd1, rs1 }, o1);
			case LOAD_IMMEDIATE:
				Register rd2 = getRegisterFromArgument(arguments[0]);
				Integer n1 = getNumberFromArgument(arguments[1]);

				if (rd2 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (n1 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				return new Instruction(Command.LOAD_IMMEDIATE, new Register[]{rd2}, n1);
			case MOVE:
				Register rd3 = getRegisterFromArgument(arguments[0]);
				Register rs2 = getRegisterFromArgument(arguments[1]);

				if (rd3 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}
				if (rs2 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				return new Instruction(Command.MOVE, new Register[]{rd3, rs2});
			case STORE_WORD:
				Register rd4 = getRegisterFromArgument(arguments[0]);
				Integer o2 = getNumberFromArgument(arguments[1]);
				Register rs3 = getRegisterFromArgument(arguments[2]);

				if (rd4 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(o2 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (rs3 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.STORE_WORD, new Register[] { rd4, rs3 }, o2);
			case ADD:
				Register rd5 = getRegisterFromArgument(arguments[0]);
				Register rs4 = getRegisterFromArgument(arguments[1]);
				Register rs5 = getRegisterFromArgument(arguments[2]);

				if (rd5 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}
				if (rs4 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}
				if (rs5 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.ADD, new Register[]{rd5, rs4, rs5});
			case SUBTRACT:
				Register rd6 = getRegisterFromArgument(arguments[0]);
				Register rs7 = getRegisterFromArgument(arguments[1]);
				Register rs8 = getRegisterFromArgument(arguments[2]);

				if (rd6 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}
				if (rs7 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}
				if (rs8 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SUBTRACT, new Register[]{rd6, rs7, rs8});
			case ADD_IMMEDIATE:
				Register rd9 = getRegisterFromArgument(arguments[0]);
				Register rs10 = getRegisterFromArgument(arguments[1]);
				Integer n2 = getNumberFromArgument(arguments[2]);

				if (rd9 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}
				if (rs10 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}
				if (n2 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.ADD_IMMEDIATE, new Register[]{rd9, rs10}, n2);
			case LOGICAL_AND:
				Register rd10 = getRegisterFromArgument(arguments[0]);
				Register rs11 = getRegisterFromArgument(arguments[1]);
				Register rs12 = getRegisterFromArgument(arguments[2]);

				if (rd10 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs11 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (rs12 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.LOGICAL_AND, new Register[]{rd10, rs11, rs12});
			case LOGICAL_OR:
				Register rd11 = getRegisterFromArgument(arguments[0]);
				Register rs13 = getRegisterFromArgument(arguments[1]);
				Register rs14 = getRegisterFromArgument(arguments[2]);

				if (rd11 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs13 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (rs14 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.LOGICAL_OR, new Register[]{rd11, rs13, rs14});
			case LOGICAL_NOR:
				Register rd12 = getRegisterFromArgument(arguments[0]);
				Register rs15 = getRegisterFromArgument(arguments[1]);
				Register rs16 = getRegisterFromArgument(arguments[2]);

				if (rd12 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs15 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (rs16 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.LOGICAL_NOR, new Register[]{rd12, rs15, rs16});
			case SHIFT_LEFT_LOGICAL:
				Register rd15 = getRegisterFromArgument(arguments[0]);
				Register rs19 = getRegisterFromArgument(arguments[1]);
				Integer n5 = getNumberFromArgument(arguments[2]);

				if (rd15 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs19 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (n5 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SHIFT_LEFT_LOGICAL, new Register[]{rd15, rs19}, n5);
			case SHIFT_RIGHT_LOGICAL:
				Register rd16 = getRegisterFromArgument(arguments[0]);
				Register rs20 = getRegisterFromArgument(arguments[1]);
				Integer n6 = getNumberFromArgument(arguments[2]);

				if (rd16 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs20 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (n6 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SHIFT_RIGHT_LOGICAL, new Register[]{rd16, rs20}, n6);
			case SET_ON_LESS_THAN:
				Register rd17 = getRegisterFromArgument(arguments[0]);
				Register rs21 = getRegisterFromArgument(arguments[1]);
				Register rs22 = getRegisterFromArgument(arguments[2]);

				if (rd17 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs21 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if (rs22 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SET_ON_LESS_THAN, new Register[]{ rd17, rs21, rs22 });
			case BRANCH_ON_EQUAL:
				Register rs24 = getRegisterFromArgument(arguments[0]);
				Register rs25 = getRegisterFromArgument(arguments[1]);
				Branch branch1 = getBranchFromArgument(arguments[2]);

				if(rs24 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs25 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(branch1 == null) {
					branch1 = createBranch(activeBranch, arguments[2], false);
				}

				return new Instruction(Command.BRANCH_ON_EQUAL, new Register[] { rs24, rs25 }, branch1);
			case BRANCH_ON_NOT_EQUAL:
				Register rs26 = getRegisterFromArgument(arguments[0]);
				Register rs27 = getRegisterFromArgument(arguments[1]);
				Branch branch2 = getBranchFromArgument(arguments[2]);

				if(rs26 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs27 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(branch2 == null) {
					branch2 = createBranch(activeBranch, arguments[2], false);
				}

				return new Instruction(Command.BRANCH_ON_NOT_EQUAL, new Register[] { rs26, rs27 }, branch2);
			case BRANCH_ON_GREATER_THAN_OR_EQUAL:
				Register rs28 = getRegisterFromArgument(arguments[0]);
				Register rs29 = getRegisterFromArgument(arguments[1]);
				Branch branch3 = getBranchFromArgument(arguments[2]);

				if(rs28 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs29 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(branch3 == null) {
					branch3 = createBranch(activeBranch, arguments[2], false);
				}

				return new Instruction(Command.BRANCH_ON_GREATER_THAN_OR_EQUAL, new Register[] { rs28, rs29 }, branch3);
			case BRANCH_ON_LESS_THAN:
				Register rs30 = getRegisterFromArgument(arguments[0]);
				Register rs31 = getRegisterFromArgument(arguments[1]);
				Branch branch4 = getBranchFromArgument(arguments[2]);

				if(rs30 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if (rs31 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(branch4 == null) {
					branch4 = createBranch(activeBranch, arguments[2], false);
				}

				return new Instruction(Command.BRANCH_ON_LESS_THAN, new Register[] { rs30, rs31 }, branch4);
			case JUMP:
				Branch branch5 = getBranchFromArgument(arguments[0]);

				if(branch5 == null) {
					branch5 = createBranch(activeBranch, arguments[0], false);
				}

				return new Instruction(Command.JUMP, branch5);
			case JUMP_REGISTER:
				Register rd18 = getRegisterFromArgument(arguments[0]);

				if(rd18 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				return new Instruction(Command.JUMP_REGISTER, new Register[] { rd18 });
			case JUMP_AND_LINK:
				Branch branch6 = getBranchFromArgument(arguments[0]);

				if(branch6 == null) {
					branch6 = createBranch(activeBranch, arguments[0], false);
				}

				return new Instruction(Command.JUMP_AND_LINK, branch6);
			case MOVE_FROM_HI:
				Register rd20 = getRegisterFromArgument(arguments[0]);

				if(rd20 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				return new Instruction(Command.MOVE_FROM_HI, new Register[] { rd20 });
			case MOVE_FROM_LO:
				Register rd21 = getRegisterFromArgument(arguments[0]);

				if(rd21 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				return new Instruction(Command.MOVE_FROM_LO, new Register[] { rd21 });
			case MULTIPLY:
				Register rs32 = getRegisterFromArgument(arguments[0]);
				Register rs33 = getRegisterFromArgument(arguments[1]);

				if(rs32 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs33 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				return new Instruction(Command.MULTIPLY, new Register[] { rs32, rs33 });
			case DIVIDE:
				Register rs34 = getRegisterFromArgument(arguments[0]);
				Register rs35 = getRegisterFromArgument(arguments[1]);

				if(rs34 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs35 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				return new Instruction(Command.DIVIDE, new Register[] { rs34, rs35 });
			case SET_ON_LESS_THAN_IMMEDIATE:
				Register rd25 = getRegisterFromArgument(arguments[0]);
				Register rs36 = getRegisterFromArgument(arguments[1]);
				Integer n8 = getNumberFromArgument(arguments[2]);

				if(rd25 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs36 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(n8 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SET_ON_LESS_THAN_IMMEDIATE, new Register[] { rd25, rs36 }, n8);
			case AND_IMMEDIATE:
				Register rd26 = getRegisterFromArgument(arguments[0]);
				Register rs37 = getRegisterFromArgument(arguments[1]);
				Integer n9 = getNumberFromArgument(arguments[2]);

				if(rd26 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs37 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(n9 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.AND_IMMEDIATE, new Register[] { rd26, rs37 }, n9);
			case OR_IMMEDIATE:
				Register rd27 = getRegisterFromArgument(arguments[0]);
				Register rs38 = getRegisterFromArgument(arguments[1]);
				Integer n10 = getNumberFromArgument(arguments[2]);

				if(rd27 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs38 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(n10 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.OR_IMMEDIATE, new Register[] { rd27, rs38 }, n10);
			case SET_ON_LESS_THAN_UNSIGNED:
				Register rd28 = getRegisterFromArgument(arguments[0]);
				Register rs39 = getRegisterFromArgument(arguments[1]);
				Register rs40 = getRegisterFromArgument(arguments[2]);

				if(rd28 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs39 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(rs40 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SET_ON_LESS_THAN_UNSIGNED, new Register[] { rd28, rs39, rs40});
			case SET_ON_LESS_THAN_UNSIGNED_IMMEDIATE:
				Register rd29 = getRegisterFromArgument(arguments[0]);
				Register rs41 = getRegisterFromArgument(arguments[1]);
				Integer n11 = getNumberFromArgument(arguments[2]);

				if(rd29 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs41 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(n11 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SET_ON_LESS_THAN_UNSIGNED_IMMEDIATE, new Register[] { rd29, rs41 }, n11);
			case LOAD_ADDRESS:
				Register rd30 = getRegisterFromArgument(arguments[0]);
				Symbol symbol = getSymbolFromArgument(arguments[1]);

				if(rd30 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(symbol == null) {
					throw new SymbolNotExistError(lineNumber, arguments[1]);
				}

				return new Instruction(Command.LOAD_ADDRESS, new Register[] { rd30 }, symbol);
			case ADD_UNSIGNED:
				Register rd31 = getRegisterFromArgument(arguments[0]);
				Register rs42 = getRegisterFromArgument(arguments[1]);
				Register rs43 = getRegisterFromArgument(arguments[2]);

				if(rd31 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs42 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(rs43 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.ADD_UNSIGNED, new Register[] { rd31, rs42, rs43 });
			case SUBTRACT_UNSIGNED:
				Register rd32 = getRegisterFromArgument(arguments[0]);
				Register rs44 = getRegisterFromArgument(arguments[1]);
				Register rs45 = getRegisterFromArgument(arguments[2]);

				if(rd32 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs44 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(rs45 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.SUBTRACT_UNSIGNED, new Register[] { rd32, rs44, rs45 });
			case ADD_IMMEDIATE_UNSIGNED:
				Register rd33 = getRegisterFromArgument(arguments[0]);
				Register rs46 = getRegisterFromArgument(arguments[1]);
				Integer n12 = getNumberFromArgument(arguments[2]);

				if(rd33 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs46 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(n12 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.ADD_IMMEDIATE_UNSIGNED, new Register[] { rd33, rs46 }, n12);
			case MULTIPLY_UNSIGNED:
				Register rs47 = getRegisterFromArgument(arguments[0]);
				Register rs48 = getRegisterFromArgument(arguments[1]);

				if(rs47 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs48 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				return new Instruction(Command.MULTIPLY_UNSIGNED, new Register[] { rs47, rs48 });
			case DIVIDE_UNSIGNED:
				Register rs49 = getRegisterFromArgument(arguments[0]);
				Register rs50 = getRegisterFromArgument(arguments[1]);

				if(rs49 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs50 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				return new Instruction(Command.DIVIDE_UNSIGNED, new Register[] { rs49, rs50 });
			case SYSCALL:
				return new Instruction(Command.SYSCALL);
			case MULTIPLY_NORMAL:
				Register rd34 = getRegisterFromArgument(arguments[0]);
				Register rs51 = getRegisterFromArgument(arguments[1]);
				Register rs52 = getRegisterFromArgument(arguments[2]);

				if(rd34 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[0], command.getName());
				}

				if(rs51 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[1], command.getName());
				}

				if(rs52 == null) {
					throw new ImproperArgumentError(lineNumber, arguments[2], command.getName());
				}

				return new Instruction(Command.MULTIPLY_NORMAL, new Register[] { rd34, rs51, rs52 });
		}

		return null;
	}

	/**
	 * Validates a command and its arguments.
	 * Converts to a parameterized <code>Instruction</code>
	 * for sequencing.
	 *
	 * @param command The command
	 * @param arguments The arguments
	 * @param lineNumber The line number of the command
	 * @return An executable <code>Instruction</code> of the command
	 */
	private Instruction validateCommand2(Command command, String[] arguments, int lineNumber) {
		Optional<Register> reg1 = Optional.empty();
		Optional<Register> reg2 = Optional.empty();
		Optional<Register> reg3 = Optional.empty();

		if(arguments.length >= 1) {
			reg1 = Optional.ofNullable(getRegisterFromArgument(arguments[0]));
		}

		if(arguments.length >= 2) {
			reg2 = Optional.ofNullable(getRegisterFromArgument(arguments[1]));
		}

		if(arguments.length == 3) {
			reg3 = Optional.ofNullable(getRegisterFromArgument(arguments[2]));
		}

		Optional<Integer> num1 = Optional.empty();
		Optional<Integer> num2 = Optional.empty();

		if(arguments.length >= 2) {
			num1 = Optional.ofNullable(getNumberFromArgument(arguments[1]));
		}

		if(arguments.length == 3) {
			num2 = Optional.ofNullable(getNumberFromArgument(arguments[2]));
		}

		Optional<Symbol> symbol1 = Optional.empty();

		if(arguments.length >= 2) {
			symbol1 = Optional.ofNullable(getSymbolFromArgument(arguments[1]));
		}

		Optional<Branch> branch1 = Optional.empty();
		Optional<String> branchName1 = Optional.empty();

		if(arguments.length >= 1) {
			branch1 = Optional.ofNullable(getBranchFromArgument(arguments[0]));
			branchName1 = Optional.ofNullable(arguments[0]);
		}

		Optional<Branch> branch2;
		Optional<String> branchName2;

		if(arguments.length == 3) {
			branch2 = Optional.ofNullable(getBranchFromArgument(arguments[2]));
			branchName2 = Optional.ofNullable(arguments[2]);
		} else {
			branchName2 = Optional.empty();
			branch2 = Optional.empty();
		}

		Register[] registerArray = new Register[3];
		registerArray[0] = reg1.orElse(null);
		registerArray[1] = reg2.orElse(null);
		registerArray[2] = reg3.orElse(null);

		Optional<Integer> finalNum = num2;
		Integer num = num1.orElseGet(() -> finalNum.orElse(null));
		Symbol symbol = symbol1.orElse(null);
		Branch b1 = branch1.orElseGet(() -> branch2.orElse(null));

		if(num == null) {
			num = Integer.MIN_VALUE;
		}

		if(b1 == null) {
			if(branchName1.isPresent() || branchName2.isPresent()) {
				String bn1 =  branchName1.orElse(null);
				String bn2 = branchName2.orElse(null);

				if(bn1 != null && Character.isLetter(bn1.charAt(0))) {
					b1 = createBranch(activeBranch, bn1, false);
				} else if(bn2 != null && Character.isLetter(bn2.charAt(0))) {
					b1 = createBranch(activeBranch, bn2, false);
				}
			}
		}

		return new Instruction(command, registerArray, num, b1, symbol);
	}

	/**
	 * Gets a <code>Branch</code> from an argument
	 *
	 * @param argument The String argument
	 * @return The <code>Branch</code> of the same name or <code>null</code>
	 */
	private Branch getBranchFromArgument(String argument) {
		for(Branch branch : branches) {
			if(branch.getName().equals(argument)) {
				return branch;
			}
		}

		return null;
	}

	/**
	 * If a <code>Symbol</code> of a certain
	 * identifier already exists.
	 *
	 * @param symbol A String identifier
	 * @return If a <code>Symbol</code> exists
	 * 				 with a matching identifier
	 */
	private boolean symbolExists(String symbol) {
		for(Symbol s : symbols) {
			if(s.getName().equals(symbol)) {
				return true;
			}
		}

		return false;
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
		} else {
			if(argument.contains("$")) {
				int start = argument.indexOf('$') + 1;
				int end = start;

				for(int i = start; i < argument.length(); i++) {
					if(!Character.isLetterOrDigit(argument.charAt(i))) {
						end = i;
						break;
					}
				}

				return Registers.getFromString(argument.substring(start, end));
			}
		}

		return null;
	}

	/**
	 * Gets a number from an argument.
	 *
	 * @param argument The string argument
	 * @return The integer equivalent of <code>argument</code>
	 */
	private Integer getNumberFromArgument(String argument) {
		try {
			return Integer.parseInt(argument);
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * Strips parentheses from offset arguments
	 *
	 * @param argument A string
	 * @return <code>argument</code> with parentheses removed
	 *
	 */
	private String cleanOffsetArgument(String argument) {
		StringBuilder builder = new StringBuilder();

		for (Character c : argument.toCharArray()) {
			if (c == '(' || c == ')') {
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

		for (String token : tokens) {
			if (token.startsWith("#")) {
				break;
			}

			if (!token.isEmpty()) {
				list.add(token);
			}
		}

		String[] result = new String[list.size()];

		for (int i = 0; i < list.size(); i++) {
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

	@Override
	public String toString() {
		return String.valueOf(branches) +
						'\n' + instructions + '\n' + symbols;
	}
}
