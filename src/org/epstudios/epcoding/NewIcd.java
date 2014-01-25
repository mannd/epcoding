package org.epstudios.epcoding;

import android.content.Context;

public class NewIcd extends IcdDeviceProcedure implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.new_icd_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.newIcdPrimaryCodeNumbers);
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.new_icd_help_text);
	}

}
