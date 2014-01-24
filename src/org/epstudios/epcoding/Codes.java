package org.epstudios.epcoding;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Codes {

	private final static Map<String, Code> allCodes = createMap();

	private final static Map<String, Code> createMap() {
		Map<String, Code> map = new HashMap<String, Code>();
		// addCode(map, String codeNumber, String codeName, boolean isAddOn)

		// Ablation and EP testing codes

		// EP Testing and Mapping
		addCode(map, "93609", "2D mapping", true);
		addCode(map, "93613", "3D mapping", true);
		addCode(map, "93620", "EP testing", false);
		addCode(map, "93621", "LA pacing & recording", true);
		addCode(map, "93622", "LV pacing & recording", true);
		addCode(map, "93623", "Induce post IV drug", true);

		// Ablation
		addCode(map, "93650", "AV node ablation", false);
		addCode(map, "93653", "SVT ablation", false);
		addCode(map, "93654", "VT ablation", false);
		addCode(map, "93655", "Additional SVT ablation", true);
		addCode(map, "93656", "AFB ablation", false);
		addCode(map, "93657", "Additional AFB ablation", true);

		// Ancillary to EP Testing/Ablation, Other EP Procedures
		addCode(map, "92960", "Cardioversion (external)", false);
		addCode(map, "92961", "Cardioversion (internal)", false);
		// skipped code 93640 used to test leads externally, not through device
		addCode(map, "93641", "DFT testing at time of ICD implant/replacement",
				false);
		addCode(map, "93642", "Transseptal cath", true);
		addCode(map, "93660", "Tilt table test", false);
		addCode(map, "93662", "Intracardiac echo", true);

		// Devices

		// New PPM
		addCode(map, "33206", "New or replacement PPM with new A lead", false);
		addCode(map, "33207", "New or replacement PPM with new V lead", false);
		addCode(map, "33208", "New or replacement PPM with new A and V leads",
				false);

		// Leads
		addCode(map, "33216", "Place single lead (A or V, PPM or ICD) only",
				false);
		addCode(map, "33217", "Place dual leads (PPM or ICD) only", false);

		// PPM Generators
		addCode(map, "33212",
				"Implant single chamber PPM generator, existing lead", false);
		addCode(map, "33213",
				"Implant dual chamber PPM generator, existing leads", false);
		addCode(map, "33214", "Upgrade single chamber to dual chamber PPM",
				false);
		addCode(map, "33221", "Implant CRT PPM generator, existing leads",
				false);

		// Replacement PPM
		addCode(map, "33227", "Single chamber PPM generator replacement", false);
		addCode(map, "33228", "Dual chamber PPM generator replacement", false);
		addCode(map, "33229", "CRT PPM generator replacement", false);
		addCode(map, "33233", "Removal of PPM generator without replacement",
				false);

		// ICD Generators
		addCode(map, "33240",
				"Place single chamber ICD generator, existing lead", false);
		addCode(map, "33230",
				"Place dual chamber ICD generator, existing leads", false);
		addCode(map, "33241", "Removal of ICD generator without replacement",
				false);
		addCode(map, "33231", "Place CRT ICD generator, existing leads", false);

		// Lead extraction
		addCode(map, "33234", "Removal electrode only single lead PPM system",
				false);
		addCode(map, "33235", "Removal electrodes only dual lead PPM system",
				false);
		addCode(map, "33244", "Removal one or more electrodes, ICD system",
				false);

		// New ICD/CRT
		addCode(map, "33249", "New ICD, single or dual, with leads", false);
		addCode(map, "33225",
				"Insertion of LV lead at time of ICD/PPM insertion", true);
		addCode(map, "33224",
				"Addition of LV lead to preexisting ICD/PPM system", false);

		// Replacement ICD/CRT
		addCode(map, "32229", "Replacement of CRT PPM generator only", false);
		addCode(map, "33262", "Replacement of single lead ICD generator only",
				false);
		addCode(map, "33263", "Replacement of dual lead ICD generator only",
				false);
		addCode(map, "33264", "Replacement of CRT ICD generator only", false);

		// ILR
		addCode(map, "33282", "Insertion of loop recorder", false);
		addCode(map, "33284", "Removal of loop recorder", false);

		// Misc
		addCode(map, "36620", "Arterial line placement", false);

		// SubQ ICD New Codes
		addCode(map, "0319T",
				"Implantation of SubQ ICD system (generator + electrode)",
				false);
		addCode(map, "0320T", "Insertion of SubQ defibrillator electrode only",
				false);
		addCode(map, "0321T",
				"Insertion of SubQ ICD generator only with existing electrode",
				false);
		addCode(map, "0322T", "Removal of SubQ ICD generator only", false);
		addCode(map, "0323T", "Removal and Replacement of SubQ ICD generator",
				false);
		addCode(map, "0324T", "Removal of SubQ ICD electrode", false);
		addCode(map, "0325T",
				"Repositioning of SubQ ICD electrode and/or generator", false);
		addCode(map, "0326T", "EP evaluation of SubQ ICD system", false);
		addCode(map, "0327T", "Interrogation of SubQ ICD system", false);
		addCode(map, "0328T", "Programming of SubQ ICD system", false);

		return Collections.unmodifiableMap(map);
	}

	public final static String[] afbAblationPrimaryCodeNumbers = { "93656" };
	public final static String[] svtAblationPrimaryCodeNumbers = { "93653" };
	public final static String[] vtAblationPrimaryCodeNumbers = { "93654" };
	public final static String[] avnAblationPrimaryCodeNumbers = { "93650" };
	public final static String[] epTestingPrimaryCodeNumbers = { "93620" };
	public final static String[] subQIcdPrimaryCodeNumbers = { "0319T",
			"0320T", "0321T", "0322T", "0323T", "0324T", "0325T", "0326T",
			"0327T", "0328T" };
	public final static String[] otherProcedurePrimaryCodeNumbers = { "33282",
			"33284", "93660", "92960", "92961" };

	public final static String[] ppmGeneratorReplacementCodeNumbers = {
			"33227", "33228" };

	public final static String[] ablationSecondaryCodeNumbers = { "93655",
			"93657", "93609", "93613", "93621", "93622", "93623", "93662",
			"93642", "36620" };

	public final static String[] avnAblationSecondaryCodeNumbers = { "93620",
			"33207", "33208", "33249", "33225", "92960", "92961" };

	public final static String[] deviceSecondaryCodeNumbers = { "92960",
			"92961" };

	public final static String[] afbAblationDisabledCodeNumbers = { "93621",
			"93642" };
	public final static String[] svtAblationDisabledCodeNumbers = { "93657" };
	public final static String[] vtAblationDisabledCodeNumbers = { "93657",
			"93609", "93613", "93622" };
	public final static String[] epTestingDisabledCodeNumbers = { "93657",
			"93655" };

	private final static void addCode(Map<String, Code> map, String codeNumber,
			String name, boolean isAddOn) {
		map.put(codeNumber, new Code(codeNumber, name, isAddOn));

	}

	public final static Code getCode(String code) {
		return allCodes.get(code);
	}

	public static Code[] getCodes(String[] codeNumbers) {
		Code[] codes = new Code[codeNumbers.length];
		for (int i = 0; i < codeNumbers.length; ++i)
			codes[i] = getCode(codeNumbers[i]);

		return codes;
	}

	public static String[] allCodeNumbersSorted() {
		String[] codeNumbers = new String[allCodes.size()];
		int i = 0;
		for (Map.Entry<String, Code> entry : allCodes.entrySet()) {
			codeNumbers[i++] = entry.getKey();
		}
		Arrays.sort(codeNumbers);
		return codeNumbers;
	}

	public static Code[] getAblationSecondaryCodes() {
		return getCodes(ablationSecondaryCodeNumbers);
	}

	public static Code[] getDeviceSecondaryCodes() {
		return getCodes(deviceSecondaryCodeNumbers);
	}

	public static int allCodesSize() {
		return allCodes.size();
	}
}
