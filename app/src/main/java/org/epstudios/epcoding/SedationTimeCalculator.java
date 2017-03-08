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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.ToggleButton;

import static org.epstudios.epcoding.Constants.SEDATION_TIME;


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
        Button setTimeButton = (Button)findViewById(R.id.set_button);
        toggleButton = (ToggleButton)findViewById(R.id.toggleButton);
        timePicker = (TimePicker)findViewById(R.id.timePicker);

        timePicker.setIs24HourView(true);


        cancelButton.setOnClickListener(this);
        calculateButton.setOnClickListener(this);
        toggleButton.setOnClickListener(this);
        setTimeButton.setOnClickListener(this);

        startHour = timePicker.getCurrentHour();
        startMinute = timePicker.getCurrentMinute();
        endHour = startHour;
        endMinute = startMinute;

        updateToggleButtonTitle();

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
                break;
            case R.id.set_button:
                setTime();
                break;
        }

    }

    private void calculate() {
        int difference = minuteDifference();
        Intent intent = new Intent();
        intent.putExtra(SEDATION_TIME, difference);
        setResult(Activity.RESULT_OK, intent);
        finish();
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

    private String differenceLabel() {
        String diff = String.format("δ = %d min", minuteDifference());
        return diff;
    }

    private void assessTime() {
       if (toggleButton.isChecked()) {
           timePicker.setCurrentHour(endHour);
           timePicker.setCurrentMinute(endMinute);
       }
       else {
           timePicker.setCurrentHour(startHour);
           timePicker.setCurrentMinute(startMinute);
       }
    }

    private void setTime() {
        if (toggleButton.isChecked()) {
            endHour = timePicker.getCurrentHour();
            endMinute = timePicker.getCurrentMinute();
        }
        else {
            startHour = timePicker.getCurrentHour();
            startMinute = timePicker.getCurrentMinute();
        }
        updateToggleButtonTitle();
    }

    private void updateToggleButtonTitle() {
        toggleButton.setTextOn(String.format("End Time %d:%d %s", endHour, endMinute, differenceLabel()));
        toggleButton.setTextOff(String.format("Start Time %d:%d %s", startHour, startMinute, differenceLabel()));
        if (toggleButton.isChecked()) {
            toggleButton.setText(String.format("End Time %d:%d %s", endHour, endMinute, differenceLabel()));
        }
        else {
            toggleButton.setText(String.format("Start Time %d:%d %s", startHour, startMinute, differenceLabel()));
        }
    }
}
