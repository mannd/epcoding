package org.epstudios.epcoding;

import android.content.Context;

public class DeviceUpgrade extends IcdDeviceProcedure implements Procedure {

	@Override
	public String title(Context context) {
		return context.getString(R.string.device_upgrade_title);
	}

	@Override
	public Code[] primaryCodes() {
		return Codes.getCodes(Codes.deviceUpgradePrimaryCodeNumbers);
	}

	@Override
	public String helpText(Context context) {
		return context.getString(R.string.device_upgrade_help_text);
	}

}
