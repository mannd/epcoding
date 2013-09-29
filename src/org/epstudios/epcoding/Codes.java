package org.epstudios.epcoding;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Codes {
	private static Map<String, Code> codes = createMap();

	private static Map<String, Code> createMap() {
		Map<String, Code> result = new HashMap<String, Code>();
		// initialize all codes here
		Code testCode = new Code("99999", "Test Code", false);
		result.put("99999", testCode);
		return Collections.unmodifiableMap(result);
	}

	public static Code getCode(String code) {
		return codes.get(code);
	}

}
