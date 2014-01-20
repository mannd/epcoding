package org.epstudios.epcoding;

public class SvtAblation extends AblationProcedure implements Procedure {
	@Override
	public String title() {
		return "SVT Ablation";
	}

	@Override
	public boolean hasSecondaryCodes() {
		return true;
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.svtAblationPrimaryCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		return Codes.svtAblationDisabledCodeNumbers;
	}

}
