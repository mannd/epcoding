package org.epstudios.epcoding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Codes {
	private static Map<String, Code> codes = createMap();

	private static Map<String, Code> createMap() {
		Map<String, Code> result = new HashMap<String, Code>();
		String codeNum = new String();
		// initialize all codes here
		codeNum = "99999";
		Code testCode = new Code(codeNum, "Test Code", false);
		result.put(codeNum, testCode);
		// Ablation and EP testing codes
		codeNum = "93656";
		Code afbAblation = new Code(codeNum, "AFB Ablation", false);
		result.put(codeNum, afbAblation);
		codeNum = "93654";
		Code vtAblation = new Code(codeNum, "VT Ablation", false);
		result.put(codeNum, vtAblation);
		codeNum = "93653";
		Code svtAblation = new Code(codeNum, "SVT Ablation", false);
		result.put(codeNum, svtAblation);
		codeNum = "93620";
		Code epTesting = new Code(codeNum, "EP Testing", false);
		result.put(codeNum, epTesting);
		codeNum = "93655";
		Code additionalSvtAblation = new Code(codeNum,
				"Additional SVT or VT Ablation", true);
		result.put(codeNum, additionalSvtAblation);
		codeNum = "93657";
		Code additionalAfbAblation = new Code(codeNum,
				"Additional AFB Ablation", true);
		result.put(codeNum, additionalAfbAblation);
		codeNum = "93609";
		Code twoDMapping = new Code(codeNum, "2D Mapping", true);
		result.put(codeNum, twoDMapping);
		codeNum = "93613";
		Code threeDMapping = new Code(codeNum, "3D Mapping", true);
		result.put(codeNum, threeDMapping);
		codeNum = "93621";
		Code laPaceRecord = new Code(codeNum, "LA Pacing & Recording", true);
		result.put(codeNum, laPaceRecord);
		codeNum = "93622";
		Code lvPaceRecord = new Code(codeNum, "LV Pacing & Recording", true);
		result.put(codeNum, lvPaceRecord);
		codeNum = "93623";
		Code induceIvDrug = new Code(codeNum, "Induce Post IV Drug", true);
		result.put(codeNum, induceIvDrug);
		codeNum = "93662";
		Code intraCardiacEcho = new Code(codeNum, "Intracardiac Echo", true);
		result.put(codeNum, intraCardiacEcho);
		codeNum = "93462";
		Code transseptalCath = new Code(codeNum, "Transseptal Cath", true);
		result.put(codeNum, transseptalCath);
		codeNum = "36620";
		Code arterialLine = new Code(codeNum, "Arterial Line Placement", true);
		result.put(codeNum, arterialLine);
		return Collections.unmodifiableMap(result);
	}

	public static Code getCode(String code) {
		return codes.get(code);
	}

}
