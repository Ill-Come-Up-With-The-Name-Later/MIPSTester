package program;

import interpreter.errors.MemoryError;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Stack;

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
		int startIndex = Memory.GLOBAL_MEMORY.size() / 2;

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

					startIndex = addresses[1] + 4;
					break;
				case ASCII:
					String val = String.valueOf(symbol.getValue());
					String[] binary = BinaryConversion.stringToBinary(val);

					int index = 0;
					for(int i = addresses[0]; i < addresses[1]; i += 4) {
						Word w2 = new Word();
						w2.storeStringNum(binary[index]);
						Memory.GLOBAL_MEMORY.setWord(w2, i);
						index++;
					}

					startIndex = addresses[1] + 4;
					break;
				case SPACE:
					int start = addresses[0];
					int end = addresses[1];

					for(int i = start; i < end; i += 4) {
						Word w3 = new Word();
						Memory.GLOBAL_MEMORY.setWord(w3, i);
					}

					startIndex = addresses[1] + 4;
					break;
			}

			symbolAddresses.put(symbol, new Integer[] { addresses[0], addresses[1] });
		}
	}

	/**
	 * Runs the program.
	 */
	public void run() {
		allocateSymbols();
		Registers.sp.storeNum(Memory.STACK_MEMORY.size());
		Registers.pc.storeNum(0);
		currentBranch = branches.getFirst();

		while(Registers.pc.getIntegerOfValues() < instructions.size() * 4) {
			Instruction instruction = getInstructionAt(Registers.pc.getIntegerOfValues());

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
					Registers.pc.storeNum(getStartOfBranch(destination));
					currentBranch = destination;

					Registers.ra.storeNum(Registers.pc.getIntegerOfValues() + 4);
				}
			}
		}
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
			if(instructions.get(i) == branch.getInstructions().getFirst()) {
				return i * 4;
			}
		}

		return -1;
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
}
