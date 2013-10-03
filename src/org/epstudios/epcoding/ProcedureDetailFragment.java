package org.epstudios.epcoding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
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
public class ProcedureDetailFragment extends Fragment {
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
	private CheckBox additionalAfbCheckBox;
	private CheckBox twoDMapCheckBox;
	private CheckBox threeDMapCheckBox;
	private CheckBox laPaceRecordCheckBox;
	private CheckBox lvPaceRecordCheckBox;
	private CheckBox transseptalCathCheckBox;

	private final int numCheckBoxes = 9;
	private final CheckBox[] checkBoxes = new CheckBox[numCheckBoxes];

	private TextView codeTextView;

	final private int afbAblation = 0;
	final private int svtAblation = 1;
	final private int vtAblation = 2;

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
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView;

		rootView = inflater.inflate(R.layout.ablation_afb, container, false);
		codeTextView = (TextView) rootView.findViewById(R.id.code_title);
		checkBoxLayout = (LinearLayout) rootView
				.findViewById(R.id.checkbox_layout);

		checkBoxes[0] = (CheckBox) rootView.findViewById(R.id.additional_svt);
		CheckBox testCheckBox = new CheckBox(getActivity());
		testCheckBox.setText(Codes.getCode("99999").getDescription());
		checkBoxLayout.addView(testCheckBox);
		checkBoxes[1] = additionalAfbCheckBox = (CheckBox) rootView
				.findViewById(R.id.additional_afb);
		checkBoxes[2] = twoDMapCheckBox = (CheckBox) rootView
				.findViewById(R.id.two_d_map);
		checkBoxes[3] = threeDMapCheckBox = (CheckBox) rootView
				.findViewById(R.id.three_d_map);
		checkBoxes[4] = laPaceRecordCheckBox = (CheckBox) rootView
				.findViewById(R.id.la_pace_record);
		checkBoxes[5] = lvPaceRecordCheckBox = (CheckBox) rootView
				.findViewById(R.id.lv_pace_record);
		checkBoxes[6] = (CheckBox) rootView.findViewById(R.id.iv_drug);
		checkBoxes[7] = (CheckBox) rootView.findViewById(R.id.ice);
		checkBoxes[8] = transseptalCathCheckBox = (CheckBox) rootView
				.findViewById(R.id.transseptal_cath);
		clearButton = (Button) rootView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.clear_button:
					clearEntries();
					break;
				}
			}
		});

		if (mItem == afbAblation) {
			codeTextView.setText(Codes.getCode("93656").getDescription());
			laPaceRecordCheckBox.setChecked(false);
			laPaceRecordCheckBox.setEnabled(false);
			transseptalCathCheckBox.setChecked(false);
			transseptalCathCheckBox.setEnabled(false);
			// ??v.setPaintFlags(v.getPaintFlags()
			// & ~Paint.STRIKE_THRU_TEXT_FLAG);

		} else if (mItem == svtAblation) {
			codeTextView.setText(getString(R.string.code_93653_label));
			additionalAfbCheckBox.setChecked(false);
			additionalAfbCheckBox.setEnabled(false);
		} else if (mItem == vtAblation) {
			codeTextView.setText(getString(R.string.code_93654_label));
			additionalAfbCheckBox.setChecked(false);
			additionalAfbCheckBox.setEnabled(false);
			twoDMapCheckBox.setChecked(false);
			twoDMapCheckBox.setEnabled(false);
			threeDMapCheckBox.setChecked(false);
			threeDMapCheckBox.setEnabled(false);
			lvPaceRecordCheckBox.setEnabled(false);
			lvPaceRecordCheckBox.setEnabled(false);

		}

		return rootView;
	}

	private void clearEntries() {
		for (int i = 0; i < numCheckBoxes; ++i)
			checkBoxes[i].setChecked(false);
	}
}
