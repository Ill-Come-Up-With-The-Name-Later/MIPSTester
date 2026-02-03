package interpreter.instructions;

import misc.Register;

import java.util.Arrays;

/**
 * An instruction to be executed.
 */
public class Instruction {

	private final Command command;
	private final Register[] registersData;
	private final int integerData;
	private Branch branchData;

	public Instruction() {
		this(null, new Register[3], Integer.MIN_VALUE);
	}

	public Instruction(Command command) {
		this(command, new Register[3], Integer.MIN_VALUE);
	}

	public Instruction(Command command, Register[] registersData) {
		this(command, registersData, Integer.MIN_VALUE);
	}

	public Instruction(Command command, Register[] registersData, int integerData) {
		this.command = command;
		this.registersData = registersData;
		this.integerData = integerData;
	}

	public Instruction(Command command, Register[] registersData, Branch branchData) {
		this(command, registersData, Integer.MIN_VALUE);
		this.branchData = branchData;
	}

	public Instruction(Command command, int integerData) {
		this(command, new Register[3], integerData);
	}

	public Instruction(Command command, Branch branchData) {
		this(command);
		this.branchData = branchData;
	}

	public Command getCommand() {
		return command;
	}

	public int getIntegerData() {
		return integerData;
	}

	public Branch getBranchData() {
		return branchData;
	}

	/**
	 * Gets the <code>Register</code>s associated with the command.
	 * For the purpose of storing/retrival, <code>Registers</code>
	 * will be stored in the order they are written in the
	 * instruction set.
	 *
	 * @return The <code>Registers</code> associated with this
	 * 				 <code>Instruction</code>.
	 */
	public Register[] getRegistersData() {
		return registersData;
	}

	/**
	 * Runs the <code>Command</code> in this
	 * <code>Instruction</code>.
	 */
	public void run() {
		switch(command) {
			case LOAD_WORD -> Command.LOAD_WORD.run(registersData[0], integerData, registersData[1]);
			case LOAD_IMMEDIATE -> Command.LOAD_IMMEDIATE.run(registersData[0], integerData);
			case MOVE -> Command.MOVE.run(registersData[0], registersData[1]);
			case MOVE_FROM_HI -> Command.MOVE_FROM_HI.run(registersData[0]);
			case MOVE_FROM_LO -> Command.MOVE_FROM_LO.run(registersData[0]);
			case STORE_WORD -> Command.STORE_WORD.run(registersData[0], integerData, registersData[1]);
			case ADD -> Command.ADD.run(registersData[0], registersData[1], registersData[2]);
			case SUBTRACT -> Command.SUBTRACT.run(registersData[0], registersData[1], registersData[2]);
			case MULTIPLY -> Command.MULTIPLY.run(registersData[0], registersData[1]);
			case DIVIDE -> Command.DIVIDE.run(registersData[0], registersData[1]);
			case ADD_IMMEDIATE -> Command.ADD_IMMEDIATE.run(registersData[0], registersData[1], integerData);
			case LOGICAL_AND -> Command.LOGICAL_AND.run(registersData[0], registersData[1], registersData[2]);
			case LOGICAL_OR -> Command.LOGICAL_OR.run(registersData[0], registersData[1], registersData[2]);
			case LOGICAL_NOR -> Command.LOGICAL_NOR.run(registersData[0], registersData[1], registersData[2]);
			case LOGICAL_AND_IMMEDIATE -> Command.LOGICAL_AND_IMMEDIATE.run(registersData[0], registersData[1], integerData);
			case LOGICAL_OR_IMMEDIATE -> Command.LOGICAL_OR_IMMEDIATE.run(registersData[0], registersData[1], integerData);
			case SHIFT_LEFT_LOGICAL -> Command.SHIFT_LEFT_LOGICAL.run(registersData[0], registersData[1], integerData);
			case SHIFT_RIGHT_LOGICAL -> Command.SHIFT_RIGHT_LOGICAL.run(registersData[0], registersData[1], integerData);
			case SET_ON_LESS_THAN -> Command.SET_ON_LESS_THAN.run(registersData[0], registersData[1], registersData[2]);
			case BRANCH_ON_EQUAL -> {
				if(branchData == null) {
					Command.BRANCH_ON_EQUAL.run(registersData[0], registersData[1], integerData);
				} else {
					Command.BRANCH_ON_EQUAL.run(registersData[0], registersData[1], branchData);
				}
			}
			case BRANCH_ON_NOT_EQUAL -> {
				if(branchData == null) {
					Command.BRANCH_ON_NOT_EQUAL.run(registersData[0], registersData[1], integerData);
				} else {
					Command.BRANCH_ON_NOT_EQUAL.run(registersData[0], registersData[1], branchData);
				}
			}
			case BRANCH_ON_LESS_THAN -> {
				if(branchData == null) {
					Command.BRANCH_ON_LESS_THAN.run(registersData[0], registersData[1], integerData);
				} else {
					Command.BRANCH_ON_LESS_THAN.run(registersData[0], registersData[1], branchData);
				}
			}
			case BRANCH_ON_GREATER_THAN_OR_EQUAL -> {
				if(branchData == null) {
					Command.BRANCH_ON_GREATER_THAN_OR_EQUAL.run(registersData[0], registersData[1], integerData);
				} else {
					Command.BRANCH_ON_GREATER_THAN_OR_EQUAL.run(registersData[0], registersData[1], branchData);
				}
			}
			case JUMP -> {
				if(branchData == null) {
					Command.JUMP.run(integerData);
				} else {
					Command.JUMP.run(branchData);
				}
			}
			case JUMP_REGISTER -> Command.JUMP_REGISTER.run(registersData[0]);
			case JUMP_AND_LINK -> Command.JUMP_AND_LINK.run(integerData);
		}
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();

		builder.append("Instruction: ").append(command).append("\n");

		if(registersData != null) {
			builder.append("Registers: ").append(Arrays.toString(registersData)).append("\n");
		}

		if(branchData != null) {
			builder.append("Branch: ").append(branchData).append("\n");
		}

		if(integerData != Integer.MIN_VALUE) {
			builder.append("Integer: ").append(integerData).append("\n");
		}

		return builder.toString();
	}
}
