package org.epstudios.epcoding;

import android.content.Context;

public class EpTesting implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.ep_testing_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.epTestingPrimaryCodeNumbers);
	}

	@Override
	public boolean disablePrimaryCodes() {
		return false;
	}

	@Override
	public Code[] secondaryCodes() {
		return Codes.getCodes(Codes.ablationSecondaryCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		return Codes.epTestingDisabledCodeNumbers;
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.ablation_help_text);
	}

}
