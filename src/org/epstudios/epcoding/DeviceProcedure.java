package org.epstudios.epcoding;

import android.content.Context;

public class DeviceProcedure {
	public boolean disablePrimaryCodes() {
		return false;
	}

	public Code[] secondaryCodes() {
		return Codes.getDeviceSecondaryCodes();
	}

	public String helpText(Context context) {
		return context.getString(R.string.devices_help_text);
	}
}
