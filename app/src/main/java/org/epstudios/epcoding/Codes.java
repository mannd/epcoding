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
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Codes {

	private final static Map<String, Code> allCodes = createMap();

    private static Map<String, List<Modifier>> defaultModifiers;

	private static Map<String, Code> createMap() {
		Map<String, Code> map = new HashMap<>();
		// addCode(map, String codeNumber, String codeName, boolean isAddOn)

		// Devices /////////////////////////////////////////
		// SubQ ICD New Codes
		addCode(map, "33270",
				"New or replacement SubQ ICD system, including SubQ lead, and including testing",
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
		addCode(map, "33274", "New or replacement leadless PPM", false);
		addCode(map, "33275", "Transcatheter removal of leadless PPM", false);

		// hopefully rarely needed!
		addCode(map, "33010", "Pericardiocentesis", false);


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
		addCode(map, "33262", "Single lead ICD generator or SubQ ICD generator replacement", false);
		addCode(map, "33263", "Dual lead ICD generator replacement", false);
		addCode(map, "33264", "CRT ICD generator replacement", false);

		// ILR
		addCode(map, "33285", "Insertion of loop recorder", false);
		addCode(map, "33286", "Removal of loop recorder", false);

		addCode(map, "33999",
				"Unlisted surgical procedure, e.g. SubQ array lead", false);

		// Misc
		addCode(map, "35476", "Venous angioplasty", false);
		addCode(map, "36620", "Arterial line placement", false);

		// Imaging
		// note fluoroscopy included in device codes, but this code
		// used e.g to evaluate a lead such as a Riata
		addCode(map, "76000", "Fluoroscopic lead evaluation", false);
		addCode(map, "76937", "Ultrasonic guidance for vascular access", false);

		// Ablation and EP testing codes

		// TEE
		addCode(map, "93312", "TEE, complete", false);
		addCode(map, "93315", "TEE. complete, congenital heart disease", false);
		addCode(map, "93320", "Doppler imaging, complete", true);
		addCode(map, "93321", "Doppler imaging, follow-up or limited study", true);
		addCode(map, "93325", "Doppler color flow velocity mapping", true);
		addCode(map, "93355", "TEE for guidance of transcatheter vascular interventions, such as TAVR, LAA closure, etc.", false);

		// EP Testing and Mapping ///////////////////////
		addCode(map, "93600", "His bundle recording", false);
		addCode(map, "93602", "Intra-atrial recording", false);
		addCode(map, "93603", "Right ventricular recording", false);
		addCode(map, "93610", "Intra-atrial pacing", false);
		addCode(map, "93612", "Intraventricular pacing", false);
		addCode(map, "93618", "Induction of arrhythmia", false);

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

		addCode(map, "93724", "Noninvasive programmed stimulation", false);

		// Unlisted procedure
		addCode(map, "93799", "Unlisted procedure", false);

		// New sedation codes, 2017
		addCode(map, "99151", "Moderate sedation, same MD, initial 15 min, pt < 5 y/o", false);
		addCode(map, "99152", "Moderate sedation, same MD, initial 15 min, pt ≥ 5 y/o", false);
		addCode(map, "99153", "Moderate sedation, same MD, each additional 15 min", true);
		addCode(map, "99155", "Moderate sedation, different MD, initial 15 min, pt < 5 y/o", false);
		addCode(map, "99156", "Moderate sedation, different MD, initial 15 min, pt ≥ 5 y/o", false);
		addCode(map, "99157", "Moderate sedation, different MD, each additional 15 min", true);


		return Collections.unmodifiableMap(map);
	}

	public final static String[] afbAblationPrimaryCodeNumbers = { "93656" };
	public final static String[] svtAblationPrimaryCodeNumbers = { "93653" };
	public final static String[] vtAblationPrimaryCodeNumbers = { "93654" };
	public final static String[] avnAblationPrimaryCodeNumbers = { "93650" };
	public final static String[] epTestingPrimaryCodeNumbers = { "93619",
			"93620" };
    public final static String[] subQIcdPrimaryCodeNumbers = { "33270", "33262", "33271",
			"33272", "33273", "33274", "33275" };
	public final static String[] otherProcedurePrimaryCodeNumbers = { "33285",
			"33286", "93660", "92960", "92961", "76000" };

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

//	public static int allCodesSize() {
//		return allCodes.size();
//	}
//
	public static List<String> searchCodes(String searchString) {
		List<String> result = new ArrayList<>();
		for (Map.Entry<String, Code> entry : allCodes.entrySet()) {
			if (entry.getValue().codeContains(searchString)) {
				result.add(entry.getValue().getUnformattedNumberFirst());
			}
		}
		return result;
	}

	private static Map<String, List<Modifier>> defaultModifiers() {
        if (defaultModifiers == null) {
            defaultModifiers = new HashMap<>();
            // Modifier 26 alone
            List<Modifier> modifierList_26 = new ArrayList<>();
            modifierList_26.add(Modifiers.getModifierForNumber("26"));
            defaultModifiers.put("93312", modifierList_26);
            defaultModifiers.put("93609", modifierList_26);
            defaultModifiers.put("93620", modifierList_26);
            defaultModifiers.put("93619", modifierList_26);
            defaultModifiers.put("93621", modifierList_26);
			defaultModifiers.put("93622", modifierList_26);
			defaultModifiers.put("93662", modifierList_26);
            defaultModifiers.put("76000", modifierList_26);
            defaultModifiers.put("93641", modifierList_26);
            defaultModifiers.put("93642", modifierList_26);
            defaultModifiers.put("93660", modifierList_26);
            // Modifier 26 and 59
            List<Modifier> modifierList_26_59 = new ArrayList<>();
            modifierList_26_59.add(Modifiers.getModifierForNumber("26"));
            modifierList_26_59.add(Modifiers.getModifierForNumber("59"));
            defaultModifiers.put("93623", modifierList_26_59);
            // Modifier Q0
            List<Modifier> modifierList_Q0 = new ArrayList<>();
            modifierList_Q0.add(Modifiers.getModifierForNumber("Q0"));
            defaultModifiers.put("33249", modifierList_Q0);
            defaultModifiers.put("33262", modifierList_Q0);
            defaultModifiers.put("33263", modifierList_Q0);
            defaultModifiers.put("33264", modifierList_Q0);
            // Modifier 59
            List<Modifier> modifierList_59 = new ArrayList<>();
            modifierList_59.add(Modifiers.getModifierForNumber("59"));
            defaultModifiers.put("33222", modifierList_59);
            defaultModifiers.put("33223", modifierList_59);
            defaultModifiers.put("33620", modifierList_59);
        }
        return defaultModifiers;
    }

    public static void clearMultipliersAndModifiers(List<Code> array) {
        for (Code code : array) {
            code.setMultiplier(0);
            code.clearModifiers();
        }
    }

	public static void loadDefaultModifiers(List<Code> array) {
        for (Code code : array) {
            loadDefaultModifiersForCode(code);
        }
    }

	private static void loadDefaultModifiersForCode(Code code) {
        List<Modifier> modifiers = defaultModifiers().get(code.getCodeNumber());
        if (modifiers != null) {
            code.addModifiers(modifiers);
        }
    }

    public static void loadSavedModifiers(List<Code> array, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
        for (Code code : array) {
            loadSavedModifiersForCode(code, prefs);
         }
	}

	public static void loadTempAddedModifiers(List<Code> array, Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		for (Code code : array) {
			loadTempAddedModifiersForCode(code, prefs);
		}
	}


	private static void loadSavedModifiersForCode(Code code, SharedPreferences prefs) {
        Set<String> modifierNumbers = prefs.getStringSet(code.getCodeNumber(),
                null);
        if (modifierNumbers != null) {
            code.clearModifiers();
            for (String s : modifierNumbers) {
                Modifier modifier = Modifiers.getModifierForNumber(s);
                code.addModifier(modifier);
            }
        }
    }

	private static void loadTempAddedModifiersForCode(Code code, SharedPreferences prefs) {
		Set<String> modifierNumbers = prefs.getStringSet(Constants.TEMP + code.getCodeNumber(),
				null);
		if (modifierNumbers != null) {
			code.clearModifiers();
			for (String s : modifierNumbers) {
				Modifier modifier = Modifiers.getModifierForNumber(s);
				code.addModifier(modifier);
			}
		}
	}


	public static void resetSavedModifiers(List<Code> codes, Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        for (Code code : codes) {
           resetSavedModifiersForCode(code, prefs);
        }
    }

	public static void resetTempAddedModifiers(List<Code> codes, Context context) {
		SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
		for (Code code : codes) {
			resetTempAddedModifiersForCode(code, prefs);
		}
	}

	private static void resetTempAddedModifiersForCode(Code code, SharedPreferences prefs) {
		resetModifiersForCode(code, prefs, Constants.TEMP + code.getCodeNumber());
	}

    private static void resetSavedModifiersForCode(Code code, SharedPreferences prefs) {
		resetModifiersForCode(code, prefs, code.getCodeNumber());
    }

	private static void resetModifiersForCode(Code code, SharedPreferences prefs, String codeNumber) {
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.remove(codeNumber);
		prefsEditor.apply();
		code.clearModifiers();
	}

	// returns code number or null
    public static Code setModifiersForCode(String[] codeAndModifiers) {
		if (codeAndModifiers == null || codeAndModifiers.length < 1) {
			return null;
		}
		int length = codeAndModifiers.length;
		String codeNumber = codeAndModifiers[0];
		Code code = Codes.getCode(codeNumber);
		if (code != null) {
			code.clearModifiers();
			for (int i = 1; i < length; i++) {
				code.addModifier(Modifiers.getModifierForNumber(codeAndModifiers[i]));
			}
		}
		return code;
	}
}
