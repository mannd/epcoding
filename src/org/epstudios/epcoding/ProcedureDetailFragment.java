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
import android.widget.TextView;

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
	private int mItem = 0;

	private Context context;

	private LinearLayout primaryCheckBoxLayout;
	private LinearLayout secondaryCheckBoxLayout;
	private TextView secondaryCodeTextView;

	private Button summarizeButton;
	private Button clearButton;
	private Button helpButton;

	private final Map<String, CodeCheckBox> primaryCheckBoxMap = new HashMap<String, CodeCheckBox>();
	private final Map<String, CodeCheckBox> secondaryCheckBoxMap = new HashMap<String, CodeCheckBox>();

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
	final private int otherProcedure = 10;
	final private int allProcedures = 11;

	private Procedure procedure;

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

		// Toast.makeText(getActivity(), "mItem = " + mItem, Toast.LENGTH_SHORT)
		// .show();
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
		case R.id.help_button:
			help();
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
		secondaryCodeTextView = (TextView) rootView
				.findViewById(R.id.secondary_code_textView);

		loadSettings(); // no settings implemented yet

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
		case epTesting:
			// procedure = new EpTesting();
			break;
		case avnAblation:
			// procedure = new AvnAblation();
			break;
		case newPpm:
			// procedure = new NewPpm();
			break;
		case newIcd:
			// procedure = new NewIcd();
			break;
		case ppmReplacement:
			procedure = new PpmReplacement();
			break;
		case icdReplacement:
			// procedure = new IcdReplacement();
			break;
		case deviceUpgrade:
			// procedure = new DeviceUpgrade();
			break;
		case otherProcedure:
			// procedure = new OtherProcedure();
			break;
		default:
			procedure = new AllCodes();
			break;
		}

		getActivity().setTitle(procedure.title(context));

		Code[] secondaryCodes = procedure.secondaryCodes();

		if (secondaryCodes.length > 0) {
			createCheckBoxLayoutAndCodeMap(secondaryCodes,
					secondaryCheckBoxMap, secondaryCheckBoxLayout, context);
		} else {
			secondaryCheckBoxLayout.setVisibility(View.GONE);
			secondaryCodeTextView.setVisibility(View.GONE);
		}

		Code[] primaryCodes = procedure.primaryCodes();
		String[] disabledCodeNumbers = procedure.disabledCodeNumbers();

		for (int i = 0; i < disabledCodeNumbers.length; ++i)
			secondaryCheckBoxMap.get(disabledCodeNumbers[i]).disable();

		createCheckBoxLayoutAndCodeMap(primaryCodes, primaryCheckBoxMap,
				primaryCheckBoxLayout, context);

		if (procedure.disablePrimaryCodes()) {
			// check and disable primary checkboxes for ablation type items
			for (Map.Entry<String, CodeCheckBox> entry : primaryCheckBoxMap
					.entrySet()) {
				entry.getValue().setEnabled(false);
				entry.getValue().setChecked(true);
			}
		}

		// apply saved configurations here
		loadCoding();
		// set up buttons
		summarizeButton = (Button) rootView.findViewById(R.id.summary_button);
		summarizeButton.setOnClickListener(this);
		clearButton = (Button) rootView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		helpButton = (Button) rootView.findViewById(R.id.help_button);
		helpButton.setOnClickListener(this);
		return rootView;
	}

	private void createCheckBoxLayoutAndCodeMap(Code[] codes,
			Map<String, CodeCheckBox> codeCheckBoxMap, LinearLayout layout,
			Context context) {
		for (int i = 0; i < codes.length; ++i) {
			CodeCheckBox codeCheckBox = new CodeCheckBox(context);
			codeCheckBox.setCode(codes[i]);
			codeCheckBoxMap.put(codes[i].getCodeNumber(), codeCheckBox);
			layout.addView(codeCheckBox);
		}
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
		displayMessage(getString(R.string.coding_summary_label), message);
	}

	private void help() {
		String message = procedure.helpText(context);
		displayMessage(getString(R.string.help_label), message);
	}

	private void displayMessage(String title, String message) {
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		dialog.setMessage(message);
		dialog.setTitle(title);
		dialog.show();
	}

	public void saveCoding() {
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
		// Toast toast = Toast.makeText(context, "Saving codes",
		// Toast.LENGTH_SHORT);
		// toast.show();
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
		// Toast toast = Toast.makeText(context, "Loading codes",
		// Toast.LENGTH_SHORT);
		// toast.show();
		load(context);
	}

	private void load(Context context) {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);
		String mItemString = String.valueOf(mItem);
		Set<String> defaultStringSet = new TreeSet<String>();
		Set<String> codeNumbersChecked = prefs.getStringSet(mItemString,
				defaultStringSet);
		// String numbers = codeNumbersChecked.toString();
		for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
				.entrySet()) {
			if (codeNumbersChecked.contains(entry.getValue().getCode()
					.getCodeNumber()))
				entry.getValue().setChecked(true);
		}
		// Toast toast = Toast.makeText(context, numbers, Toast.LENGTH_SHORT);
		// toast.show();

	}

	public void loadSettings() {
		// no setting so far, but just in case
		// Toast toast = Toast.makeText(context, "Loading settings",
		// Toast.LENGTH_SHORT);
		// toast.show();
	}

	public void saveSettings() {
		// Toast toast = Toast.makeText(context, "Saving settings",
		// Toast.LENGTH_SHORT);
		// toast.show();
	}

}
