package org.epstudios.epcoding;

import android.content.Context;

public class AvnAblation implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.avn_ablation_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.avnAblationPrimaryCodeNumbers);
	}

	@Override
	public boolean disablePrimaryCodes() {
		return true;
	}

	@Override
	public Code[] secondaryCodes() {
		return Codes.getCodes(Codes.avnAblationSecondaryCodeNumbers);
	}

	@Override
	public String[] disabledCodeNumbers() {
		String[] disabledCodeNumbers = {};
		return disabledCodeNumbers;
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.av_node_ablation_help_text);
	}

	@Override
	public boolean doNotWarnForNoSecondaryCodesSelected() {
		return false;
	}

}
