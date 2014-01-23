package org.epstudios.epcoding;

import android.widget.CheckBox;

public class Code {
	private final String code;
	private final String shortDescription;
	private final boolean isAddOn;
	private CheckBox checkBox;

	public Code(String code, String shortDescription, boolean isAddOn) {
		this.code = code;
		this.shortDescription = shortDescription;
		this.isAddOn = isAddOn;

	}

	public void setCheckBox(CheckBox checkBox) {
		this.checkBox = checkBox;
	}

	public void setChecked(boolean checked) {
		checkBox.setChecked(checked);
	}

	public void setEnabled(boolean enabled) {
		checkBox.setEnabled(enabled);
	}

	public String getCodeNumberWithAddOn() {
		return (isAddOn ? "+" : "") + code;
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
		return shortDescription + " (" + getCodeNumberWithAddOn() + ")";
	}

	public String getCodeFirstDescription() {
		return code + " " + shortDescription;
	}

	public boolean isAddOn() {
		return isAddOn;
	}
}
