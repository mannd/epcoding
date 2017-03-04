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

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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
    public static final String EPCODING = "EPCODING";

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
	// shorten description based on screen width?

    // don't try to refactor these using Utilities.createCheckBoxLayoutAndCodeMap,
	// as then will have to put in many check for null map.
	private final Map<String, CodeCheckBox> primaryCheckBoxMap = new LinkedHashMap<>();
	private final Map<String, CodeCheckBox> secondaryCheckBoxMap = new LinkedHashMap<>();

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

    private LinearLayout primaryCheckBoxLayout;
    private LinearLayout secondaryCheckBoxLayout;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProcedureDetailFragment() {

	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			String itemID = getArguments().getString(ARG_ITEM_ID);
			try {
				mItem = Integer.parseInt(itemID);
			} catch (NumberFormatException e) {
				mItem = 0; // AFB ablation will be shown
			}
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.menu, menu);
	}

	@Override
	public void onResume() {
		super.onResume();
		loadSettings();
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
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
	}

	@Override
    public void onClick(View v) {
        Log.d(EPCODING, "click view");
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

        primaryCheckBoxLayout = (LinearLayout) rootView
                .findViewById(R.id.primary_checkbox_layout);
        secondaryCheckBoxLayout = (LinearLayout) rootView
                .findViewById(R.id.secondary_checkbox_layout);
        TextView primaryCodeTextView = (TextView) rootView
                .findViewById(R.id.primary_code_textView);
        TextView secondaryCodeTextView = (TextView) rootView
                .findViewById(R.id.secondary_code_textView);

		loadSettings();

		switch (mItem) {
		case allProcedures:
			procedure = new AllCodes();
			break;
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
		default:
			procedure = new AllCodes();
			break;
		}

		Codes.clearMultipliersAndModifiers(allPrimaryAndSecondaryCodes());
        if (mItem != allProcedures) {
            Codes.loadDefaultModifiers(allPrimaryAndSecondaryCodes());
        }
        // TODO: load saved modifiers
        //Codes.loadSavedModifiers(allPrimaryAndSecondaryCodes(), context);


		getActivity().setTitle(procedure.title(context));

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

		for (int i = 0; i < disabledCodeNumbers.length; ++i)
			secondaryCheckBoxMap.get(disabledCodeNumbers[i]).disable();



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
				entry.getValue().setChecked(primaryCodesState != null ? primaryCodesState[i++] : false);
			i = 0;
			for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
					.entrySet())
				entry.getValue().setChecked(secondaryCodesState != null ? secondaryCodesState[i++] : false);
		} else
			loadCoding();
		// set up buttons
        Button sedationButton = (Button)rootView.findViewById(R.id.sedation_button);
        sedationButton.setOnClickListener(this);
        Button summarizeButton = (Button) rootView.findViewById(R.id.summary_button);
		summarizeButton.setOnClickListener(this);
        Button clearButton = (Button) rootView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);

        return rootView;
	}

	private void createCheckBoxLayoutAndCodeMap(Code[] codes,
			Map<String, CodeCheckBox> codeCheckBoxMap, LinearLayout layout) {
		for (int i = 0; i < codes.length; ++i) {
			final CodeCheckBox codeCheckBox = new CodeCheckBox(context);
			// codes[i].setPlusShown(plusShownInDisplay);
			codeCheckBox.setCodeFirst(mItem == allProcedures);
			codeCheckBox.setCode(codes[i]);
			codeCheckBoxMap.put(codes[i].getCodeNumber(), codeCheckBox);
            // add long click listener to open modifier dialog
            // TODO: Note that Primary and disabled codes can't be long-clicked
            // because they are disabled.  This is an Android limitation, though
            // any code can be long clicked and modified in the all codes module.
            codeCheckBox.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Log.v(EPCODING, "on long click");
                    Log.v(EPCODING, "Active code number = " + codeCheckBox.getCodeNumber());
                    Intent intent = new Intent(getActivity(), ModifierActivity.class);
                    intent.putExtra("ACTIVE_CODE_NUMBER", codeCheckBox.getCodeNumber());
                    startActivityForResult(intent, 1);
                    return true;
                }
            });
			layout.addView(codeCheckBox);
		}
	}

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if(resultCode == Activity.RESULT_OK){
                String[] result=data.getStringArrayExtra("MODIFIER_RESULT");
				Log.d(EPCODING, "Result = " + result.length);
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

    // TODO: apparently all codes module uses primary codes and no secondary, so
    // both below are needed.  Need to allow allcodes module to display modifiers in its
    // list, which uses code first.
    private CodeCheckBox getCheckBoxWithCode(String codeNumber) {
        String result;
        for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap.entrySet()) {
            result = entry.getValue().getCodeNumber();
            if (result.equals(codeNumber)) {
                return entry.getValue();
            }
        }
        for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap.entrySet()) {
            result = entry.getValue().getCodeNumber();
            if (result.equals(codeNumber)) {
                return entry.getValue();
            }
        }
        return null;
     }

	private List<Code> allPrimaryAndSecondaryCodes() {
		List<Code> allCodes = new ArrayList<>();
		Code[] primaryCodes = procedure.primaryCodes();
		Code[] secondaryCodes = procedure.secondaryCodes();
		for (int i = 0; i < primaryCodes.length; i++) {
			allCodes.add(primaryCodes[i]);
		}
		for (int i = 0; i < secondaryCodes.length; i++) {
			allCodes.add(secondaryCodes[i]);
		}
		return allCodes;
	}

    private void addSedation() {
        Log.d(EPCODING, "addSedation");
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
		String message = "";
		// we will extract the raw selected codes and shoot them to the code
		// analyzer, as well as check for no primary or secondary codes
		Code[] codes = new Code[Codes.allCodesSize()];
		int i = 0;
		for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
				.entrySet()) {
			if (entry.getValue().isChecked()) {
				codes[i] = entry.getValue().getCode();
				codes[i].setPlusShown(plusShownInSummary);
				codes[i].setDescriptionShown(codeDescriptionInSummary);
				codes[i].setDescriptionShortened(descriptionTruncatedInSummary);
				message += codes[i++].getCodeFirstFormatted() + "\n";
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
				codes[i].setPlusShown(plusShownInSummary);
				codes[i].setDescriptionShown(codeDescriptionInSummary);
				codes[i].setDescriptionShortened(descriptionTruncatedInSummary);
				message += codes[i++].getCodeFirstFormatted() + "\n";
			}
		}
		boolean noSecondaryCodes = primaryCodeCounter == i;
		boolean moduleHasNoSecondaryCodesNeedingChecking = procedure
				.doNotWarnForNoSecondaryCodesSelected();
		boolean noAnalysis = codeVerbosity.equals("None")
				|| (mItem == allProcedures && !codeCheckAllCodes);
		message += Utilities.codeAnalysis(codes, noPrimaryCodes,
				noSecondaryCodes, moduleHasNoSecondaryCodesNeedingChecking,
				noAnalysis, codeVerbosity.equals("Verbose"), context);
		Utilities.displayMessage(
				getString(R.string.coding_summary_dialog_label), message,
				context);
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
			if (codeNumbersChecked.contains(entry.getValue().getCodeNumber()))
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
	}
}
