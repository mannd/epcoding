package org.epstudios.epcoding;

import android.content.Context;

public class AllCodes implements Procedure {
	@Override
	public String title(Context context) {
		return context.getString(R.string.all_codes_title);
	}

	@Override
	public Code[] primaryCodes() {
		// TODO Auto-generated method stub
		return Codes.getCodes(Codes.allCodeNumbersSorted());
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
		String[] strings = {};
		return strings;
	}

	@Override
	public String helpText(Context context) {

		return context.getString(R.string.all_codes_help_text);
	}

	@Override
	public boolean doNotWarnForNoSecondaryCodesSelected() {
		return true;
	}

}
