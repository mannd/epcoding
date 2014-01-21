package org.epstudios.epcoding;

import android.content.Context;

public class PpmReplacement extends DeviceProcedure implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.ppm_replacement_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.ppmGeneratorReplacementCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		String[] disabledCodeNumbers = {};
		return disabledCodeNumbers;
	}

}
