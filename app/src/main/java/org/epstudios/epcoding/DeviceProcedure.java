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

@SuppressWarnings({"SameReturnValue"})
class DeviceProcedure {
	public boolean disablePrimaryCodes() {
		return false;
	}

	public Code[] secondaryCodes() {
		return Codes.getDeviceSecondaryCodes();
	}

	public String[] disabledCodeNumbers() {
        return new String[]{};
	}

	public String helpText(Context context) {
		return context.getString(R.string.devices_help_text);
	}

	public boolean doNotWarnForNoSecondaryCodesSelected() {
		return true;
	}

}
