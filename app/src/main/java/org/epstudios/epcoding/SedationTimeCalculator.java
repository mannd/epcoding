/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 3/5/17.
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
import android.widget.TimePicker;
import android.widget.ToggleButton;

import java.sql.Time;


public class SedationTimeCalculator extends BasicActionBarActivity
        implements View.OnClickListener {

    final static int MINS_IN_DAY = 60 * 24;

    private int time = 0;
    private int startHour;
    private int startMinute;
    private int endHour;
    private int endMinute;

    private ToggleButton toggleButton;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sedation_time_calculator);

        initToolbar();

        Button cancelButton = (Button)findViewById(R.id.cancel_button);
        Button calculateButton = (Button)findViewById(R.id.calculate_button);
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        timePicker = (TimePicker)findViewById(R.id.timePicker);

        cancelButton.setOnClickListener(this);
        calculateButton.setOnClickListener(this);
        toggleButton.setOnClickListener(this);

        startHour = timePicker.getCurrentHour();
        startMinute = timePicker.getCurrentMinute();
        endHour = startHour;
        endMinute = startMinute;

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            @Override
            public void onTimeChanged(TimePicker timePicker, int i, int i1) {
                if (toggleButton.isChecked()) {
                    startHour = i;
                    startMinute = i1;
                }
                else {
                    endHour = i;
                    endMinute = i1;
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.cancel_button:
                finish();
                break;
            case R.id.calculate_button:
                calculate();
                break;
            case R.id.toggleButton:
                assessTime();
                break;
        }

    }

    private void calculate() {
        int difference = minuteDifference();
        Log.d("EPCODING", "Difference = " + difference);

    }

    private int minuteDifference() {
        return minuteDifference(startHour, startMinute, endHour, endMinute);
    }

    // public static for tests
    // assumes start is always earlier than end.  If
    // end time seems earlier, adds 1 day to time.
    // Hopefully no procedure will last more than that!
    // Also, assumes valid input, no 25 o'clock please!
    public static int minuteDifference(int startHour, int startMinute,
                                int endHour, int endMinute) {
        int startMins = startHour * 60 + startMinute;
        int endMins = endHour * 60 + endMinute;
        int difference = endMins - startMins;
        if (difference < 0) {
            difference += MINS_IN_DAY;
        }
        return difference;
    }

    // TODO: time picker is not changing correctly with toggle button
    private void assessTime() {
        Log.d("EPCODING", "isChecked = " + toggleButton.isChecked());
       if (toggleButton.isChecked()) {
           timePicker.setCurrentHour(endHour);
           timePicker.setCurrentMinute(endMinute);
       }
       else {
           timePicker.setCurrentHour(startHour);
           timePicker.setCurrentMinute(startMinute);
       }
    }
}
