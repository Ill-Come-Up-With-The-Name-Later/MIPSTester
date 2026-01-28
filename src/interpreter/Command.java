package interpreter;

import misc.Memory;
import misc.Register;
import util.BinaryConversion;

/**
 * The various supported commands.
 */
public enum Command {

	/**
	 * Loads a <code>Word</code> from program memory.
	 */
	LOAD_WORD("lw") {

		@Override
		public void run(Register destination, int offset, Register source) {
			destination.setValues(Memory.GLOBAL_MEMORY
							.getWord(source.getIntegerOfValues() + offset).getValues());
		}
	},

	/**
	 * Stores a word in program memory.
	 */
	STORE_WORD("sw") {

		@Override
		public void run(Register source, int offset, Register destinationAddress) {
			Memory.GLOBAL_MEMORY.setWord(source.toWord(), destinationAddress.getIntegerOfValues() + offset);
		}
	},

	/**
	 * Adds two values and stores them.
	 */
	ADD("add") {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			int sum = r1.getIntegerOfValues() + r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Subtracts two values and stores them.
	 */
	SUBTRACT("sub") {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			int difference = r1.getIntegerOfValues() - r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(difference));
		}
	},

	/**
	 * Adds a constant and a register's value and stores the result.
	 */
	ADD_IMMEDIATE("addi") {

		@Override
		public void run(Register destination, Register r1, int num) {
			int sum = r1.getIntegerOfValues() + num;
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Stores the result of logical and between
	 * two registers.
	 */
	LOGICAL_AND("and") {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			int result = r1.getIntegerOfValues() & r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical or between two
	 * registers.
	 */
	LOGICAL_OR("or") {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			int result = r1.getIntegerOfValues() | r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical nor between
	 * two registers.
	 */
	LOGICAL_NOR("nor") {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			int result = ~(r1.getIntegerOfValues() | r2.getIntegerOfValues());
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical and between
	 * a register and a constant.
	 */
	LOGICAL_AND_IMMEDIATE("andi") {

		@Override
		public void run(Register destination, Register r1, int num) {
			int result = r1.getIntegerOfValues() & num;
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical or between
	 * a register and a constant.
	 */
	LOGICAL_OR_IMMEDIATE("ori") {

		@Override
		public void run(Register destination, Register r1, int num) {
			int result = r1.getIntegerOfValues() | num;
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Bitwise left shifts a value.
	 */
	SHIFT_LEFT_LOGICAL("sll") {

		@Override
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

		@Override
		public void run(Register destination, Register source, int shiftAmount) {
			int val = source.getIntegerOfValues();
			int shifted = val >> shiftAmount;

			destination.storeStringNum(BinaryConversion.intToBinary(shifted));
		}
	},

	/**
	 * Sets the value of a register to 1 if the second
	 * provided register's value is less than the third
	 * provided register's value.
	 */
	SET_ON_LESS_THAN("slt") {

		@Override
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

		@Override
		public void run(Register r1, Register r2, Branch branch) {

		}

		@Override
		public void run(Register r1, Register r2, int relativeBranch) {

		}
	},

	/**
	 * Jumps to a branch when two registers' values
	 * are not equal.
	 */
	BRANCH_ON_NOT_EQUAL("bne") {

		@Override
		public void run(Register r1, Register r2, Branch branch) {

		}

		@Override
		public void run(Register r1, Register r2, int relativeBranch) {

		}
	},

	/**
	 * Jumps to a branch.
	 */
	JUMP("j") {
		@Override
		public void run(Branch branch) {

		}

		@Override
		public void run(int relativeBranch) {

		}
	},

	/**
	 * Jumps to register for returning.
	 */
	JUMP_REGISTER("jr") {

		@Override
		public void run(Register destination) {

		}
	},

	/**
	 * Jumps to an instruction and retains
	 * the original jump location.
	 */
	JUMP_AND_LINK("jal") {

		@Override
		public void run(int relativeBranch) {

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

	public void run(Register destination, Register r1, Register r2) {
		throw new UnsupportedOperationException();
	}

	public void run(Register destination, Register r1, int relativeBranch) {
		throw new UnsupportedOperationException();
	}

	public void run(Register destination) {
		throw new UnsupportedOperationException();
	}

	public void run(Register r1, Register r2, Branch branch) {
		throw new UnsupportedOperationException();
	}

	public void run(Branch branch) {
		throw new UnsupportedOperationException();
	}

	public void run(int relativeBranch) {
		throw new UnsupportedOperationException();
	}

	public void run(Register destination, int offset, Register destinationAddress) {
		throw new UnsupportedOperationException();
	}
}
