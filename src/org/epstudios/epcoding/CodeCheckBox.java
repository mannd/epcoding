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

	public void setCode(Code c) {
		code = c;
	}

	public Code getCode() {
		return code;
	}

}
