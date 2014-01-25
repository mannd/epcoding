package org.epstudios.epcoding;

import android.content.Context;

public class NewPpm extends DeviceProcedure implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.new_ppm_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.newPpmPrimaryCodeNumbers);
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.new_ppm_help_text);
	}

	@Override
	public String[] disabledCodeNumbers() {
		String[] numbers = {};
		return numbers;
	}

}
