package org.epstudios.epcoding;

import android.content.Context;

// This partially implements the Procedure interface, only the parts common to 
// all ablation procedures (secondary codes).
public class AblationProcedure {
	public boolean disablePrimaryCodes() {
		return true;
	}

	public Code[] secondaryCodes() {
		return Codes.getAblationSecondaryCodes();
	}

	public String helpText(Context context) {
		return context.getString(R.string.ablation_help_text);
	}
}
