/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 3/4/17.
 * <p>
 * This file is part of epcoding.
 * <p>
 * epcoding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * epcoding is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with epcoding.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.epstudios.epcoding;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Sedation extends BasicActionBarActivity implements View.OnClickListener {

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

        Button calculateTimeButton = (Button)findViewById(R.id.calculate_sedation_button);
        Button cancelButton = (Button)findViewById(R.id.cancel_button);
        Button noSedationButton = (Button)findViewById(R.id.no_sedation_button);
        Button addButton = (Button)findViewById(R.id.add_sedation_button);
        calculateTimeButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        noSedationButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

        timeEditText = (EditText)findViewById(R.id.sedation_time_edit_text);
        ageCheckBox = (CheckBox)findViewById(R.id.pt_over_5_checkbox);
        sameMDCheckBox = (CheckBox)findViewById(R.id.same_md_checkbox);

        timeEditText.setText(Integer.toString(sedationTime));
        ageCheckBox.setChecked(ageOver5);
        sameMDCheckBox.setChecked(sameMD);

    }

    @Override
    public void onClick(View v) {
        boolean finished = true;
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        switch (v.getId()) {
            case R.id.calculate_sedation_button:
                finished = false;
                calculateSedationTime();
                break;
            case R.id.cancel_button:
                break;
            case R.id.no_sedation_button:
                noSedation(returnIntent);
                break;
            case R.id.add_button:
                //addSedation();
                break;
            default:
                break;
        }
        if (finished) {
            finish();
        }
    }

    private void calculateSedationTime() {
        Intent intent = new Intent(this, SedationTimeCalculator.class);
        startActivity(intent);
    }

//    private SedationStatus determineSedationStatus() {
//
//    }

    private void noSedation(Intent intent) {
        sedationStatus = SedationStatus.None;
        intent.putExtra("SEDATION_STATUS", sedationStatus);
        setResult(Activity.RESULT_OK, intent);
        // showResults();
    }
}
