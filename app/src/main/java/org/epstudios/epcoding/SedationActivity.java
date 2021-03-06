/*
  Copyright (C) 2017 EP Studios, Inc.
  www.epstudiossoftware.com
  <p>
  Created by mannd on 3/4/17.
  <p>
  This file is part of epcoding.
  <p>
  epcoding is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.
  <p>
  epcoding is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.
  <p>
  You should have received a copy of the GNU General Public License
  along with epcoding.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epcoding;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.util.List;
import java.util.Locale;

import static org.epstudios.epcoding.Constants.SEDATION_TIME;

public class SedationActivity extends BasicActionBarActivity implements View.OnClickListener {

    private final int SEDATION_CALCULATOR_REQUEST_CODE = 1;

    private int sedationTime = 0;
    private boolean ageOver5 = true;
    private boolean sameMD = true;
    private SedationStatus sedationStatus;

    private EditText timeEditText;
    private CheckBox ageCheckBox;
    private CheckBox sameMDCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sedation);

        initToolbar();

        sedationTime = getIntent().getIntExtra("TIME", 0);
        ageOver5 = getIntent().getBooleanExtra("AGE", true);
        sameMD = getIntent().getBooleanExtra("SAME_MD", true);
        sedationStatus = (SedationStatus)getIntent().getSerializableExtra("SEDATION_STATUS");

        Button calculateTimeButton = findViewById(R.id.calculate_sedation_button);
        Button cancelButton = findViewById(R.id.cancel_button);
        Button noSedationButton = findViewById(R.id.no_sedation_button);
        Button addButton = findViewById(R.id.add_sedation_button);
        calculateTimeButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        noSedationButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        timeEditText = findViewById(R.id.sedation_time_edit_text);
        ageCheckBox = findViewById(R.id.pt_over_5_checkbox);
        sameMDCheckBox = findViewById(R.id.same_md_checkbox);

        //noinspection RedundantStringFormatCall
        timeEditText.setText(String.format(Locale.getDefault(), Integer.toString(sedationTime)));
        ageCheckBox.setChecked(ageOver5);
        sameMDCheckBox.setChecked(sameMD);

    }

    @Override
    public void onClick(View v) {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        final int id = v.getId();
        if (id == R.id.calculate_sedation_button) {
            calculateSedationTime();
        }
        else if (id == R.id.no_sedation_button) {
            noSedation(returnIntent);
        }
        else if (id == R.id.add_sedation_button) {
            addCodes(returnIntent);
        }
        else if (id == R.id.cancel_button) {
            finish();
        }
        else {
            finish();
        }
    }

    private void calculateSedationTime() {
        Intent intent = new Intent(this, SedationTimeCalculator.class);
        startActivityForResult(intent, SEDATION_CALCULATOR_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SEDATION_CALCULATOR_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                int result = data.getIntExtra(SEDATION_TIME, 0);
                //noinspection RedundantStringFormatCall
                timeEditText.setText(String.format(Locale.getDefault(), Integer.toString(result)));

            }
        }
    }

    // assumes data has been extracted from fields already
    // also assumes not called if sedationStatus == None
    private SedationStatus determineSedationStatus() {
        return SedationStatus.determineSedationStatus(sedationTime, sameMD);
    }

    private void noSedation(Intent intent) {
        sedationStatus = SedationStatus.None;
        sedationTime = 0;
        showResults(intent);
        putSedationData(intent);
    }

    private void addCodes(Intent intent) {
        sedationTime = getSedationTime();
        sameMD = sameMDCheckBox.isChecked();
        ageOver5 = ageCheckBox.isChecked();
        sedationStatus = determineSedationStatus();
        showResults(intent);
        putSedationData(intent);
    }

    private int getSedationTime() {
        try {
            sedationTime = Integer.parseInt(timeEditText.getText().toString());
        } catch (NumberFormatException e) {
            sedationTime = 0;
        }
        return sedationTime;
    }

    private void putSedationData(Intent intent) {
        intent.putExtra("SEDATION_STATUS", sedationStatus);
        intent.putExtra("TIME", sedationTime);
        intent.putExtra("AGE", ageOver5);
        intent.putExtra("SAME_MD", sameMD);
        setResult(Activity.RESULT_OK, intent);
    }

    private void showResults(Intent intent) {
        List<Code> sedationCodes = SedationCode.sedationCoding(sedationTime, sameMD,
                ageOver5, sedationStatus);
        AlertDialog dialog = new AlertDialog.Builder(this).create();
        String message;
        String title = "Sedation Coding";
        final Intent theIntent = intent;
        switch (sedationStatus) {
            case None:
                message = "No sedation used during this procedure.  No sedation codes added.";
                break;
            case LessThan10Mins:
                message = "Sedation time < 10 minutes.  No sedation codes can be added.";
                break;
            case OtherMDCalculated:
                message = String.format("Sedation codes will need to be reported by MD administering sedation" +
                        ", not by MD performing procedure.\n%s",
                        SedationCode.printSedationCodesWithDescription(sedationCodes));
                break;
            case AssignedSameMD:
                message = String.format("%s", SedationCode.printSedationCodesWithDescription(sedationCodes));
                break;
            default:
                message = "Error in sedation coding.";
                break;
        }

        dialog.setMessage(message);
        dialog.setTitle(title);
        dialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK", (dialogInterface, i) -> {
            putSedationData(theIntent);
            finish();
        });
        dialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Cancel", (dialogInterface, i) -> {
            // make finished false
        });
        dialog.show();
    }
}
