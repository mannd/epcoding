package org.epstudios.epcoding;

// class containing the components of a CPT code
public class CPTCode {
	private final String code;
	private final String shortDescription;
	private final String longDescription;
	private final boolean isModifier;
	private final boolean isAddOn;

	public CPTCode(String code, String shortDescription,
			String longDescription, boolean isModifier, boolean isAddOn) {
		this.code = code;
		this.shortDescription = shortDescription;
		this.longDescription = longDescription;
		this.isModifier = isModifier;
		this.isAddOn = isAddOn;
	}

	public String getCode() {
		return code;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getLongDescription() {
		return longDescription;
	}

	public boolean isModifier() {
		return isModifier;
	}

	public boolean isAddOn() {
		return isAddOn;
	}

}
