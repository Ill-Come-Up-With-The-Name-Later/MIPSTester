package program;

import interpreter.errors.MemoryError;
import interpreter.instructions.Branch;
import interpreter.instructions.Instruction;
import interpreter.variables.DataType;
import interpreter.variables.Symbol;
import misc.Memory;
import misc.Word;
import util.BinaryConversion;
import util.MathHelper;

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

	private Program() {

	}

	/**
	 * Allocates user-defined symbols into
	 * memory
	 */
	public void allocateSymbols() {
		for(Symbol symbol : symbols) {
			int[] addresses = null;

			switch(symbol.getType()) {
				case SPACE:
					addresses = Memory.GLOBAL_MEMORY.findAvailableMemory(Integer.parseInt(symbol.getValue()));
					break;
				case ASCII:
					String val = String.valueOf(symbol.getValue());
					int binaryLength = MathHelper.binaryLength(val);
					addresses = Memory.GLOBAL_MEMORY.findAvailableMemory(binaryLength);
					break;
				default:
					addresses = Memory.GLOBAL_MEMORY.findAvailableMemory(4);
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
					String[] binary = BinaryConversion.stringToBinary(val);

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
						Word w2 = new Word();
						Memory.GLOBAL_MEMORY.setWord(w2, i);
					}

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

		for(Branch branch : branches) {
			for(Instruction instruction : branch.getInstructions()) {
				instruction.run();
			}
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
}
