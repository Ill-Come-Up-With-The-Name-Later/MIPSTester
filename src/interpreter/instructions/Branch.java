package interpreter.instructions;

import java.util.ArrayList;

/**
 * A branch of a program. Really only needed if
 * the program has function calls or other types
 * of branching such as looping or conditionals.
 */
public class Branch {

	private Branch previous;
	private String name;
	private final ArrayList<Instruction> instructions;

	public Branch() {
		this.instructions = new ArrayList<>();
		this.name = "";
	}

	public Branch(Branch previous) {
		this();
		this.previous = previous;
	}

	public Branch(Branch previous, String name) {
		this(previous);
		this.name = name;
	}

	public void addInstruction(Instruction instruction) {
		instructions.add(instruction);
	}

	public ArrayList<Instruction> getInstructions() {
		return instructions;
	}

	public Branch getPrevious() {
		return previous;
	}

	public void setPrevious(Branch previous) {
		this.previous = previous;
	}

	public String getName() {
		return name;
	}

	/**
	 * Executes all instructions in this
	 * <code>Branch</code>.
	 */
	public void execute() {
		instructions.forEach(Instruction::run);
	}

	@Override
	public String toString() {
		return name;
	}
}
