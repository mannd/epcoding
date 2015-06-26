package org.epstudios.epcoding;

import android.app.AlertDialog;
import android.content.Context;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.Map;

class Utilities {
	public static Map<String, CodeCheckBox> createCheckBoxLayoutAndCodeMap(
			Code[] codes, LinearLayout layout, Context context,
			boolean showCodeFirst) {
		Map<String, CodeCheckBox> map = new LinkedHashMap<>();
		for (int i = 0; i < codes.length; ++i) {
			CodeCheckBox codeCheckBox = new CodeCheckBox(context);
			codeCheckBox.setCodeFirst(showCodeFirst);
			codeCheckBox.setCode(codes[i]);
			map.put(codes[i].getCodeNumber(), codeCheckBox);
			layout.addView(codeCheckBox);
		}
		return map;
	}

	public static String codeAnalysis(final Code[] codes,
			final boolean noPrimaryCodes, final boolean noSecondaryCodes,
			final boolean moduleHasNoSecondaryCodes, boolean noAnalysis,
			boolean verbose, Context context) {
		CodeAnalyzer analyzer = new CodeAnalyzer(codes, noPrimaryCodes,
				noSecondaryCodes, moduleHasNoSecondaryCodes, context);
		// no analysis for all procedures module
		analyzer.setNoAnalysis(noAnalysis);
		analyzer.setVerbose(verbose);
		return analyzer.analysis();
	}

	public static String simpleCodeAnalysis(final Code[] codes, Context context) {
		CodeAnalyzer analyzer = new CodeAnalyzer(codes, context);
		analyzer.setVerbose(true); // if you are using the wizard you need
									// verbose messages.
		return analyzer.simpleAnalysis();
	}

	public static void displayMessage(String title, String message,
			Context context) {
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setMessage(message);
		dialog.setTitle(title);
		dialog.show();
	}

}
