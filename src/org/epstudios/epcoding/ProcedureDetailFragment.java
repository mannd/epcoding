package org.epstudios.epcoding;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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
	private int mItem;

	private LinearLayout checkBoxLayout;

	private Button clearButton;
	private Button summarizeButton;
	private Button infoButton;
	private CheckBox additionalAfbCheckBox;
	private CheckBox twoDMapCheckBox;
	private CheckBox threeDMapCheckBox;
	private CheckBox laPaceRecordCheckBox;
	private CheckBox lvPaceRecordCheckBox;
	private CheckBox transseptalCathCheckBox;

	private final int numCheckBoxes = 10;
	private final CheckBox[] checkBoxes = new CheckBox[numCheckBoxes];

	private TextView codeTextView;

	final private int afbAblation = 0;
	final private int svtAblation = 1;
	final private int vtAblation = 2;
	final private int epTesting = 3;

	private Code majorCode;

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

		rootView = inflater.inflate(R.layout.ablation_afb, container, false);
		codeTextView = (TextView) rootView.findViewById(R.id.code_title);
		checkBoxLayout = (LinearLayout) rootView
				.findViewById(R.id.checkbox_layout);
		Context context = getActivity();
		checkBoxes[0] = new CheckBox(context);
		checkBoxes[0].setText(Codes.getCode("93655").getDescription());

		checkBoxes[1] = additionalAfbCheckBox = new CheckBox(context);
		checkBoxes[1].setText(Codes.getCode("93657").getDescription());
		checkBoxes[2] = twoDMapCheckBox = new CheckBox(context);
		checkBoxes[2].setText(Codes.getCode("93609").getDescription());
		checkBoxes[3] = threeDMapCheckBox = new CheckBox(context);
		checkBoxes[3].setText(Codes.getCode("93613").getDescription());
		checkBoxes[4] = laPaceRecordCheckBox = new CheckBox(context);
		checkBoxes[4].setText(Codes.getCode("93621").getDescription());
		checkBoxes[5] = lvPaceRecordCheckBox = new CheckBox(context);
		checkBoxes[5].setText(Codes.getCode("93622").getDescription());
		checkBoxes[6] = new CheckBox(context);
		checkBoxes[6].setText(Codes.getCode("93623").getDescription());
		checkBoxes[7] = new CheckBox(context);
		checkBoxes[7].setText(Codes.getCode("93662").getDescription());
		checkBoxes[8] = transseptalCathCheckBox = new CheckBox(context);
		checkBoxes[8].setText(Codes.getCode("93462").getDescription());
		checkBoxes[9] = new CheckBox(context);
		checkBoxes[9].setText(Codes.getCode("36620").getDescription());
		for (int i = 0; i < numCheckBoxes; ++i)
			checkBoxLayout.addView(checkBoxes[i]);

		clearButton = (Button) rootView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		summarizeButton = (Button) rootView.findViewById(R.id.summary_button);
		summarizeButton.setOnClickListener(this);
		infoButton = (Button) rootView.findViewById(R.id.info_button);
		infoButton.setOnClickListener(this);
		// remove when all conditions covered
		majorCode = Codes.getCode("99999");
		if (mItem == afbAblation) {
			majorCode = Codes.getCode("93656");

			disableCheckBox(laPaceRecordCheckBox);
			disableCheckBox(transseptalCathCheckBox);
		} else if (mItem == svtAblation) {
			majorCode = Codes.getCode("93653");
			disableCheckBox(additionalAfbCheckBox);
		} else if (mItem == vtAblation) {
			majorCode = Codes.getCode("93654");
			disableCheckBox(additionalAfbCheckBox);
			disableCheckBox(twoDMapCheckBox);
			disableCheckBox(threeDMapCheckBox);
			disableCheckBox(lvPaceRecordCheckBox);
		} else if (mItem == epTesting) {
			majorCode = Codes.getCode("93620");
			disableCheckBox(additionalAfbCheckBox);
		}
		codeTextView.setText(majorCode.getDescription());

		return rootView;
	}

	private void disableCheckBox(CheckBox c) {
		c.setChecked(false);
		c.setEnabled(false);
	}

	private void clearEntries() {
		for (int i = 0; i < numCheckBoxes; ++i)
			checkBoxes[i].setChecked(false);
	}

	private void summarizeCoding() {
		Context context = getActivity();
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		String message = majorCode.getCode();
		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.coding_summary_label));
		dialog.show();

	}
}
