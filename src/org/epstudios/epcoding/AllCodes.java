package org.epstudios.epcoding;

import android.content.Context;

public class AllCodes implements Procedure {

	@Override
	public boolean hasSecondaryCodes() {
		return false;
	}

	@Override
	public Code[] primaryCodes() {
		// TODO Auto-generated method stub
		return Codes.getCodes(Codes.allCodeNumbersSorted());
	}

	@Override
	public Code[] secondaryCodes() {
		return null;
	}

	@Override
	public String helpText(Context context) {

		return context.getString(R.string.ablation_help_text);
	}

}
