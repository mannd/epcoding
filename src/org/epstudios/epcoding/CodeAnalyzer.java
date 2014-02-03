package org.epstudios.epcoding;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.content.Context;

public class CodeAnalyzer {
	private final Code[] codes;
	private final boolean noPrimaryCodes;
	private final boolean noSecondaryCodes;
	private final boolean moduleHasNoSecondaryCodes;
	private final Context context;
	private boolean verbose = false;

	private static final List<String> mappingCodes = Arrays.asList("93609",
			"93613");
	private static final Set<String> mappingCodeSet = new HashSet<String>(
			mappingCodes);
	// below doesn't include AV node ablation or VT ablation as mapping not
	// separately billable
	private static final List<String> ablationCodes = Arrays.asList("93653",
			"93656");
	private static final Set<String> ablationCodeSet = new HashSet<String>(
			ablationCodes);

	// also need list of codes that can be compared to a single code,
	// e.g. 76000 should not be used with any other device code

	public CodeAnalyzer(final Code[] codes, final boolean noPrimaryCodes,
			final boolean noSecondaryCodes,
			final boolean moduleHasNoSecondaryCodes, final Context context) {
		this.codes = codes;
		this.noPrimaryCodes = noPrimaryCodes;
		this.noSecondaryCodes = noSecondaryCodes;
		this.moduleHasNoSecondaryCodes = moduleHasNoSecondaryCodes;
		this.context = context;
	}

	public void setVerbose(boolean verbose) {
		this.verbose = verbose;
	}

	public String analysis() {
		// Note unicode doesn't consistently work on different devices.
		// use ASCII! Maybe will work in iOS?
		final String WARNING = "?? "; // \u26A0 "; // ! in triangle
		final String ERROR = "!! "; // "\u2620 "; // skull and bones
									// "\u24CD "; x in circle
		final String OK = ""; // "\u263A "; // smiley face
		// quick exit for no codes selected
		if (noPrimaryCodes && noSecondaryCodes)
			return getMessage(WARNING, R.string.no_codes_selected_label,
					R.string.empty_message);
		String message = "";
		if (noPrimaryCodes)
			message += getMessage(ERROR, R.string.no_primary_codes_message,
					R.string.no_primary_codes_verbose_message);
		if (noSecondaryCodes && !moduleHasNoSecondaryCodes)
			message += getMessage(WARNING, R.string.no_secondary_codes_message,
					R.string.no_secondary_codes_verbose_message);
		if (allAddOnCodes())
			message += getMessage(ERROR, R.string.all_addons_error_message,
					R.string.all_addons_verbose_error_message);
		// check for forbidden code combos
		// first make array of code numbers
		String[] codeNumbers = new String[codes.length];
		for (int i = 0; i < codes.length; ++i) {
			if (codes[i] != null)
				codeNumbers[i] = codes[i].getCodeNumber();
		}
		List<String> codeNumberList = Arrays.asList(codeNumbers);
		Set<String> codeNumberSet = new HashSet<String>(codeNumberList);
		if (containsCodeNumbers(mappingCodes, codeNumberSet))
			message += getCodingErrorMessage(ERROR, mappingCodes,
					R.string.duplicate_mapping_codes_verbose_error_message);
		if (noMappingCodesForAblation(codeNumberList))
			message += getMessage(WARNING, R.string.no_mapping_codes_message,
					R.string.no_mapping_codes_verbose_message);
		if (avnAblationHasMappingCodes(codeNumberSet))
			message += getMessage(WARNING,
					R.string.mapping_with_avn_ablation_warning,
					R.string.mapping_with_avn_ablation_verbose_warning);
		if (message.length() == 0) // no errors!
			message = OK + context.getString(R.string.no_code_errors_message);
		return message;
	}

	private boolean allAddOnCodes() {
		boolean allAddOns = true;
		for (int i = 0; i < codes.length; ++i) {
			if (codes[i] != null && !codes[i].isAddOn())
				allAddOns = false;
		}
		return allAddOns;
	}

	private boolean containsCodeNumbers(final List<String> badNumbers,
			final Set<String> codeNumberSet) {
		return codeNumberSet.containsAll(badNumbers);
	}

	private boolean noMappingCodesForAblation(final List<String> codeNumbers) {
		boolean noMappingCodes = true;
		boolean hasAblationCodes = false;
		for (String codeNumber : codeNumbers) {
			if (ablationCodeSet.contains(codeNumber))
				hasAblationCodes = true;
			if (mappingCodeSet.contains(codeNumber))
				noMappingCodes = false;
		}
		return (hasAblationCodes && noMappingCodes);
	}

	private boolean avnAblationHasMappingCodes(final Set<String> codeNumbers) {
		return codeNumbers.contains("93650")
				&& (codeNumbers.contains("93609") || codeNumbers
						.contains("93613"));

	}

	// returns string of codes in this format: "[99999, 99991]"
	private String getCodeString(final List<String> codeList) {
		return codeList.toString();
	}

	private String getCodingErrorMessage(final String threat,
			final List<String> codeList, final int details) {
		return getMessageFromStrings(threat, getCodeString(codeList),
				context.getString(details));
	}

	private String getMessage(final String threat, final int brief,
			final int details) {
		return getMessageFromStrings(threat, context.getString(brief),
				context.getString(details));
	}

	private String getMessageFromStrings(final String threat,
			final String brief, final String details) {
		return threat + (verbose ? brief + " " + details : brief) + "\n";
	}
}
