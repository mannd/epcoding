package org.epstudios.epcoding;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
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

	private Button clearButton;
	private CheckBox additionalSvtCheckBox;
	private CheckBox additionalAfbCheckBox;
	private CheckBox twoDMapCheckBox;
	private CheckBox threeDMapCheckBox;
	private CheckBox laPaceRecordCheckBox;
	private CheckBox lvPaceRecordCheckBox;
	private CheckBox ivDrugCheckBox;
	private CheckBox iceCheckBox;
	private CheckBox transseptalCathCheckBox;

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
			// Load the dummy content specified by the fragment
			// arguments. In a real-world scenario, use a Loader
			// to load content from a content provider.
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
		if (mItem >= 0 && mItem <= 2) {
			rootView = inflater
					.inflate(R.layout.ablation_afb, container, false);
			additionalSvtCheckBox = (CheckBox) rootView
					.findViewById(R.id.additional_svt);
			additionalAfbCheckBox = (CheckBox) rootView
					.findViewById(R.id.additional_afb);
			twoDMapCheckBox = (CheckBox) rootView.findViewById(R.id.two_d_map);
			threeDMapCheckBox = (CheckBox) rootView
					.findViewById(R.id.three_d_map);
			laPaceRecordCheckBox = (CheckBox) rootView
					.findViewById(R.id.la_pace_record);
			lvPaceRecordCheckBox = (CheckBox) rootView
					.findViewById(R.id.lv_pace_record);
			ivDrugCheckBox = (CheckBox) rootView.findViewById(R.id.iv_drug);
			iceCheckBox = (CheckBox) rootView.findViewById(R.id.ice);
			transseptalCathCheckBox = (CheckBox) rootView
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

			if (mItem == 0) {
				laPaceRecordCheckBox.setChecked(false);
				laPaceRecordCheckBox.setEnabled(false);
				transseptalCathCheckBox.setChecked(false);
				transseptalCathCheckBox.setEnabled(false);
				// ??v.setPaintFlags(v.getPaintFlags()
				// & ~Paint.STRIKE_THRU_TEXT_FLAG);

			}
		} else {
			rootView = inflater.inflate(R.layout.fragment_procedure_detail,
					container, false);

			((TextView) rootView.findViewById(R.id.procedure_detail))
					.setText(String.valueOf(mItem));

		}

		return rootView;
	}

	private void clearEntries() {
		additionalSvtCheckBox.setChecked(false);
		additionalAfbCheckBox.setChecked(false);
		twoDMapCheckBox.setChecked(false);
		threeDMapCheckBox.setChecked(false);
		laPaceRecordCheckBox.setChecked(false);
		lvPaceRecordCheckBox.setChecked(false);
		ivDrugCheckBox.setChecked(false);
		iceCheckBox.setChecked(false);
		transseptalCathCheckBox.setChecked(false);
	}
}
