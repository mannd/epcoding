package org.epstudios.epcoding;

import java.util.LinkedHashMap;
import java.util.Map;

import android.content.Context;
import android.widget.LinearLayout;

public class Utilities {
	public static Map<String, CodeCheckBox> createCheckBoxLayoutAndCodeMap(
			Code[] codes, LinearLayout layout, Context context,
			boolean showCodeFirst) {
		Map<String, CodeCheckBox> map = new LinkedHashMap<String, CodeCheckBox>();
		for (int i = 0; i < codes.length; ++i) {
			CodeCheckBox codeCheckBox = new CodeCheckBox(context);
			codeCheckBox.setCodeFirst(showCodeFirst);
			codeCheckBox.setCode(codes[i]);
			map.put(codes[i].getCodeNumber(), codeCheckBox);
			layout.addView(codeCheckBox);
		}
		return map;
	}

}
