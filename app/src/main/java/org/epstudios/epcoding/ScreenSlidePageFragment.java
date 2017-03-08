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
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import static org.epstudios.epcoding.ProcedureDetailFragment.EPCODING;

/**
 * A fragment representing a single step in a wizard. The fragment shows a dummy
 * title indicating the page number, along with some dummy text.
 * 
 * <p>
 * This class is used by the {CardFlipActivity} and
 * {@link ScreenSlideActivity} samples.
 * </p>
 */
public class ScreenSlidePageFragment extends Fragment implements View.OnClickListener {
	/**
	 * The argument key for the page number this fragment represents.
	 */
	private static final String ARG_PAGE = "page";

	private static final String[] revisionCodeNumbers = { "33215", "33226", "33218",
			"33220", "33222", "33223" };

	private static final String[] removalCodeNumbers = { "33233", "33241", "33234",
			"33235", "33244" };

	private static final String[] addingCodeNumbers = { "33206", "33207", "33208",
			"33249", "33216", "33217", "33225", "33212", "33213", "33240",
			"33230", "33231" };

	private Map<String, CodeCheckBox> removalCheckBoxMap;
	private Map<String, CodeCheckBox> addingCheckBoxMap;
	private Map<String, CodeCheckBox> finalCheckBoxMap;

	/**
	 * The fragment's page number, which is set to the argument value for
	 * {@link #ARG_PAGE}.
	 */
	private int mPageNumber;
	private Context context;
	private Code[] removalCodes;
	private Code[] addingCodes;
	private Code[] finalCodes;
	List<Code> allCodes;

	private Button sedationButton;
	private SedationStatus sedationStatus = SedationStatus.Unassigned;
	private List<Code> sedationCodes = new ArrayList<>();

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
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// select layout based on page number
		int layout = R.layout.fragment_screen_slide_page;
		ViewGroup rootView = (ViewGroup) inflater.inflate(layout, container,
				false);
		LinearLayout removedCheckBoxLayout = (LinearLayout) rootView
				.findViewById(R.id.removed_hardware);
		removedCheckBoxLayout.setVisibility(View.GONE);
		LinearLayout addingCheckBoxLayout = (LinearLayout) rootView
				.findViewById(R.id.adding_hardware);
		addingCheckBoxLayout.setVisibility(View.GONE);
		LinearLayout finalCheckBoxLayout = (LinearLayout) rootView
				.findViewById(R.id.final_codes);
		finalCheckBoxLayout.setVisibility(View.GONE);

		sedationButton = (Button)rootView.findViewById(R.id.sedation_button);
		sedationButton.setOnClickListener(this);
		sedationButton.setVisibility(View.GONE);

		removalCodes = Codes.getCodes(removalCodeNumbers);
		addingCodes = Codes.getCodes(addingCodeNumbers);
		finalCodes = Codes.getCodes(Codes.icdReplacementSecondaryCodeNumbers);

		allCodes = new ArrayList<>();
		allCodes.addAll(Arrays.asList(removalCodes));
		allCodes.addAll(Arrays.asList(addingCodes));
		allCodes.addAll(Arrays.asList(finalCodes));

		Codes.clearMultipliersAndModifiers(allCodes);
		Codes.loadDefaultModifiers(allCodes);
		Codes.loadSavedModifiers(allCodes, context);

		Context context = getActivity();
		removalCheckBoxMap = Utilities.createCheckBoxLayoutAndCodeMap(
				removalCodes, removedCheckBoxLayout, context, true, this);
		addCheckMarkListener(removalCheckBoxMap);
		addingCheckBoxMap = Utilities.createCheckBoxLayoutAndCodeMap(
				addingCodes, addingCheckBoxLayout, context, true, this);
		addCheckMarkListener(addingCheckBoxMap);
		finalCheckBoxMap = Utilities.createCheckBoxLayoutAndCodeMap(finalCodes,
				finalCheckBoxLayout, context, true, this);
		addCheckMarkListener(finalCheckBoxMap);
		// Set the title view to show the page number.
		((TextView) rootView.findViewById(android.R.id.text1))
				.setText(getString(R.string.title_template_step,
						mPageNumber + 1));
		TextView headingText = (TextView) rootView
				.findViewById(R.id.slide_help_text);
		checkChanged();
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
				Code[] codes = Codes.getCodes(revisionCodeNumbers);
				String text = getString(R.string.slide_step3_heading_text) + "\n\n";
				for (Code code : codes) {
					text += code.getUnformattedNumberFirst() + "\n";
				}
				headingText.setText(text);
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
			boolean[] removalCodesState = savedInstanceState
					.getBooleanArray("removal_codes");
			boolean[] addingCodesState = savedInstanceState
					.getBooleanArray("adding_codes");
			boolean[] finalCodesState = savedInstanceState
					.getBooleanArray("final_codes");
			int i = 0;
			for (Map.Entry<String, CodeCheckBox> entry : removalCheckBoxMap
					.entrySet())
				entry.getValue().setChecked(removalCodesState != null ? removalCodesState[i++] : false);
			i = 0;
			for (Map.Entry<String, CodeCheckBox> entry : addingCheckBoxMap
					.entrySet())
				entry.getValue().setChecked(addingCodesState != null ? addingCodesState[i++] : false);
			i = 0;
			for (Map.Entry<String, CodeCheckBox> entry : finalCheckBoxMap
					.entrySet())
				entry.getValue().setChecked(finalCodesState != null ? finalCodesState[i++] : false);
		}

		return rootView;
	}

	/**
	 * Returns the page number represented by this fragment object.
	 */
	public int getPageNumber() {
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
		// store check marks here
		boolean[] removalCodeState = new boolean[removalCheckBoxMap.size()];
		int i = 0;
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
		savedInstanceState.putBooleanArray("removal_codes", removalCodeState);
		savedInstanceState.putBooleanArray("adding_codes", addingCodeState);
		savedInstanceState.putBooleanArray("final_codes", finalCodeState);

	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {

		if (requestCode == 1) {
			if(resultCode == Activity.RESULT_OK){
				String[] result=data.getStringArrayExtra(ModifierActivity.MODIFIER_RESULT);
				Log.d(EPCODING, "Result = " + result.length);
				if (result.length == 1 && result[0].equals(ModifierActivity.RESET_MODIFIERS)) {
					resetModifiers();
					resetCodes();
					return;
				}
				Code code = Codes.setModifiersForCode(result);
				if (code != null) {
					CodeCheckBox checkBox = getCheckBoxWithCode(code.getCodeNumber());
					if (checkBox != null) {
						// forces checkbox to redraw itself
						// checkBox.invalidate() doesn't work for some reason
						checkBox.setCode(code);
					}
				}
			}
		}
	}

	private void resetModifiers() {
		Codes.resetSavedModifiers(allCodes, context);
	}

	private void resetCodes() {
		Codes.loadDefaultModifiers(allCodes);
		for (Code code : allCodes) {
			CodeCheckBox checkBox = getCheckBoxWithCode(code.getCodeNumber());
			if (checkBox != null) {
				checkBox.setCode(code);
			}
		}
	}

	private CodeCheckBox getCheckBoxWithCode(String codeNumber) {
		String result;
		for (Map.Entry<String, CodeCheckBox> entry : addingCheckBoxMap.entrySet()) {
			result = entry.getValue().getCodeNumber();
			if (result.equals(codeNumber)) {
				return entry.getValue();
			}
		}
		for (Map.Entry<String, CodeCheckBox> entry : removalCheckBoxMap.entrySet()) {
			result = entry.getValue().getCodeNumber();
			if (result.equals(codeNumber)) {
				return entry.getValue();
			}
		}
		for (Map.Entry<String, CodeCheckBox> entry : finalCheckBoxMap.entrySet()) {
			result = entry.getValue().getCodeNumber();
			if (result.equals(codeNumber)) {
				return entry.getValue();
			}
		}
		return null;
	}


	@Override
	public void onClick(View view) {
		Log.d("EPCODING", "clicked it!");
		switch(view.getId()) {
			case R.id.sedation_button:
				Log.d("EPCODING", "click sedation!");
				break;
		}
	}
}
