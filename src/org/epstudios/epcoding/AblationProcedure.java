package org.epstudios.epcoding;

// This partially implements the Procedure interface, only the parts common to 
// all ablation procedures (secondary codes).
public class AblationProcedure {
	public boolean disablePrimaryCodes() {
		return true;
	}

	public Code[] secondaryCodes() {
		return Codes.getAblationSecondaryCodes();
	}

	public boolean doNotWarnForNoSecondaryCodesSelected() {
		return false;
	}

}
