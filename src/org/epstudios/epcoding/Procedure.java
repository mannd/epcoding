package org.epstudios.epcoding;

import android.content.Context;

public interface Procedure {
	String title(Context context);

	Code[] primaryCodes();

	boolean disablePrimaryCodes();

	Code[] secondaryCodes();

	String[] disabledCodeNumbers();

	String helpText(Context context);

	boolean doNotWarnForNoSecondaryCodesSelected();

}