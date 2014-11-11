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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProcedureTypes {

	/**
	 * Helper class for providing sample content for user interfaces created by
	 * Android template wizards.
	 * <p>
	 */

	/**
	 * An array of sample (dummy) items.
	 */
	public static final List<ProcedureType> ITEMS = new ArrayList<ProcedureType>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static final Map<String, ProcedureType> ITEM_MAP = new HashMap<String, ProcedureType>();

	static {
		// Add 3 sample items.
		addItem(new ProcedureType("0", "AFB Ablation"));
		addItem(new ProcedureType("1", "SVT Ablation"));
		addItem(new ProcedureType("2", "VT Ablation"));
		addItem(new ProcedureType("3", "AV Node Ablation"));
		addItem(new ProcedureType("4", "EP Testing"));
		addItem(new ProcedureType("5", "New PPM"));
		addItem(new ProcedureType("6", "New ICD"));
		addItem(new ProcedureType("7", "Replace PPM"));
		addItem(new ProcedureType("8", "Replace ICD"));
		addItem(new ProcedureType("9", "Upgrade/Revision/Extraction"));
		addItem(new ProcedureType("10", "SubQ ICD"));
		addItem(new ProcedureType("11", "Other Procedure"));
		addItem(new ProcedureType("12", "All EP Codes"));
	}

	private static void addItem(ProcedureType item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class ProcedureType {
		public final String id;
		public final String content;

		public ProcedureType(String id, String content) {
			this.id = id;
			this.content = content;
		}

		@Override
		public String toString() {
			return content;
		}
	}
}
