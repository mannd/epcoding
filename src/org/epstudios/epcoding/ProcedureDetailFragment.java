package org.epstudios.epcoding;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * A fragment representing a single Procedure detail screen. This fragment is
 * either contained in a {@link ProcedureListActivity} in two-pane mode (on
 * tablets) or a {@link ProcedureDetailActivity} on handsets.
 */
public class ProcedureDetailFragment extends Fragment implements
		OnClickListener {
	/**
	 * The fragment argument representing the item ID that this fragment
	 * represents.
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * The content this fragment is presenting.
	 */
	private int mItem;

	private LinearLayout primaryCheckBoxLayout;
	private LinearLayout secondaryCheckBoxLayout;

	private Button summarizeButton;
	private Button clearButton;

	private final Map<String, CodeCheckBox> primaryCheckBoxMap = new HashMap<String, CodeCheckBox>();
	private final Map<String, CodeCheckBox> secondaryCheckBoxMap = new HashMap<String, CodeCheckBox>();

	private final int numOtherProcedures = 1;
	private final Code[] otherProcedureCodes = new Code[numOtherProcedures];

	// these correspond with the procedure list order
	final private int afbAblation = 0;
	final private int svtAblation = 1;
	final private int vtAblation = 2;
	final private int epTesting = 3;
	// final private int pacemakers = 4;
	final private int ppmReplacement = 7;
	//
	// final private int otherProcedures = 8;

	final private Set<Integer> ablationProceduresSet = new TreeSet<Integer>();

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProcedureDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		if (getArguments().containsKey(ARG_ITEM_ID)) {
			// Load the content specified by the fragment
			// arguments.
			String itemID = getArguments().getString(ARG_ITEM_ID);
			if (itemID != null) // if it is null, screen 0 (AFB ablation) will
								// be shown
				mItem = Integer.parseInt(itemID);
		}
		ablationProceduresSet.add(afbAblation);
		ablationProceduresSet.add(svtAblation);
		ablationProceduresSet.add(vtAblation);
		ablationProceduresSet.add(epTesting);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
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
		View rootView;
		// / TODO rename ablation_afb layout
		rootView = inflater.inflate(R.layout.ablation_afb, container, false);
		primaryCheckBoxLayout = (LinearLayout) rootView
				.findViewById(R.id.primary_checkbox_layout);
		secondaryCheckBoxLayout = (LinearLayout) rootView
				.findViewById(R.id.secondary_checkbox_layout);

		Context context = getActivity();

		// initialize other procedure codes
		otherProcedureCodes[0] = Codes.getCode("99999");

		if (ablationProceduresSet.contains(mItem)) {
			Code[] ablationSecondaryCodes = Codes.getAblationSecondaryCodes();
			for (int i = 0; i < ablationSecondaryCodes.length; ++i) {
				CodeCheckBox secondaryCheckBox = new CodeCheckBox(context);
				secondaryCheckBox.setCode(ablationSecondaryCodes[i]);
				secondaryCheckBoxMap.put(
						ablationSecondaryCodes[i].getCodeNumber(),
						secondaryCheckBox);
				secondaryCheckBoxLayout.addView(secondaryCheckBox);

			}
		}

		String[] disabledCodeNumbers = { "99999" };
		String[] primaryCodeNumbers = disabledCodeNumbers;
		if (mItem == afbAblation) {

			primaryCodeNumbers = Codes.afbAblationPrimaryCodeNumbers;
			disabledCodeNumbers = Codes.afbAblationDisabledCodeNumbers;
		} else if (mItem == svtAblation) {

			primaryCodeNumbers = Codes.svtAblationPrimaryCodeNumbers;
			disabledCodeNumbers = Codes.svtAblationDisabledCodeNumbers;
		} else if (mItem == vtAblation) {

			primaryCodeNumbers = Codes.vtAblationPrimaryCodeNumbers;
			disabledCodeNumbers = Codes.vtAblationDisabledCodeNumbers;
		} else if (mItem == epTesting) {

			primaryCodeNumbers = Codes.epTestingPrimaryCodeNumbers;
			disabledCodeNumbers = Codes.epTestingDisabledCodeNumbers;
		}
		for (int i = 0; i < disabledCodeNumbers.length; ++i)
			secondaryCheckBoxMap.get(disabledCodeNumbers[i]).disable();

		Code[] primaryCodes = Codes.getCodes(primaryCodeNumbers);
		for (int i = 0; i < primaryCodeNumbers.length; ++i) {
			CodeCheckBox primaryCheckBox = new CodeCheckBox(context);
			primaryCheckBox.setCode(primaryCodes[i]);
			primaryCheckBoxMap.put(primaryCodes[i].getCodeNumber(),
					primaryCheckBox);
			primaryCheckBoxLayout.addView(primaryCheckBox);
		}

		if (ablationProceduresSet.contains(mItem)) {
			// check and disable primary checkboxes for ablation type items
			for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
					.entrySet()) {
				entry.getValue().setEnabled(false);
				entry.getValue().setChecked(true);
			}
		}

		// apply saved configurations here
		loadCoding();

		summarizeButton = (Button) rootView.findViewById(R.id.summary_button);
		summarizeButton.setOnClickListener(this);
		clearButton = (Button) rootView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		return rootView;
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
		Context context = getActivity();
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		String message = "";
		for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
				.entrySet()) {
			if (entry.getValue().isChecked())
				message += entry.getValue().getCode().getCodeNumberWithAddOn()
						+ "\n";
		}
		for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
				.entrySet()) {
			if (entry.getValue().isChecked())
				message += entry.getValue().getCode().getCodeNumberWithAddOn()
						+ "\n";
		}
		if (message.isEmpty())
			message = getString(R.string.no_codes_selected_label);

		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.coding_summary_label));
		dialog.show();

	}

	public void saveCoding() {
		Context context = getActivity();
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
		Context context = getActivity();
		Toast toast = Toast.makeText(context, "Saving codes",
				Toast.LENGTH_SHORT);
		toast.show();
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String mItemString = String.valueOf(mItem);
		Set<String> checkedCodeNumbers = new TreeSet<String>();
		for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
				.entrySet()) {
			if (entry.getValue().isChecked())
				checkedCodeNumbers.add(entry.getValue().getCode()
						.getCodeNumber());
		}
		SharedPreferences.Editor prefsEditor = prefs.edit();
		prefsEditor.putStringSet(mItemString, checkedCodeNumbers);
		prefsEditor.commit();
	}

	public void loadCoding() {
		Context context = getActivity();
		Toast toast = Toast.makeText(context, "Loading codes",
				Toast.LENGTH_SHORT);
		toast.show();
		load(context);
	}

	private void load(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String mItemString = String.valueOf(mItem);
		Set<String> defaultStringSet = new TreeSet<String>();
		defaultStringSet.add("99999");
		Set<String> codeNumbersChecked = prefs.getStringSet(mItemString,
				defaultStringSet);
		String numbers = codeNumbersChecked.toString();
		for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
				.entrySet()) {
			if (codeNumbersChecked.contains(entry.getValue().getCode()
					.getCodeNumber()))
				entry.getValue().setChecked(true);
		}
		Toast toast = Toast.makeText(context, numbers, Toast.LENGTH_SHORT);
		toast.show();

	}

}
