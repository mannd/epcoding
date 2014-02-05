package org.epstudios.epcoding;

public class Combo {
	private final String s1;
	private final String s2;

	Combo(String s1, String s2) {
		this.s1 = s1;
		this.s2 = s2;
	}

	public String getS1() {
		return s1;
	}

	public String getS2() {
		return s2;
	}

	@Override
	public String toString() {
		return "[" + s1 + ", " + s2 + "]";
	}
}
