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
	 * TODO: Replace all uses of this class before publishing your app.
	 */

	/**
	 * An array of sample (dummy) items.
	 */
	public static List<ProcedureType> ITEMS = new ArrayList<ProcedureType>();

	/**
	 * A map of sample (dummy) items, by ID.
	 */
	public static Map<String, ProcedureType> ITEM_MAP = new HashMap<String, ProcedureType>();

	static {
		// Add 3 sample items.
		addItem(new ProcedureType("0", "AFB Ablation"));
		addItem(new ProcedureType("1", "SVT Ablation"));
		addItem(new ProcedureType("2", "VT Ablation"));
		addItem(new ProcedureType("3", "AV Node Ablation"));
		addItem(new ProcedureType("4", "EP Testing"));
		addItem(new ProcedureType("5", "New PPM"));
		addItem(new ProcedureType("6", "New ICD"));
		addItem(new ProcedureType("7", "PPM Replace"));
		addItem(new ProcedureType("8", "ICD Replace"));
		addItem(new ProcedureType("9", "Upgrade/Revision/Extraction"));
		// Cardioversion, Tilt Table
		addItem(new ProcedureType("10", "Other Procedure"));
		addItem(new ProcedureType("11", "All Codes"));
	}

	private static void addItem(ProcedureType item) {
		ITEMS.add(item);
		ITEM_MAP.put(item.id, item);
	}

	/**
	 * A dummy item representing a piece of content.
	 */
	public static class ProcedureType {
		public String id;
		public String content;

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
