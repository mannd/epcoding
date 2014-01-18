package org.epstudios.epcoding;

import android.content.Context;
import android.widget.CheckBox;

public class CodeCheckBox extends CheckBox {
	private Code code;

	public CodeCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// default code is error code
		code = Codes.getCode("99999");
	}

	// public static void createCheckBoxMap(Context context, Map<String,
	// CodeCheckBox> map, Layout layout) {
	// for (int i = 0; i < map.length; ++i) {
	// CodeCheckBox secondaryCheckBox = new CodeCheckBox(context);
	// secondaryCheckBox.setCode(map[i]);
	// secondaryCheckBoxMap.put(map[i].getCodeNumber(),
	// secondaryCheckBox);
	// secondaryCheckBoxLayout.addView(secondaryCheckBox);
	// }
	// }

	public void setCode(Code c) {
		code = c;
		// text is automatically set to code description
		super.setText(c.getDescription());
	}

	public Code getCode() {
		return code;
	}

	public void disable() {
		setChecked(false);
		setEnabled(false);
	}

	public void clearIfEnabled() {
		if (isEnabled())
			setChecked(false);
	}

}
