package org.epstudios.epcoding;

public class Code {
	private final String code;
	private final String shortDescription;
	private final boolean isAddOn;
	// control display of code
	private boolean plusShown = true;
	private boolean descriptionShortened = false;
	private boolean descriptionShown = true;

	public Code(String code, String shortDescription, boolean isAddOn) {
		this.code = code;
		this.shortDescription = shortDescription;
		this.isAddOn = isAddOn;
	}

	public String getCodeNumberWithAddOn() {
		return (isAddOn && plusShown ? "+" : "") + code;
	}

	public String getCodeNumberWithAddOnWithDescription() {
		return getCodeNumberWithAddOn() + " (" + shortDescription + ")";
	}

	public String getCodeNumber() {
		return code;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getDescription() {
		return getShortDescription() + " (" + getCodeNumberWithAddOn() + ")";
	}

	public String getCodeFirstDescription() {
		return getCodeNumber() + " " + getShortDescription();
	}

	public boolean isAddOn() {
		return isAddOn;
	}

	public void setPlusShown(boolean plusShown) {
		this.plusShown = plusShown;
	}

	public void setDescriptionShortened(boolean descriptionShortened) {
		this.descriptionShortened = descriptionShortened;
	}

	public void setDescriptionShown(boolean descriptionShown) {
		this.descriptionShown = descriptionShown;
	}

	private String truncateString(final String s, final int newLength,
			final int minLength) {
		if (newLength > s.length() || s.length() < minLength)
			return s;
		return s.substring(0, s.length() - 3) + "...";
	}
}
