/*  
Copyright (C) 2013, 2014 EP Studios, Inc.
www.epstudiossoftware.com

This file is part of EP Coding.

    EP Coding is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    EP Coding is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with EP Coding.  If not, see <http://www.gnu.org/licenses/>.

    Note also:

    CPT copyright 2012 American Medical Association. All rights
    reserved. CPT is a registered trademark of the American Medical
    Association.

    A limited number of CPT codes are used in this program under the Fair Use
    doctrine of US Copyright Law.  See README.md for more information.
 */

package org.epstudios.epcoding;

import android.content.Context;
import androidx.appcompat.widget.AppCompatCheckBox;

class CodeCheckBox extends AppCompatCheckBox {
	private Code code;
	private boolean codeFirst = false;

	public CodeCheckBox(Context context) {
		super(context);
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

	public void setCodeFirst(boolean codeFirst) {
		this.codeFirst = codeFirst;
	}

}
