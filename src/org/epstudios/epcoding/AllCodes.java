package org.epstudios.epcoding;

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

}
