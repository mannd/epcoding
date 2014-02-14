package org.epstudios.epcoding;

import android.content.Context;

public class OtherProcedure implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.other_procedure_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.otherProcedurePrimaryCodeNumbers);
	}

	@Override
	public boolean disablePrimaryCodes() {
		return false;
	}

	@Override
	public Code[] secondaryCodes() {
		Code[] secondaryCodes = {};
		return secondaryCodes;
	}

	@Override
	public String[] disabledCodeNumbers() {
		String[] disabledCodeNumbers = {};
		return disabledCodeNumbers;
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.other_procedure_help_text);
	}

	@Override
	public boolean doNotWarnForNoSecondaryCodesSelected() {
		return true;
	}

}
