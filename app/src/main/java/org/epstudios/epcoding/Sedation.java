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

public class Sedation extends BasicActionBarActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sedation);

        initToolbar();

        Button calculateTimeButton = (Button)findViewById(R.id.calculate_sedation_button);
        Button cancelButton = (Button)findViewById(R.id.cancel_button);
        Button noSedationButton = (Button)findViewById(R.id.no_sedation_button);
        Button addButton = (Button)findViewById(R.id.add_sedation_button);
        calculateTimeButton.setOnClickListener(this);
        cancelButton.setOnClickListener(this);
        noSedationButton.setOnClickListener(this);
        addButton.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.calculate_sedation_button:
                calculateSedationTime();
                break;
            case R.id.cancel_button:
                //cancel();
                break;
            case R.id.no_sedation_button:
                //noSedation();
                break;
            case R.id.add_button:
                //addSedation();
                break;
            default:
                //cancel();
                break;
        }
    }

    private void calculateSedationTime() {
        Intent intent = new Intent(this, SedationTimeCalculator.class);
        startActivity(intent);
    }
}
