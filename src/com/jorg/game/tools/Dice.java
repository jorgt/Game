package com.jorg.game.tools;

import java.util.Random;

public class Dice {

	private static Random r = new Random();

	public static int d4() {
		return r.nextInt(3) + 1;
	}

	public static int d4(int n) {
		int result = 0;
		while (n-- > 0) {
			result += Dice.d4();
		}
		return result;
	}

	public static int d6() {
		return r.nextInt(6) + 1;
	}

	public static int d6(int n) {
		int result = 0;
		while (n-- > 0) {
			result += Dice.d6();
		}
		return result;
	}

	public static int d8() {
		return r.nextInt(7) + 1;
	}

	public static int d8(int n) {
		int result = 0;
		while (n-- > 0) {
			result += Dice.d8();
		}
		return result;
	}

	public static int d10() {
		return r.nextInt(9) + 1;
	}

	public static int d10(int n) {
		int result = 0;
		while (n-- > 0) {
			result += Dice.d10();
		}
		return result;
	}

	public static int d12() {
		return r.nextInt(11) + 1;
	}

	public static int d12(int n) {
		int result = 0;
		while (n-- > 0) {
			result += Dice.d12();
		}
		return result;
	}

	public static int d20() {
		return r.nextInt(19) + 1;
	}

	public static int d20(int n) {
		int result = 0;
		while (n-- > 0) {
			result += Dice.d20();
		}
		return result;
	}
}
