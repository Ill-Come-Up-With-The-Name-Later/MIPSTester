package program;

import interpreter.errors.BranchNotExistError;
import interpreter.errors.MemoryError;
import interpreter.file.FileParser;
import interpreter.instructions.Branch;
import interpreter.instructions.Command;
import interpreter.instructions.Instruction;
import interpreter.variables.Symbol;
import misc.Memory;
import misc.Register;
import misc.Registers;
import misc.Word;
import util.BinaryConversion;
import util.MathHelper;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * The actual program itself, with the branches,
 * symbols, and instructions.
 */
public class Program {

	public static final Program MAIN_PROGRAM = new Program();

	private ArrayList<Branch> branches = new ArrayList<>();
	private ArrayList<Instruction> instructions = new ArrayList<>();
	private ArrayList<Symbol> symbols = new ArrayList<>();
	private final HashMap<Symbol, Integer[]> symbolAddresses = new HashMap<>();
	private int programCounter = 0;
	private Branch currentBranch;

	private Program() {

	}

	/**
	 * Allocates user-defined symbols into
	 * memory.
	 */
	public void allocateSymbols() {
		int startIndex = Memory.GLOBAL_MEMORY.size() / 8;
		Registers.gp.storeNum(startIndex);

		for(Symbol symbol : symbols) {
			int[] addresses = null;

			switch(symbol.getType()) {
				case SPACE:
					addresses = Memory.GLOBAL_MEMORY.findAvailableMemory(Integer.parseInt(symbol.getValue()), startIndex);
					break;
				case ASCII:
					String val = String.valueOf(symbol.getValue());
					int binaryLength = MathHelper.binaryLength(val);
					addresses = Memory.GLOBAL_MEMORY.findAvailableMemory(binaryLength, startIndex);
					break;
				default:
					addresses = Memory.GLOBAL_MEMORY.findAvailableMemory(4, startIndex);
					break;
			}

			if(addresses == null) {
				throw new MemoryError(symbol);
			}

			switch(symbol.getType()) {
				case WORD:
					Word w1 = new Word();
					int num = Integer.parseInt(symbol.getValue());
					w1.storeStringNum(BinaryConversion.intToBinary(num));
					Memory.GLOBAL_MEMORY.setWord(w1, addresses[0]);

					break;
				case ASCII:
					String val = String.valueOf(symbol.getValue());
					String[] binary = BinaryConversion.stringToCompressedBinary(val);

					int index = 0;
					for(int i = addresses[0]; i < addresses[1]; i += 4) {
						Word w2 = new Word();
						w2.storeStringNum(binary[index]);
						Memory.GLOBAL_MEMORY.setWord(w2, i);
						index++;
					}

					break;
				case SPACE:
					int start = addresses[0];
					int end = addresses[1];

					for(int i = start; i < end; i += 4) {
						Word w3 = new Word(true);
						Memory.GLOBAL_MEMORY.setWord(w3, i);
					}

					break;
			}

			// Allocating this prevents some weird behaviors related to
			// null-terminated values like Strings
			Word filler = new Word(true);
			Memory.GLOBAL_MEMORY.setWord(filler, addresses[1] + 4);
			startIndex = addresses[1] + 4;

			symbolAddresses.put(symbol, new Integer[] { addresses[0], addresses[1] });
		}
	}

	/**
	 * Runs the program.
	 *
	 */
	public void run() {
		run(0, instructions.size() * 4);
	}

	/**
	 * Runs the program from
	 * a set start to a set end.
	 *
	 * @param startPoint The starting line of the program
	 * @param endPoint The ending line of the program
	 */
	public void run(int startPoint, int endPoint) {
		if(startPoint > endPoint) {
			throw new IllegalArgumentException("Cannot start after end.");
		}

		if(startPoint % 4 != 0 || endPoint % 4 != 0) {
			throw new IllegalArgumentException("Cannot start or end on a non-multiple of 4.");
		}

		if(startPoint == 0) {
			allocateSymbols();
			Registers.sp.storeNum(Memory.STACK_MEMORY.size() - 4);
			Registers.pc.storeNum(startPoint);
			currentBranch = branches.getFirst();
		}

		while(Registers.pc.getIntegerOfValues() < endPoint) {
			Instruction instruction = getInstructionAt(Registers.pc.getIntegerOfValues());

			if(instruction.getCommand() == Command.CREATE_BRANCH) {
				Registers.pc.storeNum(Registers.pc.getIntegerOfValues() + 4);
				continue;
			}

			if(Registers.sp.getIntegerOfValues() < 0) {
				throw new StackOverflowError("Program stack overflow.");
			}

			if(instruction.getCommand() == Command.SYSCALL && (Registers.v0.getIntegerOfValues() == 10 || Registers.v0.getIntegerOfValues() == 17)) {
				return;
			}

			if(!instruction.isJump()) {
				instruction.run();
				Registers.pc.storeNum(Registers.pc.getIntegerOfValues() + 4);
			} else {
				Command command = instruction.getCommand();

				if(command == Command.JUMP || evaluateJumpCondition(instruction)) {
					Branch destination = instruction.getBranchData();
					Registers.pc.storeNum(getStartOfBranch(destination));
					currentBranch = destination;
				} else if(command == Command.JUMP_REGISTER) {
					Registers.pc.storeNum(instruction.getRegistersData()[0].getIntegerOfValues());
				} else if(command == Command.JUMP_AND_LINK) {
					Branch destination = instruction.getBranchData();
					Registers.ra.storeNum(Registers.pc.getIntegerOfValues() + 4);

					Registers.pc.storeNum(getStartOfBranch(destination));
					currentBranch = destination;
				} else {
					Registers.pc.storeNum(Registers.pc.getIntegerOfValues() + 4);
				}
			}
		}
	}

	/**
	 * Runs a single instruction.
	 */
	public void step() {
		run(Registers.pc.getIntegerOfValues(), Registers.pc.getIntegerOfValues() + 4);
	}

	/**
	 * Determines if a branch should happen based on
	 * the condition of the branching.
	 *
	 * @param instruction The branching <code>Instruction</code>
	 * @return If a branch should occur
	 */
	private boolean evaluateJumpCondition(Instruction instruction) {
		switch(instruction.getCommand()) {
			case BRANCH_ON_EQUAL:
				Register r1 = instruction.getRegistersData()[0];
				Register r2 = instruction.getRegistersData()[1];

				return r1.getIntegerOfValues() == r2.getIntegerOfValues();
			case BRANCH_ON_NOT_EQUAL:
				Register r3 = instruction.getRegistersData()[0];
				Register r4 = instruction.getRegistersData()[1];

				return r3.getIntegerOfValues() != r4.getIntegerOfValues();
			case BRANCH_ON_GREATER_THAN_OR_EQUAL:
				Register r5 = instruction.getRegistersData()[0];
				Register r6 = instruction.getRegistersData()[1];

				return r5.getIntegerOfValues() >= r6.getIntegerOfValues();
			case BRANCH_ON_LESS_THAN:
				Register r7 = instruction.getRegistersData()[0];
				Register r8 = instruction.getRegistersData()[1];

				return r7.getIntegerOfValues() < r8.getIntegerOfValues();
			case BRANCH_ON_GREATER_THAN:
					Register r9 = instruction.getRegistersData()[0];
					Register r10 = instruction.getRegistersData()[1];

					return r9.getIntegerOfValues() > r10.getIntegerOfValues();
		}

		return false;
	}

	/**
	 * Gets the start index of a <code>Branch</code>.
	 *
	 * @param branch A <code>Branch</code>
	 * @return The start index of <code>branch</code>
	 */
	public int getStartOfBranch(Branch branch) {
		for(int i = 0; i < instructions.size(); i++) {
			if(instructions.get(i).getCommand() == Command.CREATE_BRANCH) {
				if(instructions.get(i).getBranchData() == branch) {
					return i * 4;
				}
			}
		}

		throw new BranchNotExistError(branch);
	}

	/**
	 * Gets the <code>Instruction</code> at an address.
	 * The address must be divisible by 4.
	 *
	 * @param address An integer address
	 * @return The <code>Instruction</code> at
	 * 				 <code>address</code>
	 */
	private Instruction getInstructionAt(int address) {
		if(address % 4 != 0) {
			throw new IllegalArgumentException("Address must be a multiple of 4");
		}

		if(address >= instructions.size() * 4 || address < 0) {
			throw new IndexOutOfBoundsException("Address out of bounds");
		}

		int trueAddress = address / 4;
		return instructions.get(trueAddress);
	}

	/**
	 * Gets the program's output. Contains
	 * a log of the memory, stack, and the registers.
	 *
	 * @return The log of the memory, stack, and registers
	 */
	public ArrayList<String> getOutput() {
		ArrayList<String> lines = new ArrayList<>();

		lines.add("- Memory -\n");
		lines.add(Memory.GLOBAL_MEMORY.toString());
		lines.add("");

		lines.add("- Stack -\n");
		lines.add(Memory.STACK_MEMORY.toString());
		lines.add("");

		lines.add("- Registers -\n");
		for(Register register : Registers.REGISTERS) {
			lines.add(register.toString() + "\n");
		}

		return lines;
	}

	/**
	 * Writes the program memory and registers to a file.
	 *
	 * @param path The path of the output file
	 */
	public void outputToFile(String path) {
		File file = new File(path);
		FileWriter fw;

		try {
			fw = new FileWriter(file);

			for(String line : getOutput()) {
				fw.write(line);
			}

			fw.close();

			System.out.println("File written to: " + path);
		} catch (IOException e) {
			System.out.println("File not found");
		}
	}

	public ArrayList<Branch> getBranches() {
		return branches;
	}

	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}

	public ArrayList<Symbol> getSymbols() {
		return symbols;
	}

	public void setBranches(ArrayList<Branch> branches) {
		this.branches = branches;
	}

	public void setInstructions(ArrayList<Instruction> instructions) {
		this.instructions = instructions;
	}

	public void setSymbols(ArrayList<Symbol> symbols) {
		this.symbols = symbols;
	}

	public HashMap<Symbol, Integer[]> getSymbolAddresses() {
		return symbolAddresses;
	}

	public int getProgramCounter() {
		return programCounter;
	}

	public void setProgramCounter(int programCounter) {
		this.programCounter = programCounter;
	}

	public Branch getCurrentBranch() {
		return currentBranch;
	}

	/**
	 * Resets the program.
	 */
	public void reset() {
		Memory.GLOBAL_MEMORY.clear();
		Memory.STACK_MEMORY.clear();

		FileParser.GLOBAL.reset();
		Registers.reset();

		instructions.clear();
		symbols.clear();
		symbolAddresses.clear();
		branches.clear();

		programCounter = 0;
		currentBranch = null;
	}
}
