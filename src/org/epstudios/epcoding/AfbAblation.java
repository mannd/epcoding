package org.epstudios.epcoding;

import android.content.Context;

public class AfbAblation extends AblationProcedure implements Procedure {
	@Override
	public String title(Context context) {
		return context.getString(R.string.afb_ablation_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.afbAblationPrimaryCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		return Codes.afbAblationDisabledCodeNumbers;
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.afb_ablation_help_text);
	}

}
