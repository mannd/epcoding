package org.epstudios.epcoding;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.Map;

class Utilities {
    private static final String EPCODING = ProcedureDetailFragment.EPCODING;

    public static Map<String, CodeCheckBox> createCheckBoxLayoutAndCodeMap(
            Code[] codes, LinearLayout layout, Context context,
            boolean showCodeFirst, final Fragment fragment) {
        Map<String, CodeCheckBox> map = new LinkedHashMap<>();
        for (int i = 0; i < codes.length; ++i) {
            final CodeCheckBox codeCheckBox = new CodeCheckBox(context);
            final Context theContext = context;
            codeCheckBox.setCodeFirst(showCodeFirst);
            codeCheckBox.setCode(codes[i]);
            map.put(codes[i].getCodeNumber(), codeCheckBox);
            codeCheckBox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.v(EPCODING, "on long click");
                    Log.v(EPCODING, "Active code number = " + codeCheckBox.getCodeNumber());
                    Intent intent = new Intent(theContext, ModifierActivity.class);
                    intent.putExtra("ACTIVE_CODE_NUMBER", codeCheckBox.getCodeNumber());
                    fragment.startActivityForResult(intent, 1);
                    return true;
                }
            });
            layout.addView(codeCheckBox);
        }
        return map;
    }

    public static String codeAnalysis(final Code[] codes,
                                      final boolean noPrimaryCodes, final boolean noSecondaryCodes,
                                      final boolean moduleHasNoSecondaryCodes, boolean noAnalysis,
                                      boolean verbose, SedationStatus sedationStatus,
                                      Context context) {
        CodeAnalyzer analyzer = new CodeAnalyzer(codes, noPrimaryCodes,
                noSecondaryCodes, moduleHasNoSecondaryCodes, sedationStatus, context);
        // no analysis for all procedures module
        analyzer.setNoAnalysis(noAnalysis);
        analyzer.setVerbose(verbose);
        return analyzer.analysis();
    }

    public static String simpleCodeAnalysis(final Code[] codes, SedationStatus sedationStatus,
                                            Context context) {
        CodeAnalyzer analyzer = new CodeAnalyzer(codes, sedationStatus, context);
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
