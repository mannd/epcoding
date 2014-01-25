package org.epstudios.epcoding;

public class IcdDeviceProcedure extends DeviceProcedure {
	@Override
	public Code[] secondaryCodes() {
		return Codes.getCodes(Codes.icdDeviceSecondaryCodeNumbers);
	}
}
