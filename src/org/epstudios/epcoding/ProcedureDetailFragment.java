package org.epstudios.epcoding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

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

	private LinearLayout primaryCheckBoxLayout;
	private LinearLayout secondaryCheckBoxLayout;

	private Button clearButton;
	private Button summarizeButton;
	private Button infoButton;
	private TextView secondaryCodeTextView;

	private final List<CodeCheckBox> primaryCheckBoxList = new ArrayList<CodeCheckBox>();

	private final Map<String, CodeCheckBox> secondaryCheckBoxMap = new HashMap<String, CodeCheckBox>();

	private final int numOtherProcedures = 1;
	private final Code[] otherProcedureCodes = new Code[numOtherProcedures];

	// these correspond with the procedure list order
	final private int afbAblation = 0;
	final private int svtAblation = 1;
	final private int vtAblation = 2;
	final private int epTesting = 3;
	// final private int pacemakers = 4;
	//
	final private int otherProcedures = 8;
	final Set<Integer> ablationProceduresSet = new TreeSet<Integer>();

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
		secondaryCodeTextView = (TextView) rootView
				.findViewById(R.id.secondary_code_textView);
		Context context = getActivity();

		// initialize other procedure codes
		otherProcedureCodes[0] = Codes.getCode("99999");

		if (ablationProceduresSet.contains(mItem)) {
			Code[] ablationCodes = Codes.getAblationSecondaryCodes();
			for (int i = 0; i < ablationCodes.length; ++i) {
				CodeCheckBox secondaryCheckBox = new CodeCheckBox(context);
				secondaryCheckBox.setCode(ablationCodes[i]);
				secondaryCheckBoxMap.put(ablationCodes[i].getCodeNumber(),
						secondaryCheckBox);
				secondaryCheckBoxLayout.addView(secondaryCheckBox);
			}
		}

		// remove when all conditions covered
		// majorCode = Codes.getCode("99999");
		majorCode = Codes.getCodes("")[0];
		if (mItem == afbAblation) {
			majorCode = Codes.getCode("93656");
			disableCheckBox(secondaryCheckBoxMap.get("93621"));
			disableCheckBox(secondaryCheckBoxMap.get("93642"));
		} else if (mItem == svtAblation) {
			majorCode = Codes.getCode("93653");
			disableCheckBox(secondaryCheckBoxMap.get("93657"));
		} else if (mItem == vtAblation) {
			majorCode = Codes.getCode("93654");
			disableCheckBox(secondaryCheckBoxMap.get("93657"));
			disableCheckBox(secondaryCheckBoxMap.get("93609"));
			disableCheckBox(secondaryCheckBoxMap.get("93613"));
			disableCheckBox(secondaryCheckBoxMap.get("93622"));
		} else if (mItem == epTesting) {
			majorCode = Codes.getCode("93620");
			disableCheckBox(secondaryCheckBoxMap.get("93657"));
			disableCheckBox(secondaryCheckBoxMap.get("93655"));
		}

		if (mItem == otherProcedures) {
			secondaryCodeTextView.setVisibility(View.GONE);
			for (int i = 0; i < otherProcedureCodes.length; ++i) {
				CodeCheckBox primaryCheckBox = new CodeCheckBox(context);
				primaryCheckBox.setCode(otherProcedureCodes[i]);
				primaryCheckBoxList.add(primaryCheckBox);
				primaryCheckBoxLayout.addView(primaryCheckBox);

			}
		} else {
			CodeCheckBox primaryCheckBox = new CodeCheckBox(context);
			primaryCheckBox.setCode(majorCode);
			primaryCheckBoxList.add(primaryCheckBox);

			ListIterator<CodeCheckBox> iter = primaryCheckBoxList
					.listIterator();
			while (iter.hasNext()) {
				CodeCheckBox c = iter.next();
				c.setChecked(true);
				c.setEnabled(false);
				primaryCheckBoxLayout.addView(c);
			}
		}

		clearButton = (Button) rootView.findViewById(R.id.clear_button);
		clearButton.setOnClickListener(this);
		summarizeButton = (Button) rootView.findViewById(R.id.summary_button);
		summarizeButton.setOnClickListener(this);
		infoButton = (Button) rootView.findViewById(R.id.info_button);
		infoButton.setOnClickListener(this);

		return rootView;
	}

	private void disableCheckBox(CheckBox c) {
		c.setChecked(false);
		c.setEnabled(false);
	}

	private void clearEntries() {
		ListIterator<CodeCheckBox> primaryIter = primaryCheckBoxList
				.listIterator();
		while (primaryIter.hasNext()) {
			CodeCheckBox c = primaryIter.next();
			if (c.isEnabled()) // only clear enabled checkboxes
				c.setChecked(false);
		}
		for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
				.entrySet())
			entry.getValue().setChecked(false);

	}

	private void summarizeCoding() {
		Context context = getActivity();
		AlertDialog dialog = new AlertDialog.Builder(context).create();
		String message = "";
		ListIterator<CodeCheckBox> iter = primaryCheckBoxList.listIterator();
		while (iter.hasNext()) {
			CodeCheckBox c = iter.next();
			if (c.isChecked())
				message += c.getCode().getCodeNumberWithAddOn() + "\n";
		}
		for (Map.Entry<String, CodeCheckBox> entry : secondaryCheckBoxMap
				.entrySet()) {
			if (entry.getValue().isChecked())
				message += entry.getValue().getCode().getCodeNumberWithAddOn()
						+ "\n";
		}

		// ListIterator<CodeCheckBox> secondaryIter = secondaryCheckBoxList
		// .listIterator();
		// while (secondaryIter.hasNext()) {
		// CodeCheckBox c = secondaryIter.next();
		// if (c.isChecked())
		// message += c.getCode().getCodeNumberWithAddOn() + "\n";
		// }
		if (message.isEmpty())
			message = getString(R.string.no_codes_selected_label);

		dialog.setMessage(message);
		dialog.setTitle(getString(R.string.coding_summary_label));
		dialog.show();

	}
}
