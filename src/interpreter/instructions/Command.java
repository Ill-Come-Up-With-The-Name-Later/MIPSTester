package interpreter.instructions;

import interpreter.errors.IllegalModificationError;
import interpreter.errors.SymbolNotExistError;
import interpreter.variables.Symbol;
import misc.Memory;
import misc.Register;
import misc.Registers;
import program.Program;
import util.BinaryConversion;
import util.MathHelper;

/**
 * The various supported commands.
 */
public enum Command {

	/**
	 * Loads a <code>Word</code> from program memory.
	 */
	LOAD_WORD("lw", 3) {

		@Override
		public void run(Register destination, int offset, Register source) {
			super.run(destination, offset, source);
			if(source == Registers.sp) {
				destination.setValues(Memory.STACK_MEMORY.getWord(source.getIntegerOfValues() +
								offset).getValues().clone());
				return;
			}

			destination.setValues(Memory.GLOBAL_MEMORY
							.getWord(source.getIntegerOfValues() + offset).getValues().clone());
		}
	},

	/**
	 * Loads the start address of a symbol into
	 * a register.
	 */
	LOAD_ADDRESS("la", 2) {

		@Override
		public void run(Register destination, Symbol symbol) {
			super.run(destination, symbol);

			destination.storeNum(Program.MAIN_PROGRAM.getSymbolAddresses().get(symbol)[0]);
		}
	},

	/**
	 * Loads a constant into a register
	 */
	LOAD_IMMEDIATE("li", 2) {

		@Override
		public void run(Register destination, int num) {
			super.run(destination, num);
			destination.storeStringNum(BinaryConversion.intToBinary(num));
		}
	},

	/**
	 * Moves a value from one register to another.
	 */
	MOVE("move", 2) {

		@Override
		public void run(Register destination, Register source) {
			destination.storeStringNum(BinaryConversion.intToBinary(source.getIntegerOfValues()));
		}
	},

	/**
	 * Copies the value from the hi register.
	 */
	MOVE_FROM_HI("mfhi", 1) {

		@Override
		public void run(Register destination) {
			super.run(destination);
			destination.storeStringNum(Registers.hi.getValueString());
		}
	},

	/**
	 * Copies the value from the lo register.
	 */
	MOVE_FROM_LO("mflo", 1) {

		@Override
		public void run(Register destination) {
			super.run(destination);
			destination.storeStringNum(Registers.lo.getValueString());
		}
	},

	/**
	 * Stores a word in program memory.
	 */
	STORE_WORD("sw", 3) {

		@Override
		public void run(Register source, int offset, Register destinationAddress) {
			super.run(source, offset, destinationAddress);
			if(destinationAddress == Registers.sp) {
				Memory.STACK_MEMORY.setWord(source.toWord(), destinationAddress.getIntegerOfValues() + offset);
				return;
			}

			Memory.GLOBAL_MEMORY.setWord(source.toWord(), destinationAddress.getIntegerOfValues() + offset);
		}
	},

	/**
	 * Adds two values and stores the result.
	 */
	ADD("add", 3) {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			super.run(destination, r1, r2);
			int sum = r1.getIntegerOfValues() + r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Adds two unsigned values and stores the result.
	 */
	ADD_UNSIGNED("addu", 3) {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			super.run(destination, r1, r2);
			int sum = (int)(MathHelper.toUnsigned(r1.getIntegerOfValues()) + MathHelper.toUnsigned(r2.getIntegerOfValues()));
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Subtracts two values and stores the result.
	 */
	SUBTRACT("sub", 3) {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			super.run(destination, r1, r2);
			int difference = r1.getIntegerOfValues() - r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(difference));
		}
	},

	/**
	 * Subtracts two unsigned values and stores the result.
	 */
	SUBTRACT_UNSIGNED("subu", 3) {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			super.run(destination, r1, r2);
			int sum = (int)(MathHelper.toUnsigned(r1.getIntegerOfValues()) - MathHelper.toUnsigned(r2.getIntegerOfValues()));
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Multiplies two numbers and stores the result into
	 * hi and lo registers.
	 */
	MULTIPLY("mult", 2) {

		@Override
		public void run(Register r1, Register r2) {
			super.run(r1, r2);
			long product = MathHelper.multiply(r1.getIntegerOfValues(), r2.getIntegerOfValues());
			String binary = BinaryConversion.longToBinary(product);
			String[] splitBinary = BinaryConversion.split64BitBinary(binary);

			Registers.hi.storeStringNum(splitBinary[0]);
			Registers.lo.storeStringNum(splitBinary[1]);
		}
	},

	/**
	 * Multiplies two unsigned numbers and stores the result into
	 * hi and lo registers.
	 */
	MULTIPLY_UNSIGNED("multu", 2) {

		@Override
		public void run(Register r1, Register r2) {
			super.run(r1, r2);
			long product = MathHelper.multiply((int)MathHelper.toUnsigned(r1.getIntegerOfValues()),
							(int)MathHelper.toUnsigned(r2.getIntegerOfValues()));
			String binary = BinaryConversion.longToBinary(product);
			String[] splitBinary = BinaryConversion.split64BitBinary(binary);

			Registers.hi.storeStringNum(splitBinary[0]);
			Registers.lo.storeStringNum(splitBinary[1]);
		}
	},

	/**
	 * Divides two numbers. Stores the remainder into hi
	 * and the quotient into lo.
	 */
	DIVIDE("div", 2) {
		@Override
		public void run(Register r1, Register r2) {
			super.run(r1, r2);

			int quotient = r1.getIntegerOfValues() / r2.getIntegerOfValues();
			int remainder = r1.getIntegerOfValues() % r2.getIntegerOfValues();

			String binaryQuotient = BinaryConversion.intToBinary(quotient);
			String binaryRemainder = BinaryConversion.intToBinary(remainder);

			Registers.lo.storeStringNum(binaryQuotient);
			Registers.hi.storeStringNum(binaryRemainder);
		}
	},

	/**
	 * Divides two unsigned numbers. Stores the remainder into hi
	 * and the quotient into lo.
	 */
	DIVIDE_UNSIGNED("divu", 2) {
		@Override
		public void run(Register r1, Register r2) {
			super.run(r1, r2);

			int quotient = (int)MathHelper.toUnsigned(r1.getIntegerOfValues()) / (int)MathHelper.toUnsigned(r2.getIntegerOfValues());
			int remainder = (int)MathHelper.toUnsigned(r1.getIntegerOfValues()) % (int)MathHelper.toUnsigned((r2.getIntegerOfValues()));

			String binaryQuotient = BinaryConversion.intToBinary(quotient);
			String binaryRemainder = BinaryConversion.intToBinary(remainder);

			Registers.lo.storeStringNum(binaryQuotient);
			Registers.hi.storeStringNum(binaryRemainder);
		}
	},

	/**
	 * Adds a constant and a register's value and stores the result.
	 */
	ADD_IMMEDIATE("addi", 3) {

		@Override
		public void run(Register destination, Register r1, int num) {
			super.run(destination, r1, num);
			int sum = r1.getIntegerOfValues() + num;
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Adds a constant and a register's value and stores the result.
	 */
	ADD_IMMEDIATE_UNSIGNED("addiu", 3) {

		@Override
		public void run(Register destination, Register r1, int num) {
			super.run(destination, r1, num);
			int sum = (int)(MathHelper.toUnsigned(r1.getIntegerOfValues()) + MathHelper.toUnsigned(num));
			destination.storeStringNum(BinaryConversion.intToBinary(sum));
		}
	},

	/**
	 * Stores the result of logical and between
	 * two registers.
	 */
	LOGICAL_AND("and", 3) {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			super.run(destination, r1, r2);
			int result = r1.getIntegerOfValues() & r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Performs logical and with a register value
	 * and a constant.
	 */
	AND_IMMEDIATE("andi", 3) {

		@Override
		public void run(Register destination, Register r1, int num) {
			super.run(destination, r1, num);
			int result = r1.getIntegerOfValues() & num;
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical or between two
	 * registers.
	 */
	LOGICAL_OR("or", 3) {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			super.run(destination, r1, r2);
			int result = r1.getIntegerOfValues() | r2.getIntegerOfValues();
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Performs logical or with a register value
	 * and a constant.
	 */
	OR_IMMEDIATE("ori", 3) {

		@Override
		public void run(Register destination, Register r1, int num) {
			super.run(destination, r1, num);
			int result = r1.getIntegerOfValues() | num;
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical nor between
	 * two registers.
	 */
	LOGICAL_NOR("nor", 3) {

		@Override
		public void run(Register destination, Register r1, Register r2) {
			super.run(destination, r1, r2);
			int result = ~(r1.getIntegerOfValues() | r2.getIntegerOfValues());
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical and between
	 * a register and a constant.
	 */
	LOGICAL_AND_IMMEDIATE("andi", 3) {

		@Override
		public void run(Register destination, Register r1, int num) {
			super.run(destination, r1, num);
			int result = r1.getIntegerOfValues() & num;
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Stores the result of logical or between
	 * a register and a constant.
	 */
	LOGICAL_OR_IMMEDIATE("ori", 3) {

		@Override
		public void run(Register destination, Register r1, int num) {
			super.run(destination, r1, num);
			int result = r1.getIntegerOfValues() | num;
			destination.storeStringNum(BinaryConversion.intToBinary(result));
		}
	},

	/**
	 * Bitwise left shifts a value.
	 */
	SHIFT_LEFT_LOGICAL("sll", 3) {

		@Override
		public void run(Register destination, Register source, int shiftAmount) {
			super.run(destination, source, shiftAmount);
			int val = source.getIntegerOfValues();
			int shifted = val << shiftAmount;

			destination.storeStringNum(BinaryConversion.intToBinary(shifted));
		}
	},

	/**
	 * Bitwise right shifts a value.
	 */
	SHIFT_RIGHT_LOGICAL("srl", 3) {

		@Override
		public void run(Register destination, Register source, int shiftAmount) {
			super.run(destination, source, shiftAmount);
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
	SET_ON_LESS_THAN("slt", 3) {

		@Override
		public void run(Register result, Register r1, Register r2) {
			super.run(result, r1, r2);
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
	 * Sets the value of a register to 1 if the second
	 * provided register's value is less than the third
	 * provided register's value.
	 */
	SET_ON_LESS_THAN_UNSIGNED("sltu", 3) {

		@Override
		public void run(Register result, Register r1, Register r2) {
			super.run(result, r1, r2);

			int n1 = r1.getIntegerOfValues();
			int n2 = r2.getIntegerOfValues();

			long num1 = MathHelper.toUnsigned(n1);
			long num2 = MathHelper.toUnsigned(n2);

			if(num1 < num2) {
				result.storeStringNum(BinaryConversion.intToBinary(1));
			} else {
				result.storeStringNum(BinaryConversion.intToBinary(0));
			}
		}
	},

	/**
	 * Sets the value of a register to 1
	 * if the second provided register is less
	 * than a constant otherwise sets the register
	 * to 0.
	 */
	SET_ON_LESS_THAN_IMMEDIATE("slti", 3) {

		@Override
		public void run(Register result, Register r1, int num) {
			super.run(result, r1, num);
			int num1 = r1.getIntegerOfValues();

			if(num1 < num) {
				result.storeStringNum(BinaryConversion.intToBinary(1));
			} else {
				result.storeStringNum(BinaryConversion.intToBinary(0));
			}
		}
	},

	/**
	 * Sets the value of a register to 1
	 * if the second provided register is less
	 * than a constant otherwise sets the register
	 * to 0.
	 */
	SET_ON_LESS_THAN_UNSIGNED_IMMEDIATE("sltui", 3) {

		@Override
		public void run(Register result, Register r1, int num) {
			super.run(result, r1, num);
			int n1 = r1.getIntegerOfValues();
			long num1 = MathHelper.toUnsigned(n1);
			long num2 = MathHelper.toUnsigned(num);

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
	BRANCH_ON_EQUAL("beq", 3) {

		@Override
		public void run(Register r1, Register r2, Branch branch) {

		}
	},

	/**
	 * Jumps to a branch when two registers' values
	 * are not equal.
	 */
	BRANCH_ON_NOT_EQUAL("bne", 3) {

		@Override
		public void run(Register r1, Register r2, Branch branch) {

		}
	},

	/**
	 * Jumps to a branch if a value is greater than
	 * or equal to another.
	 */
	BRANCH_ON_GREATER_THAN_OR_EQUAL("bge", 3) {

		@Override
		public void run(Register r1, Register r2, Branch branch) {

		}
	},

	/**
	 * Jumps to a branch if a value is less than
	 * another.
	 */
	BRANCH_ON_LESS_THAN("blt", 3) {

		@Override
		public void run(Register r1, Register r2, Branch branch) {

		}
	},

	/**
	 * Jumps to a branch.
	 */
	JUMP("j", 1) {
		@Override
		public void run(Branch branch) {

		}
	},

	/**
	 * Jumps to register for returning.
	 */
	JUMP_REGISTER("jr", 1) {

		@Override
		public void run(Register destination) {

		}
	},

	/**
	 * Jumps to an instruction and retains
	 * the original jump location.
	 */
	JUMP_AND_LINK("jal", 1) {

		@Override
		public void run(Branch branch) {

		}
	},
	;

	private final String name;
	private final int argumentCount;

	Command(String name, int argumentCount) {
		this.name = name;
		this.argumentCount = argumentCount;
	}

	public String getName() {
		return name;
	}

	public int getArgumentCount() {
		return argumentCount;
	}

	public static Command getCommand(String name) {
		for(Command command : Command.values()) {
			if(command.getName().equals(name)) {
				return command;
			}
		}

		return null;
	}

	public void run(Register destination, Register r1) {
		if(Registers.READONLY_REGISTERS.contains(destination)) {
			throw new IllegalModificationError();
		}
	}

	public void run(Register destination, Register r1, Register r2) {
		if(Registers.READONLY_REGISTERS.contains(destination)) {
			throw new IllegalModificationError();
		}
	}

	public void run(Register destination, int num) {
		if(Registers.READONLY_REGISTERS.contains(destination)) {
			throw new IllegalModificationError();
		}
	}

	public void run(Register destination, Register r1, int relativeBranch) {
		if(Registers.READONLY_REGISTERS.contains(destination)) {
			throw new IllegalModificationError();
		}
	}

	public void run(Register destination) {
		if(Registers.READONLY_REGISTERS.contains(destination)) {
			throw new IllegalModificationError();
		}
	}

	public void run(Register destination, Symbol symbol) {
		if(Registers.READONLY_REGISTERS.contains(destination)) {
			throw new IllegalModificationError();
		}

		if(!Program.MAIN_PROGRAM.getSymbols().contains(symbol)) {
			throw new SymbolNotExistError();
		}
	}

	public void run(Register r1, Register r2, Branch branch) {

	}

	public void run(Branch branch) {

	}

	public void run(int relativeBranch) {

	}

	public void run(Register destination, int offset, Register destinationAddress) {
		if(Registers.READONLY_REGISTERS.contains(destination)) {
			throw new IllegalModificationError();
		}
	}
}
