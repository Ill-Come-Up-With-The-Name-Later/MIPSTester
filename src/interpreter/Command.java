package interpreter;

import util.BinaryConversion;

/**
 * The various supported commands.
 */
public enum Command {
	LOAD_WORD("lw") {
		public void run(Register destination, int offset, Register source) {
			destination.setValues(Memory.GLOBAL_MEMORY
							.getWord(source.getIntegerOfValues() + offset).getValues());
		}
	},

	STORE_WORD("sw") {
		public void run(Register source, int offset, Register destinationAddress) {
			Memory.GLOBAL_MEMORY.setWord(source.toWord(), destinationAddress.getIntegerOfValues() + offset);
		}
	},

	SHIFT_LEFT_LOGICAL("sll") {
		public void run(Register destination, Register source, int shiftAmount) {
			int val = source.getIntegerOfValues();
			int shifted = val << shiftAmount;

			destination.storeStringNum(BinaryConversion.intToBinary(shifted));
		}
	},

	SHIFT_RIGHT_LOGICAL("srl") {
		public void run(Register destination, Register source, int shiftAmount) {
			int val = source.getIntegerOfValues();
			int shifted = val >> shiftAmount;

			destination.storeStringNum(BinaryConversion.intToBinary(shifted));
		}
	},

	ADD("add") {
		public void run(Register destination, Register r1, Register r2) {
			int sum = r1.getIntegerOfValues() + r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},
	SUBTRACT("sub") {
		public void run(Register destination, Register r1, Register r2) {
			int difference = r1.getIntegerOfValues() - r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(difference));
		}
	},

	ADD_IMMEDIATE("addi") {
		public void run(Register destination, Register r1, int num) {
			int sum = r1.getIntegerOfValues() + num;
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	BRANCH_ON_EQUAL("beq") {
		public void run() {

		}
	},

	BRANCH_ON_NOT_EQUAL("bne") {
		public void run() {

		}
	},

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
