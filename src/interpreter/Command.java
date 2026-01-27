package interpreter;

import util.BinaryConversion;

/**
 * The various supported commands.
 */
public enum Command {

	/**
	 * Loads a <code>Word</code> from program memory.
	 */
	LOAD_WORD("lw") {
		public void run(Register destination, int offset, Register source) {
			destination.setValues(Memory.GLOBAL_MEMORY
							.getWord(source.getIntegerOfValues() + offset).getValues());
		}
	},

	/**
	 * Stores a word in program memory.
	 */
	STORE_WORD("sw") {
		public void run(Register source, int offset, Register destinationAddress) {
			Memory.GLOBAL_MEMORY.setWord(source.toWord(), destinationAddress.getIntegerOfValues() + offset);
		}
	},

	/**
	 * Bitwise left shifts a value.
	 */
	SHIFT_LEFT_LOGICAL("sll") {
		public void run(Register destination, Register source, int shiftAmount) {
			int val = source.getIntegerOfValues();
			int shifted = val << shiftAmount;

			destination.storeStringNum(BinaryConversion.intToBinary(shifted));
		}
	},

	/**
	 * Bitwise right shifts a value.
	 */
	SHIFT_RIGHT_LOGICAL("srl") {
		public void run(Register destination, Register source, int shiftAmount) {
			int val = source.getIntegerOfValues();
			int shifted = val >> shiftAmount;

			destination.storeStringNum(BinaryConversion.intToBinary(shifted));
		}
	},

	/**
	 * Adds two values and stores them.
	 */
	ADD("add") {
		public void run(Register destination, Register r1, Register r2) {
			int sum = r1.getIntegerOfValues() + r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Subtracts two values and stores them.
	 */
	SUBTRACT("sub") {
		public void run(Register destination, Register r1, Register r2) {
			int difference = r1.getIntegerOfValues() - r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(difference));
		}
	},

	/**
	 * Adds a constant and a register's value and stores the result.
	 */
	ADD_IMMEDIATE("addi") {
		public void run(Register destination, Register r1, int num) {
			int sum = r1.getIntegerOfValues() + num;
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Sets the value of a register to 1 if the second
	 * provided register's value is less than the third
	 * provided register's value.
	 */
	SET_ON_LESS_THAN("slt") {
		public void run(Register result, Register r1, Register r2) {
			int num1 = r1.getIntegerOfValues();
			int num2 = r2.getIntegerOfValues();

			if(num1 < num2) {
				result.storeStringNum(BinaryConversion.intToBinary(1));
			} else {
				result.storeStringNum(BinaryConversion.intToBinary(0));
			}
		}
	},

	/**
	 * Jumps to a branch when two registers' values
	 * are equal.
	 */
	BRANCH_ON_EQUAL("beq") {
		public void run() {

		}
	},

	/**
	 * Jumps to a branch when two registers' values
	 * are not equal.
	 */
	BRANCH_ON_NOT_EQUAL("bne") {
		public void run() {

		}
	},

	/**
	 * Jumps to a branch.
	 */
	JUMP("j") {
		public void run() {

		}
	},
	;

	private final String name;

	Command(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public static Command getCommand(String name) {
		for(Command command : Command.values()) {
			if(command.getName().equals(name)) {
				return command;
			}
		}

		return null;
	}
}
