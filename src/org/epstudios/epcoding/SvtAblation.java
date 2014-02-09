package org.epstudios.epcoding;

import android.content.Context;

public class SvtAblation extends AblationProcedure implements Procedure {
	@Override
	public String title(Context context) {
		return context.getString(R.string.svt_ablation_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.svtAblationPrimaryCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		return Codes.svtAblationDisabledCodeNumbers;
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.svt_ablation_help_text);
	}

}
