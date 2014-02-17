package org.epstudios.epcoding;

import android.content.Context;
import android.widget.CheckBox;

public class CodeCheckBox extends CheckBox {
	private Code code;
	private boolean codeFirst = false;

	public CodeCheckBox(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		// default code is error code
		code = Codes.getCode("99999");
	}

	public void setCode(Code c) {
		code = c;
		// text is automatically set to code description
		// super.setText(c.getUnformattedDescriptionFirst());
		super.setText(codeFirst ? c.getUnformattedNumberFirst() : c
				.getUnformattedDescriptionFirst());

	}

	public Code getCode() {
		return code;
	}

	public String getCodeNumber() {
		return code.getCodeNumber();
	}

	public void disable() {
		setChecked(false);
		setEnabled(false);
	}

	public void clearIfEnabled() {
		if (isEnabled())
			setChecked(false);
	}

	public boolean isCodeFirst() {
		return codeFirst;
	}

	public void setCodeFirst(boolean codeFirst) {
		this.codeFirst = codeFirst;
	}

}
