package org.epstudios.epcoding;

import android.content.Context;

public class IcdReplacement extends IcdDeviceProcedure implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.icd_replacement_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.IcdReplacementPrimaryCodeNumbers);
	}

	@Override
	public Code[] secondaryCodes() {
		return Codes.getCodes(Codes.icdReplacementSecondaryCodeNumbers);
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.icd_replacement_help_text);
	}

}
