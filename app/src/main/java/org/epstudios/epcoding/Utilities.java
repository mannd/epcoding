package org.epstudios.epcoding;

import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.epstudios.epcoding.Constants.AGE;
import static org.epstudios.epcoding.Constants.EPCODING;
import static org.epstudios.epcoding.Constants.SAME_MD;
import static org.epstudios.epcoding.Constants.SEDATION_REQUEST_CODE;
import static org.epstudios.epcoding.Constants.SEDATION_STATUS;
import static org.epstudios.epcoding.Constants.TIME;

class Utilities {

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

    public static void showSedationCodeSummary(final int sedationTime,
                                               final boolean patientOver5YrsOld,
                                               final boolean sameMDPerformsSedation,
                                               final SedationStatus sedationStatus,
                                               final List<Code> sedationCodes,
                                               final Fragment fragment) {
        final Context context = fragment.getActivity();
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        String message;
        String title = "Edit Sedation Codes";
        String buttonLabel = "Edit";
        switch (sedationStatus) {
            case Unassigned:
                title = "Add Sedation Codes";
                buttonLabel = "Add";
                message = "Sedation coding not yet assigned for this procedure";
                break;
            case None:
                message = "No sedation was used in this procedure.";
                break;
            case LessThan10Mins:
                message = "Sedation time < 10 mins\nNo sedation codes can be assigned.";
                break;
            case OtherMDCalculated:
                message = String.format("Sedation by other MD, using:\n%s",
                        SedationCode.printSedationCodes(sedationCodes, "\n"));
                break;
            case AssignedSameMD:
                message = String.format("Sedation by same MD, using:\n%s",
                        SedationCode.printSedationCodes(sedationCodes, "\n"));
                break;
            default:
                message = "Error in sedation coding.";
                break;
        }
        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonLabel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(EPCODING, "click Add");
                Intent intent = new Intent(context, SedationActivity.class);
                intent.putExtra("TIME", (int) sedationTime);
                intent.putExtra("AGE", patientOver5YrsOld);
                intent.putExtra("SAME_MD", sameMDPerformsSedation);
                intent.putExtra("SEDATION_STATUS", sedationStatus);
                fragment.startActivityForResult(intent, SEDATION_REQUEST_CODE);

            }
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Log.d(EPCODING, "click cancel");
            }
        });

        dialog.show();
    }

    private static void determineSedationCoding(List<Code> sedationCodes, final int sedationTime,
                                                final boolean sameMDPerformsSedation,
                                                final boolean patientOver5YrsOld) {
        sedationCodes.clear();
        sedationCodes.addAll(SedationCode.sedationCoding(sedationTime, sameMDPerformsSedation,
                patientOver5YrsOld));
    }

    public static void setSedationData(boolean sameMDPerformsSedation,
                                       boolean patientOver5YrsOld,
                                       int sedationTime,
                                       SedationStatus sedationStatus,
                                       List<Code> sedationCodes,
                                       Intent data) {
        sameMDPerformsSedation = data.getBooleanExtra(SAME_MD, sameMDPerformsSedation);
        patientOver5YrsOld = data.getBooleanExtra(AGE, patientOver5YrsOld);
        sedationTime = data.getIntExtra(TIME, sedationTime);
        sedationStatus = (SedationStatus) data.getSerializableExtra(SEDATION_STATUS);
        determineSedationCoding(sedationCodes, sedationTime, sameMDPerformsSedation, patientOver5YrsOld);
    }

    public static void setModifierData(List<Code> codes,
                                       List<Map<String, CodeCheckBox>> checkBoxMaps,
                                       Intent data,
                                       Context context) {
        String[] result = data.getStringArrayExtra(ModifierActivity.MODIFIER_RESULT);
        Log.d(EPCODING, "Result = " + result.length);
        if (result.length == 1 && result[0].equals(ModifierActivity.RESET_MODIFIERS)) {
            resetModifiers(codes, context);
            resetCodes(codes, checkBoxMaps);
            return;
        }
        Code code = Codes.setModifiersForCode(result);
        if (code != null) {
            CodeCheckBox checkBox = getCheckBoxWithCode(code.getCodeNumber(), checkBoxMaps);
            if (checkBox != null) {
                // forces checkbox to redraw itself
                // checkBox.invalidate() doesn't work for some reason
                checkBox.setCode(code);
            }
        }
    }

    public static void resetModifiers(List<Code> codes, Context context) {
        Codes.resetSavedModifiers(codes, context);
    }

    public static void resetCodes(List<Code> codes,
                                  List<Map<String, CodeCheckBox>> checkBoxMaps) {
        Codes.loadDefaultModifiers(codes);
        for (Code code : codes) {
            CodeCheckBox checkBox = getCheckBoxWithCode(code.getCodeNumber(), checkBoxMaps);
            if (checkBox != null) {
                checkBox.setCode(code);
            }
        }
    }


    public static CodeCheckBox getCheckBoxWithCode(String codeNumber, List<Map<String, CodeCheckBox>> checkBoxMaps) {
        String result;
        for (Map<String, CodeCheckBox> map : checkBoxMaps) {
            for (Map.Entry<String, CodeCheckBox> entry : map.entrySet()) {
                result = entry.getValue().getCodeNumber();
                if (result.equals(codeNumber)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }


}
