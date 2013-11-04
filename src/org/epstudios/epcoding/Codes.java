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
		addCode(map, "93656", "AFB Ablation", false);
		addCode(map, "93654", "VT Ablation", false);
		addCode(map, "93653", "SVT Ablation", false);
		addCode(map, "93620", "EP Testing", false);
		addCode(map, "93655", "Additional SVT Ablation", true);
		addCode(map, "93657", "Additional AFB Ablation", true);
		addCode(map, "93609", "2D Mapping", true);
		addCode(map, "93613", "3D Mapping", true);
		addCode(map, "93621", "LA Pacing & Recording", true);
		addCode(map, "93622", "LV Pacing & Recording", true);
		addCode(map, "93623", "Induce Post IV Drug", true);
		addCode(map, "93662", "Intracardiac Echo", true);
		addCode(map, "93642", "Transseptal Cath", true);
		addCode(map, "36620", "Arterial Line Placement", false);
		return Collections.unmodifiableMap(map);
	}

	public final static String[] ablationSecondaryCodeNumbers = { "93655",
			"93657", "93609", "93613", "93621", "93622", "93623", "93662",
			"93642", "36620" };

	private final static void addCode(Map<String, Code> map, String codeNumber,
			String name, boolean isAddOn) {
		map.put(codeNumber, new Code(codeNumber, name, isAddOn));

	}

	public final static Code getCode(String code) {
		return allCodes.get(code);
	}

	public static Code[] getCodes(String codesName) {
		Code[] codes = new Code[1];
		codes[0] = getCode("99999");
		return codes;
	}

	public static Code[] getAblationSecondaryCodes() {
		Code[] codes = new Code[ablationSecondaryCodeNumbers.length];
		for (int i = 0; i < ablationSecondaryCodeNumbers.length; ++i)
			codes[i] = getCode(ablationSecondaryCodeNumbers[i]);
		return codes;
	}

}
