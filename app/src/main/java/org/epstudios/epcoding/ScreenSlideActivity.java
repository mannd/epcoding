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

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v13.app.FragmentStatePagerAdapter;
import android.support.v4.app.NavUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static org.epstudios.epcoding.Constants.WIZARD_AGE;
import static org.epstudios.epcoding.Constants.WIZARD_SAME_MD;
import static org.epstudios.epcoding.Constants.WIZARD_SEDATION_STATUS;
import static org.epstudios.epcoding.Constants.WIZARD_TIME;

/**
 * Demonstrates a "screen-slide" animation using a {@link ViewPager}. Because
 * {@link ViewPager} automatically plays such an animation when calling
 * {@link ViewPager#setCurrentItem(int)}, there isn't any animation-specific
 * code in this sample.
 * 
 * <p>
 * This sample shows a "next" button that advances the user to the next step in
 * a wizard, animating the current screen out (to the left) and the next screen
 * in (from the right). The reverse animation is played when the user presses
 * the "previous" button.
 * </p>
 * 
 * @see ScreenSlidePageFragment
 */
public class ScreenSlideActivity extends SimpleActionBarActivity {

	/**
	 * The number of pages (wizard steps) to show in this demo.
	 */
	private static final int NUM_PAGES = 7;

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next wizard steps.
	 */
	private ViewPager mPager;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	private PagerAdapter mPagerAdapter;

	// Settings
	// boolean plusShownInDisplay; // show plus in main display
	// boolean allowChangingPrimaryCodes;
	private boolean plusShownInSummary;
	private boolean codeDescriptionInSummary;
	private boolean descriptionTruncatedInSummary;
	private boolean codeCheckAllCodes;
	private String codeVerbosity;
	private boolean useUnicodeSymbols;


	private static final String[] revisionCodeNumbers = {"33215", "33226", "33218",
			"33220", "33222", "33223"};

	private static final String[] removalCodeNumbers = {"33233", "33241", "33234",
			"33235", "33244"};

	private static final String[] addingCodeNumbers = {"33206", "33207", "33208",
			"33249", "33216", "33217", "33225", "33212", "33213", "33240",
			"33230", "33231"};

	private boolean sameMD = true;
	private boolean ageOver5 = true;
	private int sedationTime = 0;
	private SedationStatus sedationStatus = SedationStatus.Unassigned;
	private List<Code> sedationCodes = new ArrayList<>();


	public boolean isSameMD() {
		return sameMD;
	}

	public void setSameMD(boolean sameMD) {
		this.sameMD = sameMD;
	}

	public boolean isAgeOver5() {
		return ageOver5;
	}

	public void setAgeOver5(boolean ageOver5) {
		this.ageOver5 = ageOver5;
	}

	public int getSedationTime() {
		return sedationTime;
	}

	public void setSedationTime(int sedationTime) {
		this.sedationTime = sedationTime;
	}

	public SedationStatus getSedationStatus() {
		return sedationStatus;
	}

	public void setSedationStatus(SedationStatus sedationStatus) {
		this.sedationStatus = sedationStatus;
	}


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_screen_slide);
		initToolbar();
		// Instantiate a ViewPager and a PagerAdapter.
		mPager = (ViewPager) findViewById(R.id.pager);
		mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
		mPager.setAdapter(mPagerAdapter);
		mPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// When changing pages, reset the action bar actions since they
				// are dependent
				// on which page is currently active. An alternative approach is
				// to have each
				// fragment expose actions itself (rather than the activity
				// exposing actions),
				// but for simplicity, the activity provides the actions in this
				// sample.
				invalidateOptionsMenu();

			}
		});

        loadSettings();

		Code[] revisionCodes = Codes.getCodes(revisionCodeNumbers);
		Code[] removalCodes = Codes.getCodes(removalCodeNumbers);
		Code[] addingCodes = Codes.getCodes(addingCodeNumbers);
		Code[] finalCodes = Codes.getCodes(Codes.icdReplacementSecondaryCodeNumbers);

		List<Code> allCodes = new ArrayList<>();
		allCodes.addAll(Arrays.asList(revisionCodes));
		allCodes.addAll(Arrays.asList(removalCodes));
		allCodes.addAll(Arrays.asList(addingCodes));
		allCodes.addAll(Arrays.asList(finalCodes));


		if (savedInstanceState != null) {
			sedationTime = savedInstanceState.getInt(WIZARD_TIME, sedationTime);
			sedationStatus = SedationStatus.stringToSedationStatus(
					savedInstanceState.getString(WIZARD_SEDATION_STATUS));
			sameMD = savedInstanceState.getBoolean(WIZARD_SAME_MD, sameMD);
			ageOver5 = savedInstanceState.getBoolean(WIZARD_AGE, ageOver5);
			sedationCodes.clear();
			sedationCodes.addAll(SedationCode.sedationCoding(sedationTime,
					sameMD, ageOver5, sedationStatus));
		}
		else {
			Codes.resetTempAddedModifiers(allCodes, this);
			// remove old selections when starting wizard
			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
			SharedPreferences.Editor editor = prefs.edit();
			editor.remove("wizardrevisioncodes");
			editor.remove("wizardremovalcodes");
			editor.remove("wizardaddingcodes");
			editor.remove("wizardfinalcodes");
			editor.apply();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.activity_screen_slide, menu);

		menu.findItem(R.id.action_previous).setEnabled(
				mPager.getCurrentItem() > 0);

		// Add either a "next" or "finish" button to the action bar, depending
		// on which page
		// is currently selected.
		MenuItem item = menu
				.add(Menu.NONE,
						R.id.action_next,
						Menu.NONE,
						(mPager.getCurrentItem() == mPagerAdapter.getCount() - 1) ? R.string.action_finish
								: R.string.action_next);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM
				| MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			// Navigate "up" the demo structure to the launchpad activity.
			// See http://developer.android.com/design/patterns/navigation.html
			// for more.
			NavUtils.navigateUpFromSameTask(this);
			return true;

		case R.id.action_previous:
			// Go to the previous step in the wizard. If there is no previous
			// step,
			// setCurrentItem will do nothing.
			mPager.setCurrentItem(mPager.getCurrentItem() - 1);
			return true;

		case R.id.action_next:
			// Advance to the next step in the wizard. If there is no next step,
			// setCurrentItem
			// will do nothing.
			if (mPager.getCurrentItem() == NUM_PAGES - 1) {
				displayResult();
			} else {
				mPager.setCurrentItem(mPager.getCurrentItem() + 1);
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	private void displayResult() {
		Set<String> codeNumbers = loadCodeNumbers();
		summarizeCoding(codeNumbers);
	}

	private void summarizeCoding(Set<String> codeNumbers) {
		String message = "";
		// we will extract the raw selected codes and shoot them to the code
		// analyzer, as well as check for no primary or secondary codes
		String[] codeNumberArray = codeNumbers.toArray(new String[codeNumbers
				.size()]);
		Code[] codes = new Code[codeNumbers.size()];
		for (int i = 0; i < codes.length; ++i) {
			codes[i] = Codes.getCode(codeNumberArray[i]);
			//message += codes[i].getCodeFirstFormatted() + "\n";
			message += getSummaryFromCode(codes[i]);
		}
		List<Code> allCodes = new ArrayList<>();
		allCodes.addAll(Arrays.asList(codes));
		sedationCodes = SedationCode.sedationCoding(sedationTime, sameMD,
				ageOver5, sedationStatus);
		for (Code code : sedationCodes) {
			//message += code.getCodeFirstFormatted() + "\n";
			message += getSummaryFromCode(code);
		}
		allCodes.addAll(sedationCodes);
		message += Utilities.simpleCodeAnalysis(allCodes, sedationStatus, useUnicodeSymbols, this);
		displayResult(getString(R.string.coding_summary_dialog_label), message,
				this);
	}

	// does not load sedation codes
	private Set<String> loadCodeNumbers() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		Set<String> defaultStringSet = new TreeSet<>();
		Set<String> codeNumbersChecked = new TreeSet<>();
		codeNumbersChecked.addAll(prefs.getStringSet("wizardrevisioncodes",
				defaultStringSet));
		codeNumbersChecked.addAll(prefs.getStringSet("wizardremovalcodes",
				defaultStringSet));
		codeNumbersChecked.addAll(prefs.getStringSet("wizardaddingcodes",
				defaultStringSet));
		codeNumbersChecked.addAll(prefs.getStringSet("wizardfinalcodes",
				defaultStringSet));
		return codeNumbersChecked;
	}

	private void displayResult(String title, String message, Context context) {
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setMessage(message);
		dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Exit Wizard",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						finish();
					}
				});
		dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Cancel",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
                        // just close dialog
					}
				});
		dialog.setTitle(title);
		dialog.show();
	}

	/**
	 * A simple pager adapter that represents 7 {@link ScreenSlidePageFragment}
	 * objects, in sequence.
	 */
	private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
		public ScreenSlidePagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			return ScreenSlidePageFragment.create(position);
		}

		@Override
		public int getCount() {
			return NUM_PAGES;
		}
	}

	@Override
	public void onSaveInstanceState(Bundle bundle) {
		super.onSaveInstanceState(bundle);
		bundle.putBoolean(WIZARD_SAME_MD, sameMD);
		bundle.putBoolean(WIZARD_AGE, ageOver5);
		bundle.putInt(WIZARD_TIME, sedationTime);
		bundle.putString(WIZARD_SEDATION_STATUS, sedationStatus.toString());

	}

	private void loadSettings() {
		SharedPreferences sharedPreferences = PreferenceManager
				.getDefaultSharedPreferences(this);
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

	private String getSummaryFromCode(Code code) {
		code.setPlusShown(plusShownInSummary);
		code.setDescriptionShown(codeDescriptionInSummary);
		code.setDescriptionShortened(descriptionTruncatedInSummary);
		return code.getCodeFirstFormatted() + "\n";
	}
}
