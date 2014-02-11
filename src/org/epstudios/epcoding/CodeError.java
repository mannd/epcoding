package org.epstudios.epcoding;

import java.util.List;

public class CodeError {
	public static enum WarningLevel {
		NONE, WARNING, ERROR
	}

	private WarningLevel warningLevel;
	private List<String> codes;
	private String warningMessage;

	public CodeError(WarningLevel warningLevel, List<String> codes,
			String warningMessage) {
		this.setWarningLevel(warningLevel);
		this.setCodes(codes);
		this.setWarningMessage(warningMessage);
	}

	public WarningLevel getWarningLevel() {
		return warningLevel;
	}

	public void setWarningLevel(WarningLevel warningLevel) {
		this.warningLevel = warningLevel;
	}

	public List<String> getCodes() {
		return codes;
	}

	public void setCodes(List<String> codes) {
		this.codes = codes;
	}

	public String getWarningMessage() {
		return warningMessage;
	}

	public void setWarningMessage(String warningMessage) {
		this.warningMessage = warningMessage;
	}

}
