package org.epstudios.epcoding;

import android.content.Context;

public class VtAblation extends AblationProcedure implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.vt_ablation_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.vtAblationPrimaryCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		return Codes.vtAblationDisabledCodeNumbers;
	}

}
