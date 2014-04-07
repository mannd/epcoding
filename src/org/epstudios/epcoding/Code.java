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

public class Code {
	private final String code;
	private final String shortDescription;
	private final boolean isAddOn;
	// control display of code
	private boolean plusShown = true;
	private boolean descriptionShortened = false;
	private boolean descriptionShown = true;

	public Code(String code, String shortDescription, boolean isAddOn) {
		this.code = code;
		this.shortDescription = shortDescription;
		this.isAddOn = isAddOn;
	}

	public String getCodeNumberWithAddOn() {
		return (isAddOn && plusShown ? "+" : "") + code;
	}

	public String getCodeNumberWithAddOnWithDescription() {
		return getCodeNumberWithAddOn() + " (" + shortDescription + ")";
	}

	public String getCodeNumber() {
		return code;
	}

	public String getShortDescription() {
		return shortDescription;
	}

	public String getDescription() {
		return getShortDescription() + " (" + getCodeNumberWithAddOn() + ")";
	}

	public String getCodeFirstDescription() {
		return getCodeNumber() + " " + getShortDescription();
	}

	public boolean isAddOn() {
		return isAddOn;
	}

	public void setPlusShown(boolean plusShown) {
		this.plusShown = plusShown;
	}

	public void setDescriptionShortened(boolean descriptionShortened) {
		this.descriptionShortened = descriptionShortened;
	}

	public void setDescriptionShown(boolean descriptionShown) {
		this.descriptionShown = descriptionShown;
	}

	// applies _all_ formatting settings with code given first
	public String getCodeFirstFormatted() {
		return getCodeNumberWithAddOn()
				+ (descriptionShown ? " (" + (getFormattedDescription()) + ")"
						: "");
	}

	// applies _all_ formatting settings with description given first
	public String getDescriptionFirstFormatted() {
		return getFormattedDescription() + " (" + getCodeNumberWithAddOn()
				+ ")";
	}

	private String getFormattedDescription() {
		return descriptionShortened ? truncateString(shortDescription, 24)
				: shortDescription;
	}

	private String truncateString(final String s, final int newLength) {
		if (newLength > s.length())
			return s;
		return s.substring(0, newLength - 3) + "...";
	}

	// use this for CodeCheckBox text
	public String getUnformattedDescriptionFirst() {
		return shortDescription + " (" + (isAddOn ? "+" : "") + code + ")";
	}

	public String getUnformattedNumberFirst() {
		return "(" + (isAddOn ? "+" : "") + code + ") " + shortDescription;
	}

	boolean codeContains(String searchString) {
		return (code.contains(searchString) || shortDescription
				.contains(searchString));
	}
}
