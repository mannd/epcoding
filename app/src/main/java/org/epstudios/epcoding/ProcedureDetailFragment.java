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

import androidx.annotation.NonNull;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.preference.PreferenceManager;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.TreeSet;

import static org.epstudios.epcoding.Constants.ACTIVE_CODE_NUMBER;
import static org.epstudios.epcoding.Constants.AGE;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_AGE;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_SAME_MD;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_STATUS;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_TIME;
import static org.epstudios.epcoding.Constants.EPCODING;
import static org.epstudios.epcoding.Constants.MODIFIER_REQUEST_CODE;
import static org.epstudios.epcoding.Constants.SAME_MD;
import static org.epstudios.epcoding.Constants.SAVE_TEMP_ADDED_MODIFIERS;
import static org.epstudios.epcoding.Constants.SEDATION_REQUEST_CODE;
import static org.epstudios.epcoding.Constants.SEDATION_STATUS;
import static org.epstudios.epcoding.Constants.TIME;

/**
 * A fragment representing a single Procedure detail screen. This fragment is
 * either contained in a {@link ProcedureListActivity} in two-pane mode (on
 * tablets) or a {@link ProcedureDetailActivity} on handsets.
 */
@SuppressWarnings("FieldCanBeLocal")
public class ProcedureDetailFragment extends Fragment implements
        OnClickListener {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

//    private final int MODIFIER_REQUEST_CODE = 1;
//    private final int SEDATION_REQUEST_CODE = 2;

    /**
     * The content this fragment is presenting.
     */
    private int mItem = 0;

    // get context from owning Activity
    private Context context;

    // Settings
    // boolean plusShownInDisplay; // show plus in main display
    // boolean allowChangingPrimaryCodes;
    private boolean plusShownInSummary;
    private boolean codeDescriptionInSummary;
    private boolean descriptionTruncatedInSummary;
    private boolean codeCheckAllCodes;
    private String codeVerbosity;
    private boolean useUnicodeSymbols;
    // shorten description based on screen width?

    // don't try to refactor these using Utilities.createCheckBoxLayoutAndCodeMap,
    // as then will have to put in many check for null map.
    private final Map<String, CodeCheckBox> primaryCheckBoxMap = new LinkedHashMap<>();
    private final Map<String, CodeCheckBox> secondaryCheckBoxMap = new LinkedHashMap<>();
    private final List<Map<String, CodeCheckBox>> allCheckBoxMapList = new ArrayList<>();

    // these correspond with the procedure list order
    final private int afbAblation = 0;
    final private int svtAblation = 1;
    final private int vtAblation = 2;
    final private int avnAblation = 3;
    final private int epTesting = 4;
    final private int newPpm = 5;
    final private int newIcd = 6;
    final private int ppmReplacement = 7;
    final private int icdReplacement = 8;
    final private int deviceUpgrade = 9;
    final private int subQIcd = 10;
    final private int otherProcedure = 11;
    final private int allProcedures = 12;

    private Procedure procedure;

    private List<Code> allPrimaryAndSecondaryCodes;

    // Sedation stuff
    private SedationStatus sedationStatus = SedationStatus.Unassigned;
    private Integer sedationTime = 0;
    private boolean sameMDPerformsSedation = true;
    private boolean patientOver5YrsOld = true;
    private final List<Code> sedationCodes = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (requireArguments().containsKey(ARG_ITEM_ID)) {
            String itemID = getArguments().getString(ARG_ITEM_ID);
            try {
                mItem = Integer.parseInt(Objects.requireNonNull(itemID));
            } catch (NumberFormatException e) {
                mItem = 0; // AFB ablation will be shown
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onResume() {
        super.onResume();
        loadSettings();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        // store check marks here
        boolean[] primaryCodeState = new boolean[primaryCheckBoxMap.size()];
        int i = 0;
        for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
                .entrySet())
            primaryCodeState[i++] = entry.getValue().isChecked();
        i = 0;
        boolean[] secondaryCodeState = new boolean[secondaryCheckBoxMap.size()];
        for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
                .entrySet())
            secondaryCodeState[i++] = entry.getValue().isChecked();
        savedInstanceState.putBooleanArray("primary_codes", primaryCodeState);
        savedInstanceState.putBooleanArray("secondary_codes",
                secondaryCodeState);

        savedInstanceState.putBoolean(BUNDLE_SEDATION_SAME_MD, sameMDPerformsSedation);
        savedInstanceState.putBoolean(BUNDLE_SEDATION_AGE, patientOver5YrsOld);
        savedInstanceState.putInt(BUNDLE_SEDATION_TIME, sedationTime);
        savedInstanceState.putString(BUNDLE_SEDATION_STATUS, sedationStatus.toString());

        // save modifiers for each code
        for (Code code : allPrimaryAndSecondaryCodes()) {
            String[] modifierNumbers = code.getModifierNumberArray();
            savedInstanceState.putStringArray(code.getCodeNumber(), modifierNumbers);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sedation_button:
                addSedation();
                break;
            case R.id.summary_button:
                summarizeCoding();
                break;
            case R.id.clear_button:
                clearEntries();
                break;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.procedure_codes, container,
                false);
        context = getActivity();

        LinearLayout primaryCheckBoxLayout = rootView
                .findViewById(R.id.primary_checkbox_layout);
        LinearLayout secondaryCheckBoxLayout = rootView
                .findViewById(R.id.secondary_checkbox_layout);
        TextView primaryCodeTextView = rootView
                .findViewById(R.id.primary_code_textView);
        TextView secondaryCodeTextView = rootView
                .findViewById(R.id.secondary_code_textView);

        loadSettings();

        switch (mItem) {
            case afbAblation:
                procedure = new AfbAblation();
                break;
            case svtAblation:
                procedure = new SvtAblation();
                break;
            case vtAblation:
                procedure = new VtAblation();
                break;
            case avnAblation:
                procedure = new AvnAblation();
                break;
            case epTesting:
                procedure = new EpTesting();
                break;
            case newPpm:
                procedure = new NewPpm();
                break;
            case newIcd:
                procedure = new NewIcd();
                break;
            case ppmReplacement:
                procedure = new PpmReplacement();
                break;
            case icdReplacement:
                procedure = new IcdReplacement();
                break;
            case deviceUpgrade:
                procedure = new DeviceUpgrade();
                break;
            case subQIcd:
                procedure = new SubQIcd();
                break;
            case otherProcedure:
                procedure = new OtherProcedure();
                break;
            case allProcedures:
            default:
                procedure = new AllCodes();
                break;
        }

        Codes.clearMultipliersAndModifiers(allPrimaryAndSecondaryCodes());
        Codes.clearMultipliersAndModifiers(sedationCodes);
        if (mItem != allProcedures) {
            Codes.loadDefaultModifiers(allPrimaryAndSecondaryCodes());
        }
        Codes.loadSavedModifiers(allPrimaryAndSecondaryCodes(), context);


        requireActivity().setTitle(procedure.title(context));

        Code[] secondaryCodes = procedure.secondaryCodes();

        if (secondaryCodes.length > 0) {
            createCheckBoxLayoutAndCodeMap(secondaryCodes,
                    secondaryCheckBoxMap, secondaryCheckBoxLayout);
        } else {
            secondaryCheckBoxLayout.setVisibility(View.GONE);
            secondaryCodeTextView.setVisibility(View.GONE);
        }

        if (mItem == allProcedures) {
            primaryCodeTextView
                    .setText(getString(R.string.all_codes_primary_title));
        }

        Code[] primaryCodes = procedure.primaryCodes();
        String[] disabledCodeNumbers = procedure.disabledCodeNumbers();

        for (String disabledCodeNumber : disabledCodeNumbers)
            Objects.requireNonNull(secondaryCheckBoxMap.get(disabledCodeNumber)).disable();


        createCheckBoxLayoutAndCodeMap(primaryCodes, primaryCheckBoxMap,
                primaryCheckBoxLayout);

        if (procedure.disablePrimaryCodes()) {
            // check and disable primary checkboxes for ablation type items
            for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
                    .entrySet()) {
                entry.getValue().setEnabled(false);
                entry.getValue().setChecked(true);
            }
        }

        allCheckBoxMapList.add(primaryCheckBoxMap);
        allCheckBoxMapList.add(secondaryCheckBoxMap);


        // apply saved configurations here
        if (null != savedInstanceState) {
            // restore state
            boolean[] primaryCodesState = savedInstanceState
                    .getBooleanArray("primary_codes");
            boolean[] secondaryCodesState = savedInstanceState
                    .getBooleanArray("secondary_codes");
            int i = 0;
            for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
                    .entrySet())
                entry.getValue().setChecked(primaryCodesState != null && primaryCodesState[i++]);
            i = 0;
            for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
                    .entrySet())
                entry.getValue().setChecked(secondaryCodesState != null && secondaryCodesState[i++]);
            patientOver5YrsOld = savedInstanceState.getBoolean(BUNDLE_SEDATION_AGE);
            sameMDPerformsSedation = savedInstanceState.getBoolean(BUNDLE_SEDATION_SAME_MD);
            sedationTime = savedInstanceState.getInt(BUNDLE_SEDATION_TIME);
            sedationStatus = SedationStatus.stringToSedationStatus(savedInstanceState.getString(BUNDLE_SEDATION_STATUS));
            sedationCodes.clear();
            sedationCodes.addAll(SedationCode.sedationCoding(sedationTime, sameMDPerformsSedation, patientOver5YrsOld,
                    sedationStatus));

            // load modifiers
            for (Code code : allPrimaryAndSecondaryCodes()) {
                String[] modifierNumbers = savedInstanceState.getStringArray(code.getCodeNumber());
                code.clearModifiers();
                for (i = 0; i < (modifierNumbers != null ? modifierNumbers.length : 0); i++) {
                    code.addModifier(Modifiers.getModifierForNumber(modifierNumbers[i]));
                }
                // redraw code checkboxes
                redrawCheckBox(code);
            }
        } else {
            // This loads saved coding patterns
            loadCoding();
        }
        // set up buttons
        Button sedationButton = rootView.findViewById(R.id.sedation_button);
        sedationButton.setOnClickListener(this);
        Button summarizeButton = rootView.findViewById(R.id.summary_button);
        summarizeButton.setOnClickListener(this);
        Button clearButton = rootView.findViewById(R.id.clear_button);
        clearButton.setOnClickListener(this);

        return rootView;
    }

    private void redrawCheckBox(Code code) {
        CodeCheckBox checkBox = getCheckBoxWithCode(code.getCodeNumber());
        if (checkBox != null) {
            // forces checkbox to redraw itself
            // checkBox.invalidate() doesn't work for some reason
            checkBox.setCode(code);
        }
    }

    private void createCheckBoxLayoutAndCodeMap(Code[] codes,
                                                Map<String, CodeCheckBox> codeCheckBoxMap, LinearLayout layout) {
        for (Code code : codes) {
            final CodeCheckBox codeCheckBox = new CodeCheckBox(context);
            // codes[i].setPlusShown(plusShownInDisplay);
            codeCheckBox.setCodeFirst(mItem == allProcedures);
            codeCheckBox.setCode(code);
            codeCheckBoxMap.put(code.getCodeNumber(), codeCheckBox);
            // add long click listener to open modifier dialog
            // because they are disabled.  This is an Android limitation, though
            // any code can be long clicked and modified in the all codes module.
            codeCheckBox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Intent intent = new Intent(getActivity(), ModifierActivity.class);
                    intent.putExtra(ACTIVE_CODE_NUMBER, codeCheckBox.getCodeNumber());
                    intent.putExtra(SAVE_TEMP_ADDED_MODIFIERS, false);
                    startActivityForResult(intent, MODIFIER_REQUEST_CODE);
                    return true;
                }
            });
            layout.addView(codeCheckBox);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == MODIFIER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String[] result = data.getStringArrayExtra(Constants.MODIFIER_RESULT);
                if (Objects.requireNonNull(result).length == 1 && result[0].equals(Constants.RESET_MODIFIERS)) {
                    resetModifiers();
                    resetCodes();
                    return;
                }
                Code code = Codes.setModifiersForCode(result);
                if (code != null) {
                    redrawCheckBox(code);
                }
            }
        }
        if (requestCode == SEDATION_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                sameMDPerformsSedation = data.getBooleanExtra(SAME_MD, sameMDPerformsSedation);
                patientOver5YrsOld = data.getBooleanExtra(AGE, patientOver5YrsOld);
                sedationTime = data.getIntExtra(TIME, sedationTime);
                sedationStatus = (SedationStatus) data.getSerializableExtra(SEDATION_STATUS);
                sedationCodes.clear();
                sedationCodes.addAll(SedationCode.sedationCoding(sedationTime, sameMDPerformsSedation,
                        patientOver5YrsOld, sedationStatus));
            }
        }
    }

    private void resetModifiers() {
        Utilities.resetModifiers(allPrimaryAndSecondaryCodes(), context);
    }

    private void resetCodes() {
        Utilities.resetCodes(allPrimaryAndSecondaryCodes(), allCheckBoxMapList);
    }

    private CodeCheckBox getCheckBoxWithCode(String codeNumber) {
        return Utilities.getCheckBoxWithCode(codeNumber, allCheckBoxMapList);
    }

    private List<Code> allPrimaryAndSecondaryCodes() {
        if (allPrimaryAndSecondaryCodes == null) {
            allPrimaryAndSecondaryCodes = new ArrayList<>();
            Code[] primaryCodes = procedure.primaryCodes();
            Code[] secondaryCodes = procedure.secondaryCodes();
            Collections.addAll(allPrimaryAndSecondaryCodes, primaryCodes);
            Collections.addAll(allPrimaryAndSecondaryCodes, secondaryCodes);
        }
        return allPrimaryAndSecondaryCodes;
    }

    private void addSedation() {
        Log.d(EPCODING, "addSedation");
        Utilities.showSedationCodeSummary(sedationTime, patientOver5YrsOld,
                sameMDPerformsSedation,sedationStatus, sedationCodes, this);

    }

    private void clearEntries() {
        for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
                .entrySet())
            entry.getValue().clearIfEnabled();
        for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
                .entrySet())
            entry.getValue().setChecked(false);

    }

    private void summarizeCoding() {
        StringBuilder message = new StringBuilder();
        // we will extract the raw selected codes and shoot them to the code
        // analyzer, as well as check for no primary or secondary codes
        int size = primaryCheckBoxMap.size() + secondaryCheckBoxMap.size() + sedationCodes.size();
        Log.d(EPCODING, "number of codes = " + size);
        //Code[] codes = new Code[Codes.allCodesSize()];
        Code[] codes = new Code[size];
        int i = 0;
        for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
                .entrySet()) {
            if (entry.getValue().isChecked()) {
                codes[i] = entry.getValue().getCode();
                message.append(getSummaryFromCode(codes[i++]));
            }
        }
        int primaryCodeCounter = i;
        boolean noPrimaryCodes = (primaryCodeCounter == 0);
        // see if there are any secondary codes to begin with,
        // or if usually no secondary codes selected, e.g. PPM modules
        for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
                .entrySet()) {
            if (entry.getValue().isChecked()) {
                codes[i] = entry.getValue().getCode();
                message.append(getSummaryFromCode(codes[i++]));
            }
        }
        for (Code code: sedationCodes) {
            message.append(getSummaryFromCode(code));

        }
        List<Code> allCodes = new ArrayList<>();
        allCodes.addAll(Arrays.asList(codes));
        allCodes.addAll(sedationCodes);
        boolean noSecondaryCodes = primaryCodeCounter == i;
        boolean moduleHasNoSecondaryCodesNeedingChecking = procedure
                .doNotWarnForNoSecondaryCodesSelected();
        boolean noAnalysis = codeVerbosity.equals("None")
                || (mItem == allProcedures && !codeCheckAllCodes);
        message.append(Utilities.codeAnalysis(allCodes, noPrimaryCodes,
                noSecondaryCodes, moduleHasNoSecondaryCodesNeedingChecking,
                noAnalysis, codeVerbosity.equals("Verbose"),
                sedationStatus, useUnicodeSymbols, context));
        Utilities.displayMessage(
                getString(R.string.coding_summary_dialog_label), message.toString(),
                context);
    }

    private String getSummaryFromCode(Code code) {
        code.setPlusShown(plusShownInSummary);
        code.setDescriptionShown(codeDescriptionInSummary);
        code.setDescriptionShortened(descriptionTruncatedInSummary);
        return code.getCodeFirstFormatted() + "\n";
    }

    public void saveCoding() {
        // don't bother if no secondary codes to save
        if (secondaryCheckBoxMap.isEmpty()) {
            Toast toast = Toast.makeText(context,
                    getString(R.string.no_secondary_codes_error_message),
                    Toast.LENGTH_SHORT);
            toast.show();
            return;
        }
        AlertDialog dialog = new AlertDialog.Builder(context).create();
        String message = "Save these selections as a default?";
        dialog.setMessage(message);
        dialog.setTitle("Save Defaults");
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        save();
                    }
                });
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
        dialog.show();
    }

    private void save() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mItemString = String.valueOf(mItem);
        Set<String> checkedCodeNumbers = new TreeSet<>();
        for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
                .entrySet()) {
            if (entry.getValue().isChecked())
                checkedCodeNumbers.add(entry.getValue().getCodeNumber());
        }
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putStringSet(mItemString, checkedCodeNumbers);
        prefsEditor.apply();
    }

    private void loadCoding() {
        load(context);
    }

    private void load(Context context) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(context);
        String mItemString = String.valueOf(mItem);
        Set<String> defaultStringSet = new TreeSet<>();
        Set<String> codeNumbersChecked = prefs.getStringSet(mItemString,
                defaultStringSet);
        // String numbers = codeNumbersChecked.toString();
        for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
                .entrySet()) {
            if (Objects.requireNonNull(codeNumbersChecked).contains(entry.getValue().getCodeNumber()))
                entry.getValue().setChecked(true);
        }

    }

    private void loadSettings() {
        SharedPreferences sharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        // plusShownInDisplay = sharedPreferences.getBoolean(
        // "show_plus_code_display", true);
        // allowChangingPrimaryCodes = sharedPreferences.getBoolean(
        // "allow_changing_primary_codes", false);
        plusShownInSummary = sharedPreferences.getBoolean(
                getString(R.string.show_plus_code_summary_key), true);
        codeDescriptionInSummary = sharedPreferences.getBoolean(
                getString(R.string.show_details_code_summary_key), false);
        descriptionTruncatedInSummary = sharedPreferences
                .getBoolean(
                        getString(R.string.truncate_long_descriptions_code_summary_key),
                        false);
        codeCheckAllCodes = sharedPreferences.getBoolean(
                getString(R.string.code_check_all_codes_key), false);
        codeVerbosity = sharedPreferences
                .getString("code_verbosity", "Verbose");
        useUnicodeSymbols = sharedPreferences.getBoolean(
                getString(R.string.use_unicode_symbols_key), false);
    }
}
