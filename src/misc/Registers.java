package misc;

import util.BinaryConversion;

import java.util.ArrayList;

/**
 * All <code>Register</code>s.
 */
public class Registers {

	public static final Register zero = new Register("zero");
	public static final Register at = new Register("at");
	public static final Register v0 = new Register("v0");
	public static final Register v1 = new Register("v1");
	public static final Register a0 = new Register("a0");
	public static final Register a1 = new Register("a1");
	public static final Register a2 = new Register("a2");
	public static final Register a3 = new Register("a3");
	public static final Register t0 = new Register("t0");
	public static final Register t1 = new Register("t1");
	public static final Register t2 = new Register("t2");
	public static final Register t3 = new Register("t3");
	public static final Register t4 = new Register("t4");
	public static final Register t5 = new Register("t5");
	public static final Register t6 = new Register("t6");
	public static final Register t7 = new Register("t7");
	public static final Register t8 = new Register("t8");
	public static final Register t9 = new Register("t9");
	public static final Register s0 = new Register("s0");
	public static final Register s1 = new Register("s1");
	public static final Register s2 = new Register("s2");
	public static final Register s3 = new Register("s3");
	public static final Register s4 = new Register("s4");
	public static final Register s5 = new Register("s5");
	public static final Register s6 = new Register("s6");
	public static final Register s7 = new Register("s7");
	public static final Register k0 = new Register("k0");
	public static final Register k1 = new Register("k1");
	public static final Register gp = new Register("gp");
	public static final Register sp = new Register("sp");
	public static final Register fp = new Register("fp");
	public static final Register ra = new Register("ra");
	public static final Register hi = new Register("hi");
	public static final Register lo = new Register("lo");
	public static final Register pc = new Register("pc");

	public static final Register[] REGISTERS = {
					zero,
					at,
					v0,
					v1,
					a0,
					a1,
					a2,
					a3,
					t0,
					t1,
					t2,
					t3,
					t4,
					t5,
					t6,
					t7,
					s0,
					s1,
					s2,
					s3,
					s4,
					s5,
					s6,
					s7,
					t8,
					t9,
					k0,
					k1,
					gp,
					sp,
					fp,
					ra,
	};

	public static final ArrayList<Register> READONLY_REGISTERS = new ArrayList<>() {
		{
			add(zero);
			add(at);
			add(k0);
			add(k1);
			add(hi);
			add(lo);
			add(pc);
		}
	};

	/**
	 * Gets the <code>Register</code> from a String name.
	 *
	 * @param name The name of a <code>Register</code>
	 * @return The matching <code>Register</code> or <code>null</code>
	 */
	public static Register getFromString(String name) {
		return switch (name) {
			case "zero", "0" -> zero;
			case "at", "1" -> at;
			case "v0", "2" -> v0;
			case "v1", "3" -> v1;
			case "a0", "4" -> a0;
			case "a1", "5" -> a1;
			case "a2", "6" -> a2;
			case "a3", "7" -> a3;
			case "t0", "8" -> t0;
			case "t1", "9" -> t1;
			case "t2", "10" -> t2;
			case "t3", "11" -> t3;
			case "t4", "12" -> t4;
			case "t5", "13" -> t5;
			case "t6", "14" -> t6;
			case "t7", "15" -> t7;
			case "s0", "16" -> s0;
			case "s1", "17" -> s1;
			case "s2", "18" -> s2;
			case "s3", "19" -> s3;
			case "s4", "20" -> s4;
			case "s5", "21" -> s5;
			case "s6", "22" -> s6;
			case "s7", "23" -> s7;
			case "t8", "24" -> t8;
			case "t9", "25" -> t9;
			case "k0", "26" -> k0;
			case "k1", "27" -> k1;
			case "gp", "28" -> gp;
			case "sp", "29" -> sp;
			case "fp", "30" -> fp;
			case "ra", "31" -> ra;
			case "pc" -> pc;
			default -> null;
		};
	}

	/**
	 * Prints out all <code>Register</code>s.
	 */
	public static void printRegisters() {
		for(Register register : REGISTERS) {
			System.out.println(register.toString());
		}
	}

	/**
	 * Prints out all <code>Register</code>s in
	 * hexadecimal
	 */
	public static void printRegistersHex() {
		for(Register register : REGISTERS) {
			System.out.println(register.getName() + ": " + register.getHexValueString());
		}
	}

	/**
	 * Prints out all <code>Register</code>s in
	 * octal
	 */
	public static void printRegistersOctal() {
		for(Register register : REGISTERS) {
			System.out.println(register.getName() + ": " + register.getOctalValueString());
		}
	}

	/**
	 * Prints out all <code>Register</code>s in
	 * base-10
	 */
	public static void printRegistersBase10() {
		for(Register register : REGISTERS) {
			System.out.println(register.getName() + ": " + register.getIntegerOfValues());
		}
	}

	/**
	 * Resets all <code>Register</code>s.
	 */
	public static void reset() {
		for(Register register : REGISTERS) {
			register.storeNum(0);
		}
	}
}
