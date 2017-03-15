/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.epstudios.epcoding;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.epstudios.epcoding.Constants.AGE;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_AGE;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_SAME_MD;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_STATUS;
import static org.epstudios.epcoding.Constants.BUNDLE_SEDATION_TIME;
import static org.epstudios.epcoding.Constants.EPCODING;
import static org.epstudios.epcoding.Constants.MODIFIER_REQUEST_CODE;
import static org.epstudios.epcoding.Constants.SAME_MD;
import static org.epstudios.epcoding.Constants.SEDATION_REQUEST_CODE;
import static org.epstudios.epcoding.Constants.SEDATION_STATUS;
import static org.epstudios.epcoding.Constants.TIME;

public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener {
    /**
     * The argument key for the page number this fragment represents.
     */
    private static final String ARG_PAGE = "page";

    private static final String[] revisionCodeNumbers = {"33215", "33226", "33218",
            "33220", "33222", "33223"};

    private static final String[] removalCodeNumbers = {"33233", "33241", "33234",
            "33235", "33244"};

    private static final String[] addingCodeNumbers = {"33206", "33207", "33208",
            "33249", "33216", "33217", "33225", "33212", "33213", "33240",
            "33230", "33231"};

    private Map<String, CodeCheckBox> revisionCheckBoxMap;
    private Map<String, CodeCheckBox> removalCheckBoxMap;
    private Map<String, CodeCheckBox> addingCheckBoxMap;
    private Map<String, CodeCheckBox> finalCheckBoxMap;
    private List<Map<String, CodeCheckBox>> allCheckBoxMapList;

    /**
     * The fragment's page number, which is set to the argument value for
     * {@link #ARG_PAGE}.
     */
    private int mPageNumber;
    private Context context;
    private ScreenSlideActivity parent;

    private List<Code> allCodes;

    private SedationStatus sedationStatus = SedationStatus.Unassigned;
    private List<Code> sedationCodes = new ArrayList<>();
    private int sedationTime = 0;
    private boolean sameMDPerformsSedation = true;
    private boolean patientOver5YrsOld = true;

    /**
     * Factory method for this fragment class. Constructs a new fragment for the
     * given page number.
     */
    public static ScreenSlidePageFragment create(int pageNumber) {
        ScreenSlidePageFragment fragment = new ScreenSlidePageFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, pageNumber);
        fragment.setArguments(args);
        return fragment;
    }

    public ScreenSlidePageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPageNumber = getArguments().getInt(ARG_PAGE);
        context = getActivity();
        parent = (ScreenSlideActivity) context;
        Log.d(EPCODING, "ScreenSlidePageFragment onCreate page " + mPageNumber);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(EPCODING, "ScreenSlidePageFragment onCreateView page " + mPageNumber);
        // select layout based on page number
        int layout = R.layout.fragment_screen_slide_page;
        ViewGroup rootView = (ViewGroup) inflater.inflate(layout, container,
                false);
        LinearLayout revisionCheckBoxLayout = (LinearLayout) rootView
                .findViewById(R.id.revision_hardware);
        revisionCheckBoxLayout.setVisibility(View.GONE);
        LinearLayout removedCheckBoxLayout = (LinearLayout) rootView
                .findViewById(R.id.removed_hardware);
        removedCheckBoxLayout.setVisibility(View.GONE);
        LinearLayout addingCheckBoxLayout = (LinearLayout) rootView
                .findViewById(R.id.adding_hardware);
        addingCheckBoxLayout.setVisibility(View.GONE);
        LinearLayout finalCheckBoxLayout = (LinearLayout) rootView
                .findViewById(R.id.final_codes);
        finalCheckBoxLayout.setVisibility(View.GONE);

        Button sedationButton = (Button) rootView.findViewById(R.id.sedation_button);
        sedationButton.setOnClickListener(this);
        sedationButton.setVisibility(View.GONE);

        Code[] revisionCodes = Codes.getCodes(revisionCodeNumbers);
        Code[] removalCodes = Codes.getCodes(removalCodeNumbers);
        Code [] addingCodes = Codes.getCodes(addingCodeNumbers);
        Code [] finalCodes = Codes.getCodes(Codes.icdReplacementSecondaryCodeNumbers);

        allCodes = new ArrayList<>();
        allCodes.addAll(Arrays.asList(revisionCodes));
        allCodes.addAll(Arrays.asList(removalCodes));
        allCodes.addAll(Arrays.asList(addingCodes));
        allCodes.addAll(Arrays.asList(finalCodes));

        Codes.clearMultipliersAndModifiers(allCodes);
        Codes.loadDefaultModifiers(allCodes);
        Codes.loadSavedModifiers(allCodes, context);
        Codes.loadTempAddedModifiers(allCodes, context);


        // Use default sedation, unless sedation already set
        sedationCodes.clear();
        sedationStatus = parent.getSedationStatus();
        patientOver5YrsOld = parent.isAgeOver5();
        sameMDPerformsSedation = parent.isSameMD();
        sedationTime = parent.getSedationTime();

        revisionCheckBoxMap = Utilities.createCheckBoxLayoutAndCodeMap(
                revisionCodes, revisionCheckBoxLayout, context, this);
        addCheckMarkListener(revisionCheckBoxMap);
        removalCheckBoxMap = Utilities.createCheckBoxLayoutAndCodeMap(
                removalCodes, removedCheckBoxLayout, context, this);
        addCheckMarkListener(removalCheckBoxMap);
        addingCheckBoxMap = Utilities.createCheckBoxLayoutAndCodeMap(
                addingCodes, addingCheckBoxLayout, context, this);
        addCheckMarkListener(addingCheckBoxMap);
        finalCheckBoxMap = Utilities.createCheckBoxLayoutAndCodeMap(finalCodes,
                finalCheckBoxLayout, context, this);
        addCheckMarkListener(finalCheckBoxMap);

        allCheckBoxMapList = new ArrayList<>();
        allCheckBoxMapList.add(revisionCheckBoxMap);
        allCheckBoxMapList.add(removalCheckBoxMap);
        allCheckBoxMapList.add(addingCheckBoxMap);
        allCheckBoxMapList.add(finalCheckBoxMap);

        // Set the title view to show the page number.
        ((TextView) rootView.findViewById(android.R.id.text1))
                .setText(getString(R.string.title_template_step,
                        mPageNumber + 1));
        TextView headingText = (TextView) rootView
                .findViewById(R.id.slide_help_text);
        switch (mPageNumber) {
            case 0:
                headingText.setText(getString(R.string.slide_step1_heading_text));
                break;
            case 1:
                headingText.setText(getString(R.string.slide_step2_heading_text));
                break;
            case 2:
                headingText.setText(getString(R.string.slide_step2a_heading_text));
                sedationButton.setVisibility(View.VISIBLE);
                break;
            case 3:
                headingText.setText(getString(R.string.slide_step3_heading_text));
                revisionCheckBoxLayout.setVisibility(View.VISIBLE);
                break;
            case 4:
                headingText.setText(getString(R.string.slide_step4_heading_text));
                removedCheckBoxLayout.setVisibility(View.VISIBLE);
                break;
            case 5:
                headingText.setText(getString(R.string.slide_step5_heading_text));
                addingCheckBoxLayout.setVisibility(View.VISIBLE);
                break;
            case 6:
                headingText.setText(getString(R.string.slide_step6_heading_text));
                finalCheckBoxLayout.setVisibility(View.VISIBLE);
                break;
        }

        if (null != savedInstanceState) {
            // restore state
            Log.d(EPCODING, "ScreenSlidePageFragment restore state from bundle Page "
                    + getPageNumber());
            boolean[] revisionCodesState = savedInstanceState
                    .getBooleanArray("revision_codes");
            boolean[] removalCodesState = savedInstanceState
                    .getBooleanArray("removal_codes");
            boolean[] addingCodesState = savedInstanceState
                    .getBooleanArray("adding_codes");
            boolean[] finalCodesState = savedInstanceState
                    .getBooleanArray("final_codes");
            int i = 0;
            for (Map.Entry<String, CodeCheckBox> entry : revisionCheckBoxMap.entrySet())
                entry.getValue().setChecked(revisionCodesState != null && revisionCodesState[i++]);
            i = 0;
            for (Map.Entry<String, CodeCheckBox> entry : removalCheckBoxMap
                    .entrySet())
                entry.getValue().setChecked(removalCodesState != null && removalCodesState[i++]);
            i = 0;
            for (Map.Entry<String, CodeCheckBox> entry : addingCheckBoxMap
                    .entrySet())
                entry.getValue().setChecked(addingCodesState != null && addingCodesState[i++]);
            i = 0;
            for (Map.Entry<String, CodeCheckBox> entry : finalCheckBoxMap
                    .entrySet())
                entry.getValue().setChecked(finalCodesState != null && finalCodesState[i++]);

            patientOver5YrsOld = savedInstanceState.getBoolean(BUNDLE_SEDATION_AGE);
            sameMDPerformsSedation = savedInstanceState.getBoolean(BUNDLE_SEDATION_SAME_MD);
            sedationTime = savedInstanceState.getInt(BUNDLE_SEDATION_TIME);
            sedationStatus = SedationStatus.stringToSedationStatus(savedInstanceState.getString(BUNDLE_SEDATION_STATUS));
            sedationCodes.clear();
            sedationCodes.addAll(SedationCode.sedationCoding(sedationTime, sameMDPerformsSedation, patientOver5YrsOld,
                    sedationStatus));
        }

        return rootView;
    }

    /**
     * Returns the page number represented by this fragment object.
     */
    private int getPageNumber() {
        return mPageNumber;
    }

    private void addCheckMarkListener(Map<String, CodeCheckBox> codeMap) {
        for (Map.Entry<String, CodeCheckBox> entry : codeMap.entrySet()) {
            entry.getValue().setOnCheckedChangeListener(
                    new OnCheckedChangeListener() {

                        @Override
                        public void onCheckedChanged(CompoundButton buttonView,
                                                     boolean isChecked) {
                            checkChanged();
                        }
                    });
        }
    }

    private void checkChanged() {
        save();
    }

    private void save() {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(getActivity());
        SharedPreferences.Editor prefsEditor = prefs.edit();
        switch (mPageNumber) {
            case 3:
                prefsEditor.putStringSet("wizardrevisioncodes",
                        getCheckBoxSet(revisionCheckBoxMap));
            case 4:
                prefsEditor.putStringSet("wizardremovalcodes",
                        getCheckBoxSet(removalCheckBoxMap));
                break;
            case 5:
                prefsEditor.putStringSet("wizardaddingcodes",
                        getCheckBoxSet(addingCheckBoxMap));
                break;
            case 6:
                prefsEditor.putStringSet("wizardfinalcodes",
                        getCheckBoxSet(finalCheckBoxMap));
                break;
        }
        prefsEditor.apply();
    }

    private Set<String> getCheckBoxSet(Map<String, CodeCheckBox> map) {
        Set<String> checkedCodeNumbers = new TreeSet<>();
        for (Map.Entry<String, CodeCheckBox> entry : map.entrySet()) {
            if (entry.getValue().isChecked())
                checkedCodeNumbers.add(entry.getValue().getCodeNumber());
        }
        return checkedCodeNumbers;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Log.d(EPCODING, "ScreenSlidePageFragment onSaveInstanceState page " + getPageNumber());
        // store check marks here
        boolean[] revisionCodeState = new boolean[revisionCheckBoxMap.size()];
        int i = 0;
        for (Map.Entry<String, CodeCheckBox> entry : revisionCheckBoxMap
                .entrySet())
            revisionCodeState[i++] = entry.getValue().isChecked();
        boolean[] removalCodeState = new boolean[removalCheckBoxMap.size()];
        i = 0;
        for (Map.Entry<String, CodeCheckBox> entry : removalCheckBoxMap
                .entrySet())
            removalCodeState[i++] = entry.getValue().isChecked();
        i = 0;
        boolean[] addingCodeState = new boolean[addingCheckBoxMap.size()];
        for (Map.Entry<String, CodeCheckBox> entry : addingCheckBoxMap
                .entrySet())
            addingCodeState[i++] = entry.getValue().isChecked();
        i = 0;
        boolean[] finalCodeState = new boolean[finalCheckBoxMap.size()];
        for (Map.Entry<String, CodeCheckBox> entry : finalCheckBoxMap
                .entrySet())
            finalCodeState[i++] = entry.getValue().isChecked();
        switch (mPageNumber) {
            case 3:
                savedInstanceState.putBooleanArray("revision_codes", revisionCodeState);
            case 4:
                savedInstanceState.putBooleanArray("removal_codes", removalCodeState);
                break;
            case 5:
                savedInstanceState.putBooleanArray("adding_codes", addingCodeState);
                break;
            case 6:
                savedInstanceState.putBooleanArray("final_codes", finalCodeState);
                break;
            default:
                break;
        }
        savedInstanceState.putBoolean(BUNDLE_SEDATION_SAME_MD, sameMDPerformsSedation);
        savedInstanceState.putBoolean(BUNDLE_SEDATION_AGE, patientOver5YrsOld);
        savedInstanceState.putInt(BUNDLE_SEDATION_TIME, sedationTime);
        savedInstanceState.putString(BUNDLE_SEDATION_STATUS, sedationStatus.toString());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(EPCODING, "onActivityResult called");
        if (requestCode == MODIFIER_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String[] result = data.getStringArrayExtra(Constants.MODIFIER_RESULT);
                if (result.length == 1 && result[0].equals(Constants.RESET_MODIFIERS)) {
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
                // send results to parent activity
                parent.setSedationStatus(sedationStatus);
                parent.setSedationTime(sedationTime);
                parent.setAgeOver5(patientOver5YrsOld);
                parent.setSameMD(sameMDPerformsSedation);
            }
        }
    }

    private void redrawCheckBox(Code code) {
        CodeCheckBox checkBox = Utilities.getCheckBoxWithCode(code.getCodeNumber(),
                allCheckBoxMapList);
        if (checkBox != null) {
            // forces checkbox to redraw itself
            // checkBox.invalidate() doesn't work for some reason
            checkBox.setCode(code);
        }
    }

    private void resetModifiers() {
        Utilities.resetModifiers(allCodes, context);
    }


    private void resetCodes() {
        Utilities.resetCodes(allCodes, allCheckBoxMapList);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.sedation_button:
                addSedation();
                break;
        }
    }

    private void addSedation() {
        Utilities.showSedationCodeSummary(sedationTime, patientOver5YrsOld,
                sameMDPerformsSedation, sedationStatus, sedationCodes, this);

    }

}
