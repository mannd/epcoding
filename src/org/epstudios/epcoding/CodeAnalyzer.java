package org.epstudios.epcoding;

import android.content.Context;

public class CodeAnalyzer {
	private final Code[] codes;
	private final boolean noPrimaryCodes;
	private final boolean noSecondaryCodes;
	private final boolean moduleHasNoSecondaryCodes;
	private final Context context;
	private boolean verbose = false;

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

		String message = "";
		if (noPrimaryCodes && noSecondaryCodes)
			message = getMessage(WARNING, R.string.no_codes_selected_label,
					R.string.empty_message);
		else {
			if (noPrimaryCodes)
				message += getMessage(ERROR, R.string.no_primary_codes_message,
						R.string.no_primary_codes_verbose_message);
			if (noSecondaryCodes && !moduleHasNoSecondaryCodes)
				message += getMessage(WARNING,
						R.string.no_secondary_codes_message,
						R.string.no_secondary_codes_verbose_message);
			if (allAddOnCodes())
				message += getMessage(ERROR, R.string.all_addons_error_message,
						R.string.all_addons_verbose_error_message);
			// other processing here
			if (message.length() == 0) // no errors!
				message = OK
						+ context.getString(R.string.no_code_errors_message);
		}
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

	private String getMessage(final String threat, final int brief,
			final int details) {
		return threat
				+ (verbose ? context.getString(brief) + " "
						+ context.getString(details) : context.getString(brief))
				+ "\n";
	}
}
