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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

class Codes {

	private final static Map<String, Code> allCodes = createMap();

	private static Map<String, Code> createMap() {
		Map<String, Code> map = new HashMap<String, Code>();
		// addCode(map, String codeNumber, String codeName, boolean isAddOn)

		// Devices ////////////////////////////////////////
		// SubQ ICD New Codes
		addCode(map, "33270",
				"New or replacement SubQ ICD system, includes testing",
				false);
		addCode(map, "33271", "Insertion of SubQ defibrillator electrode only",
				false);
		addCode(map, "33272", "Removal of SubQ ICD electrode", false);
		addCode(map, "33273",
				"Repositioning of SubQ ICD electrode", false);

		// Surgical approach - surgical codes not included in EP Coding
		// addCode(map, "33202",
		// "Insert epicardial electrode(s), open approach",
		// false);
		// addCode(map, "33203",
		// "Insert epicardial electrode(s) endoscopically",
		// false);


		// Leadless PPM
		addCode(map, "0387T", "New or replacement leadless PPM", false);
		addCode(map, "0388T", "Transcatheter removal of leadless PPM", false);
		addCode(map, "0389T", "Programming evaluation/testing of leadless PPM", false);
		addCode(map, "0390T", "Peri-procedural device evaluation/testing of leadless PPM", false);
		addCode(map, "0391T", "Interrogation of leadless PPM", false);

// New PPM with leads
		addCode(map, "33206", "New or replacement PPM with new A lead", false);
		addCode(map, "33207", "New or replacement PPM with new V lead", false);
		addCode(map, "33208", "New or replacement PPM with new A and V leads",
				false);

		// Temporary transvenous electrodes
		addCode(map, "33210", "Insert temporary transvenous pacing electrode",
				false);
		addCode(map, "33211",
				"Insert temporary transvenous A and V pacing electrodes", false);

		// PPM Generators
		addCode(map, "33212",
				"Implant single chamber PPM generator, existing lead", false);
		addCode(map, "33213",
				"Implant dual chamber PPM generator, existing leads", false);
		addCode(map, "33214", "Upgrade single chamber to dual chamber PPM",
				false);

		// Leads
		addCode(map, "33215", "Repositioning of PPM or ICD electrode", false);
		addCode(map, "33216", "Implant single lead (A or V, PPM or ICD) only",
				false);
		addCode(map, "33217", "Implant dual leads (PPM or ICD) only", false);
		addCode(map, "33218", "Repair of one (PPM or ICD) electrode", false);
		addCode(map, "33220", "Repair of two (PPM or ICD) electrodes", false);

		// Pocket revisions
		addCode(map, "33222", "PPM pocket revision", false);
		addCode(map, "33223", "ICD pocket revision", false);

		addCode(map, "33221", "Implant CRT PPM generator, existing leads",
				false);
		addCode(map, "33224",
				"Addition of LV lead to preexisting ICD/PPM system", false);
		addCode(map, "33225", "Implant LV lead at time of ICD/PPM insertion",
				true);
		addCode(map, "33226", "Repositioning of previously implanted LV lead",
				false);

		// Replacement/removal/extraction
		addCode(map, "33227", "Single chamber PPM generator replacement", false);
		addCode(map, "33228", "Dual chamber PPM generator replacement", false);
		addCode(map, "33229", "CRT PPM generator replacement", false);
		addCode(map, "33230",
				"Implant dual chamber ICD generator, existing leads", false);
		addCode(map, "33231", "Implant CRT ICD generator, existing leads",
				false);
		addCode(map, "33233", "Removal of PPM generator without replacement",
				false);
		addCode(map, "33234", "Removal electrode only single lead PPM system",
				false);
		addCode(map, "33235", "Removal electrodes only dual lead PPM system",
				false);
		addCode(map, "33240",
				"Implant single chamber ICD generator, existing lead", false);
		addCode(map, "33241", "Removal of ICD generator without replacement",
				false);
		// 33243 is removal of ICD lead by thoracotomy
		addCode(map, "33244", "Removal one or more electrodes, ICD system",
				false);

		// New ICD/CRT
		addCode(map, "33249", "New ICD, single or dual, with leads", false);

		// Replacement ICD/CRT
		addCode(map, "33262", "Single lead ICD generator replacement", false);
		addCode(map, "33263", "Dual lead ICD generator replacement", false);
		addCode(map, "33264", "CRT ICD generator replacement", false);

		// ILR
		addCode(map, "33282", "Insertion of loop recorder", false);
		addCode(map, "33284", "Removal of loop recorder", false);

		addCode(map, "33999",
				"Unlisted surgical procedure, e.g. SubQ array lead", false);

		// Misc
		addCode(map, "35476", "Venous angioplasty", false);
		addCode(map, "36620", "Arterial line placement", false);

		// note fluoroscopy included in device codes, but this code
		// used e.g to evaluate a lead such as a Riata
		addCode(map, "76000", "Fluoroscopic lead evaluation", false);
		// Ablation and EP testing codes

		// EP Testing and Mapping ///////////////////////
		addCode(map, "93600", "His bundle recording only", false);

		addCode(map, "93609", "2D mapping", true);
		addCode(map, "93613", "3D mapping", true);
		addCode(map, "93619",
				"EP testing without attempted arrhythmia induction", false);
		addCode(map, "93620", "EP testing with attempted arrhythmia induction",
				false);
		addCode(map, "93621", "LA pacing & recording", true);
		addCode(map, "93622", "LV pacing & recording", true);
		addCode(map, "93623", "Induce post IV drug", true);
		addCode(map, "93624", "Follow-up EP testing", false);
		addCode(map, "93631", "Intra-op mapping", false);

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
		// note, not clear if 92961 can be used with ICD cardioversion

		// skipped code 93640 used to test leads externally, not through device
		addCode(map, "93641",
				"DFT testing using ICD at time of ICD implant/replacement",
				false);
		addCode(map, "93642",
				"DFT testing using ICD not at time of implant/replacement",
				false);
		addCode(map, "93462", "Transseptal cath", true);
		addCode(map, "93660", "Tilt table test", false);
		addCode(map, "93662", "Intracardiac echo", true);

		// Unlisted procedure
		addCode(map, "93799", "Unlisted procedure", false);

		return Collections.unmodifiableMap(map);
	}

	public final static String[] afbAblationPrimaryCodeNumbers = { "93656" };
	public final static String[] svtAblationPrimaryCodeNumbers = { "93653" };
	public final static String[] vtAblationPrimaryCodeNumbers = { "93654" };
	public final static String[] avnAblationPrimaryCodeNumbers = { "93650" };
	public final static String[] epTestingPrimaryCodeNumbers = { "93619",
			"93620" };
    public final static String[] subQIcdPrimaryCodeNumbers = { "33270", "33271",
			"33272", "33273", "0387T", "0388T", "0389T", "0390T", "0391T" };
	public final static String[] otherProcedurePrimaryCodeNumbers = { "33282",
			"33284", "93660", "92960", "92961", "76000" };

	public final static String[] newPpmPrimaryCodeNumbers = { "33206", "33207",
			"33208", "33225" };

	public final static String[] ppmGeneratorReplacementPrimaryCodeNumbers = {
			"33227", "33228", "33229" };

	public final static String[] newIcdPrimaryCodeNumbers = { "33249", "33225" };

	public final static String[] IcdReplacementPrimaryCodeNumbers = { "33262",
			"33263", "33264" };

	public final static String[] deviceUpgradePrimaryCodeNumbers = { "33216",
			"33217", "33224", "33215", "33226", "33233", "33241", "33234",
			"33235", "33244", "33214", "33206", "33207", "33208", "33249",
			"33225", "33212", "33213", "33240", "33230", "33231", "33222",
			"33223", "33218", "33220" };

	public final static String[] ablationSecondaryCodeNumbers = { "93655",
			"93657", "93609", "93613", "93621", "93622", "93623", "93662",
			"93462", "36620" };

	// epTestingSecondaryCodeNumbers need to include device implants

	public final static String[] avnAblationSecondaryCodeNumbers = { "93600",
			"93619", "93620", "93609", "93613", "33207", "33208", "33210",
			"33249", "33225", "92960", "92961" };

	private final static String[] deviceSecondaryCodeNumbers = { "33210",
			"33218", "33220", "92960", "92961" };

	public final static String[] upgradeSecondaryCodeNumbers = { "33210",
			"33218", "33220", "92960", "92961" };

	public final static String[] icdDeviceSecondaryCodeNumbers = { "93641",
			"33210", "33999", "92960", "92961" };

	public final static String[] icdReplacementSecondaryCodeNumbers = {
			"93641", "33210", "33218", "33220", "92960", "92961" };

	public final static String[] afbAblationDisabledCodeNumbers = { "93621",
			"93462" };
	public final static String[] svtAblationDisabledCodeNumbers = { "93657" };
	public final static String[] vtAblationDisabledCodeNumbers = { "93657",
			"93609", "93613", "93622" };
	public final static String[] epTestingDisabledCodeNumbers = { "93657",
			"93655" };

	private static void addCode(Map<String, Code> map, String codeNumber,
			String name, boolean isAddOn) {
		map.put(codeNumber, new Code(codeNumber, name, isAddOn));

	}

	public static Code getCode(String code) {
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

	public static List<String> searchCodes(String searchString) {
		List<String> result = new ArrayList<String>();
		for (Map.Entry<String, Code> entry : allCodes.entrySet()) {
			if (entry.getValue().codeContains(searchString)) {
				result.add(entry.getValue().getUnformattedNumberFirst());
			}
		}
		return result;
	}
}
