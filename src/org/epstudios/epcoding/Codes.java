package org.epstudios.epcoding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Codes {

	private final static Map<String, Code> allCodes = createMap();

	private final static Map<String, Code> createMap() {
		Map<String, Code> map = new HashMap<String, Code>();
		// initialize all codes here
		addCode(map, "99999", "Test Code", false);
		// Ablation and EP testing codes
		addCode(map, "93656", "AFB ablation", false);
		addCode(map, "93654", "VT ablation", false);
		addCode(map, "93653", "SVT ablation", false);
		addCode(map, "93620", "EP testing", false);
		addCode(map, "93655", "Additional SVT ablation", true);
		addCode(map, "93657", "Additional AFB ablation", true);
		addCode(map, "93609", "2D mapping", true);
		addCode(map, "93613", "3D mapping", true);
		addCode(map, "93621", "LA pacing & recording", true);
		addCode(map, "93622", "LV pacing & recording", true);
		addCode(map, "93623", "Induce post IV drug", true);
		addCode(map, "93662", "Intracardiac echo", true);
		addCode(map, "93642", "Transseptal cath", true);
		addCode(map, "36620", "Arterial line placement", false);
		// Devices
		addCode(map, "33206", "New or replacement PPM with new A lead", false);
		addCode(map, "33227", "Single chamber PPM generator replacement", false);
		addCode(map, "33228", "Dual chamber PPM generator replacement", false);

		return Collections.unmodifiableMap(map);
	}

	public final static String[] afbAblationPrimaryCodeNumbers = { "93656" };
	public final static String[] svtAblationPrimaryCodeNumbers = { "93653" };
	public final static String[] vtAblationPrimaryCodeNumbers = { "93654" };
	public final static String[] epTestingPrimaryCodeNumbers = { "93620" };

	public final static String[] ppmGeneratorReplacementCodeNumbers = {
			"33227", "33228" };

	public final static String[] ablationSecondaryCodeNumbers = { "93655",
			"93657", "93609", "93613", "93621", "93622", "93623", "93662",
			"93642", "36620" };

	public final static String[] afbAblationDisabledCodeNumbers = { "93621",
			"93642" };
	public final static String[] svtAblationDisabledCodeNumbers = { "93657" };
	public final static String[] vtAblationDisabledCodeNumbers = { "93657",
			"93609", "93613", "93622" };
	public final static String[] epTestingDisabledCodeNumbers = { "93657",
			"93655" };

	private final static void addCode(Map<String, Code> map, String codeNumber,
			String name, boolean isAddOn) {
		map.put(codeNumber, new Code(codeNumber, name, isAddOn));

	}

	public final static Code getCode(String code) {
		return allCodes.get(code);
	}

	public static Code[] getCodes(String[] codeNumbers) {
		Code[] codes = new Code[codeNumbers.length];
		for (int i = 0; i < codeNumbers.length; ++i)
			codes[i] = getCode(codeNumbers[i]);

		return codes;
	}

	public static Code[] getAblationSecondaryCodes() {
		return getCodes(ablationSecondaryCodeNumbers);
	}

}
