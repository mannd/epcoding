package org.epstudios.epcoding;

public class AfbAblation extends AblationProcedure implements Procedure {
	@Override
	public String title() {
		return "AFB Ablation";
	}

	@Override
	public boolean hasSecondaryCodes() {
		return true;
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.afbAblationPrimaryCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		return Codes.afbAblationDisabledCodeNumbers;
	}

}
