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

}
