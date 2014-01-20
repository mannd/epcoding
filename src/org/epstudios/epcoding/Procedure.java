package org.epstudios.epcoding;

import android.content.Context;

public interface Procedure {
	boolean hasSecondaryCodes();

	Code[] primaryCodes();

	Code[] secondaryCodes();

	String helpText(Context context);

}
