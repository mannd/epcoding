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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class CodeAnalyzer {
	private final Code[] codes;
	private final boolean noPrimaryCodes;
	private final boolean noSecondaryCodes;
	private final boolean moduleHasNoSecondaryCodesNeedingChecking;
	private final SedationStatus sedationStatus;
	private final Context context;
	private boolean verbose = true;
	private boolean noAnalysis = false;

	// Note unicode doesn't consistently work on different Android devices.
	private static String WARNING = "! "; // ?? or \u26A0 or ! in triangle
	private static String ERROR = "!! "; // \u2620 or \u24CD (skull, x)
	private static String OK = ""; // \u263A smiley face
	private final static String UNICODE_WARNING = "\uD83D\uDE16";
	private final static String UNICODE_ERROR = "\uD83D\uDE31";
	private final static String UNICODE_OK = "\uD83D\uDE00";
	private final static String ASCII_WARNING = "! ";
	private final static String ASCII_ERROR = "!! ";
	private final static String ASCII_OK = "";

	// some special code lists
	private static final List<String> mappingCodes = Arrays.asList("93609",
			"93613");
	private static final Set<String> mappingCodeSet = new HashSet<>(
			mappingCodes);
	// Below doesn't include AV node ablation or VT ablation as mapping not
	// separately billable for those procedures.
	private static final List<String> ablationCodes = Arrays.asList("93653",
			"93656");
	private static final Set<String> ablationCodeSet = new HashSet<>(
			ablationCodes);

	private static final List<String> sedationCodes = Arrays.asList("99151", "99152",
		"99153", "99155", "99156", "99157");
	private static final Set<String> sedationCodeSet = new HashSet<>(sedationCodes);


	// Error messages and warnings
	private final static String DEFAULT_DUPLICATE_ERROR = "These codes shouldn't be combined.";
	private final static String DUPLICATE_MAPPING_ERROR = "You shouldn't combine 2D and 3D mapping.";
	private final static String DUPLICATE_CARDIOVERSION_ERROR = "You can't code for both internal and external cardioversion.";
	private final static String DUPLICATE_ABLATION_CODES_ERROR = "You can't combine primary ablation codes.";

	// There code errors occur when any 2 or more of the codes occur.
	private final static List<CodeError> duplicateCodeErrors = createDuplicateCodeErrors();

	private static List<CodeError> createDuplicateCodeErrors() {
		List<CodeError> codeErrors = new ArrayList<>();
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR,
				mappingCodes, DUPLICATE_MAPPING_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("92960", "92961"), DUPLICATE_CARDIOVERSION_ERROR));
		// don't use mult PPM implant, repl, or use implant and repl together
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("33206", "33207", "33208", "33227", "33228", "33229"),
				DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("33240", "33230", "33231", "33262", "33263", "33264"),
				DEFAULT_DUPLICATE_ERROR));

		// don't combine primary ablation codes, not sure what to do with
		// AVN ablation for this...
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93653", "93654", "93656"),
				DUPLICATE_ABLATION_CODES_ERROR));
		// SubQ ICD & leadless PPM  incompatible codes
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("33270", "33271"), DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("0389T", "0390T", "0391T"), DEFAULT_DUPLICATE_ERROR));
		// don't use one lead and 2 lead revision together
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR,
				Arrays.asList("33218", "33220"), DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR,
				Arrays.asList("33270", "93641"), DEFAULT_DUPLICATE_ERROR));
		return codeErrors;
	}

	// Special first code errors are sets of codes where the first code in
	// the set should not be used with any of the other codes.
	private final static List<CodeError> specialFirstCodeErrors = createSpecialFirstCodeErrors();

	private static List<CodeError> createSpecialFirstCodeErrors() {
		List<CodeError> codeErrors = new ArrayList<>();
		codeErrors
				.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays.asList(
						"33233", "33227", "33228", "33229", "33213", "33213",
						"33221"),
						"Don't use generator removal and insertion or replacement codes together."));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("33214", "33227", "33228", "33229"),
				"Don't use PPM upgrade code with generator replacement codes"));
		codeErrors
				.add(new CodeError(CodeError.WarningLevel.WARNING, Arrays
						.asList("93650", "93609", "93613"),
						"It is not clear if mapping codes can be combined with AV node ablation."));
		codeErrors
				.add(new CodeError(CodeError.WarningLevel.WARNING, Arrays
						.asList("93650", "93600", "93619", "93620"),
						"It is unclear if AV node ablation can be combined with EP testing codes."));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93656", "93621", "93462", "93609", "93613", "93662"),
				"Code(s) selected are already included in AFB Ablation."));
		codeErrors
				.add(new CodeError(
						CodeError.WarningLevel.ERROR,
						Arrays.asList("93653", "93657"),
						"AFB Ablation should not be added on to SVT ablation.  Use AFB ablation as the primary code."));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93654", "93657", "93609", "93613", "93622"),
				"Code(s) cannot be added to VT Ablation."));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR,
				Arrays.asList("93653", "93621", "93613", "93609"),
				"Code(s) selected are already included in SVT Ablation."));
		codeErrors.add(new CodeError(CodeError.WarningLevel.WARNING, Arrays
				.asList("93623", "93650", "93653", "93654", "93656"),
				"Recent coding changes may disallow bundling induce post IV drug with ablation."));
		// don't combine raw EP study codes with comprehensive codes or ablation codes
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93600", "93619", "93620", "93655", "93657"),
				DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93602", "93619", "93620", "93655", "93657"),
				DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93603", "93619", "93620", "93655", "93657"),
				DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93610", "93619", "93620", "93655", "93657"),
				DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93612", "93619", "93620", "93655", "93657"),
				DEFAULT_DUPLICATE_ERROR));
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("93618", "93619", "93620", "93655", "93657"),
				DEFAULT_DUPLICATE_ERROR));

		return codeErrors;
	}

	// use this for error when first code must be added on to other codes
	private final static List<CodeError> firstCodeNeedsOtherCodes = createFirstCodeNeedsOtherCodes();

	private static List<CodeError> createFirstCodeNeedsOtherCodes() {
		List<CodeError> codeErrors = new ArrayList<>();
		codeErrors.add(new CodeError(CodeError.WarningLevel.ERROR, Arrays
				.asList("33225", "33206", "33207", "33208", "33249", "33214"),
				"Must use 33225 with new device implant code"));
		return codeErrors;
	}

	private void setUpErrorSymbols(boolean useUnicode) {
		if (useUnicode) {
			WARNING = UNICODE_WARNING;
			ERROR = UNICODE_ERROR;
			OK = UNICODE_OK;
		}
		else {
			WARNING = ASCII_WARNING;
			ERROR = ASCII_ERROR;
			OK = ASCII_OK;
		}
	}

	public CodeAnalyzer(final List<Code> codes, final boolean noPrimaryCodes,
						final boolean noSecondaryCodes,
						final boolean moduleHasNoSecondaryCodesNeedingChecking,
						final SedationStatus status,
						boolean useUnicodeSymbols, final Context context) {
		this.codes = codes.toArray(new Code[0]);
		this.noPrimaryCodes = noPrimaryCodes;
		this.noSecondaryCodes = noSecondaryCodes;
		this.moduleHasNoSecondaryCodesNeedingChecking = moduleHasNoSecondaryCodesNeedingChecking;
		this.sedationStatus = status;
		this.context = context;
		setUpErrorSymbols(useUnicodeSymbols);
	}

	public CodeAnalyzer(final List<Code> codes, final SedationStatus status, boolean useUnicodeSymbols, final Context context) {
		this.codes = codes.toArray(new Code[0]);
		this.context = context;
		this.noPrimaryCodes = false;
		this.noSecondaryCodes = false;
		this.moduleHasNoSecondaryCodesNeedingChecking = true;
		this.sedationStatus = status;
		setUpErrorSymbols(useUnicodeSymbols);
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public void setNoAnalysis(boolean value) {
		this.noAnalysis = value;
	}

	public String analysis() {
		// quick exit for no codes selected
		if (noPrimaryCodes && noSecondaryCodes)
			return getMessage(WARNING, R.string.no_codes_selected_label,
					R.string.empty_message);
		// quick exit for noAnalysis
		if (noAnalysis)
			return getMessage(WARNING,
					R.string.no_code_analysis_performed_message,
					R.string.empty_message);
		// Now the details checks
		String message = "";
		if (noPrimaryCodes)
			message += getMessage(ERROR, R.string.no_primary_codes_message,
					R.string.no_primary_codes_verbose_message);
		if (noSecondaryCodes && !moduleHasNoSecondaryCodesNeedingChecking)
			message += getMessage(WARNING, R.string.no_secondary_codes_message,
					R.string.no_secondary_codes_verbose_message);
		if (allAddOnCodes())
			message += getMessage(ERROR, R.string.all_addons_error_message,
					R.string.all_addons_verbose_error_message);
		return getCodeAnalysis(message);
	}

	public String simpleAnalysis() {
		if (codes.length == 0) {
			return getMessage(WARNING, R.string.no_codes_selected_label,
					R.string.empty_message);
		}
		String message = "";
		return getCodeAnalysis(message);
	}

	private String getCodeAnalysis(String message) {
		String[] codeNumbers = new String[codes.length];
		for (int i = 0; i < codes.length; i++) {
			if (codes[i] != null)
				codeNumbers[i] = codes[i].getCodeNumber();
		}
		List<String> codeNumberList = Arrays.asList(codeNumbers);
		Set<String> codeNumberSet = new HashSet<>(codeNumberList);
		if (noMappingCodesForAblation(codeNumberList))
			message += getMessage(WARNING, R.string.no_mapping_codes_message,
					R.string.no_mapping_codes_verbose_message);
		message += getErrorCodes(codeNumberSet);
		message += getErrorCodesFirstSpecial(codeNumberSet);
		message += getErrorCodesFirstCodeNeedsOtherCodes(codeNumberSet);
		message += evaluateModifiers(codeNumberSet);
		message += evaluateSedationStatus(codeNumberSet);
		if (message.length() == 0) {
			message = getMessage(OK, R.string.no_code_errors_message, R.string.empty_message);
		}
		return message;
	}

	private boolean allAddOnCodes() {
		boolean allAddOns = true;
		for (Code code : codes) {
			if (code != null && !code.isAddOn()) {
				allAddOns = false;
				break;
			}
		}
		return allAddOns;
	}

	// This function must always return false since the 2022 coding
	// update, as all ablations now including mapping.  Original code
	// left commented out for historical purposes.
	private boolean noMappingCodesForAblation(final List<String> codeNumbers) {
		return false;
//		boolean noMappingCodes = true;
//		boolean hasAblationCodes = false;
//		for (String codeNumber : codeNumbers) {
//			if (ablationCodeSet.contains(codeNumber))
//				hasAblationCodes = true;
//			if (mappingCodeSet.contains(codeNumber))
//				noMappingCodes = false;
//		}
//		return (hasAblationCodes && noMappingCodes);
	}

	// This tests for 2 or more codes in a code set and is used
	// if 2 or more codes should not be used together
	private String getErrorCodes(final Set<String> codeNumbers) {
		StringBuilder errorCodes = new StringBuilder();
		for (CodeError codeError : duplicateCodeErrors) {
			List<String> badCombo = codeError.getCodes();
			List<String> badCodeList = hasBadCombo(badCombo, codeNumbers);
			if (badCodeList.size() > 1) {
				errorCodes.append(addToErrorMessage(codeError,
						badCodeList));
			}
		}
		return errorCodes.toString();
	}

	// This tests for errors where a single code should not be used with
	// multiple other codes. E.g. PPM removal should not be used with any of the
	// specially, i.e. skips testing if the first number is not present
	private String getErrorCodesFirstSpecial(final Set<String> codeNumbers) {
		StringBuilder errorCodes = new StringBuilder();
		// first see if first error code is contained in codeNumbers set
		for (CodeError codeError : specialFirstCodeErrors) {
			List<String> badCombo = codeError.getCodes();
			if (codeNumbers.contains(badCombo.get(0))) {
				List<String> badCodeList = hasBadCombo(badCombo, codeNumbers);
				if (badCodeList.size() > 1) {
					errorCodes.append(addToErrorMessage(codeError,
							badCodeList));
				}
			}
		}
		return errorCodes.toString();
	}

	// This tests to see if at least one necessary accompanying code is present
	// if first code is present
	private String getErrorCodesFirstCodeNeedsOtherCodes(
			final Set<String> codeNumbers) {
		StringBuilder errorCodes = new StringBuilder();
		for (CodeError codeError : firstCodeNeedsOtherCodes) {
			List<String> badCombo = codeError.getCodes();
			if (codeNumbers.contains(badCombo.get(0))) {
				List<String> badCodeList = hasBadCombo(badCombo, codeNumbers);
				if (badCodeList.size() == 1) { // oops only first code present
					errorCodes.append(addToErrorMessage(codeError,
							badCodeList));
				}
			}
		}
		return errorCodes.toString();
	}

	private String addToErrorMessage(final CodeError codeError, final List<String> badCodeList) {
		CodeError.WarningLevel warningLevel = codeError.getWarningLevel();
		String warning;
		switch (warningLevel) {
		case NONE:
			warning = OK;
			break;
		case WARNING:
			warning = WARNING;
			break;
		case ERROR:
		default:
			warning = ERROR;
			break;
		}
		return getMessageFromStrings(warning, getCodeString(badCodeList),
				codeError.getWarningMessage());
	}

	// returns list of matching bad codes
	private List<String> hasBadCombo(final List<String> badCodes,
			final Set<String> codes) {
		List<String> badCodeList = new ArrayList<>();
		for (String badCode : badCodes) {
			if (codes.contains(badCode)) {
				badCodeList.add(badCode);
			}
		}
		return badCodeList;
	}

	// returns string of codes in this format: "[99999, 99991]"
	private String getCodeString(final List<String> codeList) {
		return codeList.toString();
	}

	private String getMessage(final String threat, final int brief,
			final int details) {
		return getMessageFromStrings(threat, context.getString(brief),
				context.getString(details));
	}

	private String getMessageFromStrings(final String threat,
			final String brief, final String details) {
		return "\n" + threat + brief + (verbose ? " " + details : "") + "\n";
	}


	// Add Q0 modifier to ICD implant or generator replacement codes"
	//				+ " if indication is primary prevention
	private String evaluateModifiers(Set<String> codeNumbers) {
		String message = "";
		boolean q0ModifierFound = false;
		List<String> badCodeList = new ArrayList<>();
		for (String number : codeNumbers) {
			Code code = Codes.getCode(number);
			if (code == null) {
				continue;
			}
			for (Modifier modifier : code.getModifiers()) {
				if (modifier.getNumber().equals("Q0")) {
					badCodeList.add(code.getCodeNumber());
					q0ModifierFound = true;
				}
				if (badCodeList.size() > 0) {
					CodeError error = new CodeError(CodeError.WarningLevel.NONE,
							null, "Q0 modifier indicates primary prevention ICD.  " +
					"Remove Q0 modifier for other ICD indications.");
					message = addToErrorMessage(error, badCodeList);
				}
			}
		}
		if (!q0ModifierFound) {
			Set<String> icdCodeNumberSet = new HashSet<>();
			icdCodeNumberSet.add("33249");
			icdCodeNumberSet.add("33262");
			icdCodeNumberSet.add("33263");
			icdCodeNumberSet.add("33264");
			for (String codeNumber : codeNumbers) {
				if (icdCodeNumberSet.contains(codeNumber)) {
					badCodeList.add(codeNumber);
				}
				if (badCodeList.size() > 0) {
					CodeError error = new CodeError(CodeError.WarningLevel.NONE,
							null, "Add Q0 modifier to ICD implant or generator replacement codes " +
					"if indication is primary prevention");
					message = addToErrorMessage(error, badCodeList);
				}
			}
		}
		return message;
	}

	private String rawSedationCodesUsedError(final Set<String> codeNumbers) {
		String message = "";
		List<String> badCodeNumbers = new ArrayList<>();
		if (rawSedationCodesUsed(codeNumbers)) {
			for (String number : codeNumbers) {
				if (sedationCodeSet.contains(number)) {
					badCodeNumbers.add(number);
				}
			}
			if (badCodeNumbers.size() > 0) {
				CodeError error = new CodeError(CodeError.WarningLevel.WARNING,
						null, "Raw sedation codes selected." +
						"  Sedation codes may be inconsistent.  Further analysis" +
						" of sedation codes will not be performed.");
				message = addToErrorMessage(error, badCodeNumbers);
			}
		}
		return message;
	}

	private boolean rawSedationCodesUsed(Set<String> codeNumbers) {
		// We'll use a different logic from iOS version:
		// if sedationStatus is incompatible with sedation codes
		// , and yet there are sedation codes...
		if (SedationStatus.canHaveSedationCodes(sedationStatus)) {
			return false;
		}
		boolean rawCodesUsed = false;
		for (String codeNumber : codeNumbers) {
			if (sedationCodeSet.contains(codeNumber)) {
				rawCodesUsed = true;
				break;
			}
		}
		return rawCodesUsed;
	}

	private String evaluateSedationStatus(Set<String> codeNumbers) {
		String message;
		message = rawSedationCodesUsedError(codeNumbers);
		// no further sedation analysis if raw sedation codes used
		if (message.length() > 0) {
			return message;
		}
		switch (sedationStatus) {
			case Unassigned:
				message = getMessage(WARNING, R.string.unassigned_sedation_message,
						R.string.unassigned_sedation_verbose_message);
				break;
			case None:
				message = getMessage(OK, R.string.no_sedation_used_message, R.string.empty_message);
				break;
			case LessThan10Mins:
				message = getMessage(OK, R.string.short_sedation_time_message, R.string.empty_message);
				break;
			case OtherMDCalculated:
				message = getMessage(WARNING, R.string.sedation_other_md_message,
						R.string.sedation_other_md_verbose_message);
				break;
			case AssignedSameMD:
			default:
				break;
		}
		return message;
	}


}
