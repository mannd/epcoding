package org.epstudios.epcoding;

public interface Procedure {
	boolean hasSecondaryCodes();

	Code[] primaryCodes();

	Code[] secondaryCodes();

}
