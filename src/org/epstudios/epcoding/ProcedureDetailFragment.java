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
	 * The dummy content this fragment is presenting.
	 */
	private int mItem;

	/**
	 * Mandatory empty constructor for the fragment manager to instantiate the
	 * fragment (e.g. upon screen orientation changes).
	 */
	public ProcedureDetailFragment() {
	}

	private Button clearButton;

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
			if (mItem == 0) {
				CheckBox v = (CheckBox) rootView
						.findViewById(R.id.la_pace_record);
				v.setChecked(false);
				v.setEnabled(false);
				v = (CheckBox) rootView.findViewById(R.id.transseptal_cath);
				v.setChecked(false);
				v.setEnabled(false);
				// ??v.setPaintFlags(v.getPaintFlags()
				// & ~Paint.STRIKE_THRU_TEXT_FLAG);
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
	}
}
