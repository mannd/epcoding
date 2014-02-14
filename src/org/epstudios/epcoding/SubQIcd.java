package org.epstudios.epcoding;

import android.content.Context;

public class SubQIcd implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.subq_icd_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.subQIcdPrimaryCodeNumbers);
	}

	@Override
	public boolean disablePrimaryCodes() {
		return false;
	}

	@Override
	public Code[] secondaryCodes() {
		Code[] codes = {};
		return codes;
	}

	@Override
	public String[] disabledCodeNumbers() {
		String[] numbers = {};
		return numbers;
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.subq_icd_help_text);
	}

	@Override
	public boolean doNotWarnForNoSecondaryCodesSelected() {
		return true;
	}

}
