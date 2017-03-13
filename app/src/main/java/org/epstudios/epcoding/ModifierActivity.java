/**
 * Copyright (C) 2017 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 3/2/17.
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
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.epstudios.epcoding.Constants.EPCODING;

public class ModifierActivity extends BasicActionBarActivity implements View.OnClickListener {

    private boolean saveAddedModifiersToPrefs;

    private Code code;
    private Set<Modifier> modifierSet;
    private CheckBox[] checkBoxes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifiers);

        String codeNumber = getIntent().getStringExtra(Constants.ACTIVE_CODE_NUMBER);
        setTitle("Code " + codeNumber);

        saveAddedModifiersToPrefs = getIntent().getBooleanExtra(Constants.SAVE_TEMP_ADDED_MODIFIERS,
                false);

        LinearLayout checkBoxLayout = (LinearLayout) findViewById(
                R.id.modifiers_checkbox_layout);


        code = Codes.getCode(codeNumber);
        modifierSet = code.getModifierSet();

        List<Modifier> allModifiers = Modifiers.allModifiersSorted();
        checkBoxes = new CheckBox[allModifiers.size()];
        createCheckBoxLayoutAndModifierMap(allModifiers, checkBoxLayout);

        if (savedInstanceState != null) {
            boolean[] checkBoxState = savedInstanceState.getBooleanArray("ModifierState");
            int i = 0;
            for (CheckBox checkBox : checkBoxes) {
                checkBox.setChecked(checkBoxState != null ? checkBoxState[i++] : false);
            }
        }
        initToolbar();

        Button cancelButton = (Button)findViewById(R.id.cancel_button);
        cancelButton.setOnClickListener(this);
        Button resetButton = (Button)findViewById(R.id.reset_button);
        resetButton.setOnClickListener(this);
        Button saveButton = (Button)findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        Button addButton = (Button)findViewById(R.id.add_button);
        addButton.setOnClickListener(this);

        Log.d(EPCODING, "onCreate");
    }

    @Override
    public void onClick(View v) {
        Log.d(EPCODING, "click view");
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        switch (v.getId()) {
            case R.id.cancel_button:
                // Just go back
                break;
            case R.id.reset_button:
                resetModifiers(returnIntent);
                break;
            case R.id.save_button:
                saveModifiers();
                // fall-through -- need to add them too so they appear
            case R.id.add_button:
                addModifiers(returnIntent);
                break;
        }
        finish();
    }

    private void createCheckBoxLayoutAndModifierMap(List<Modifier> modifiers,
                                                    LinearLayout layout) {

        int i = 0;
        for (Modifier modifier : modifiers) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(modifier.modifierDescription());
            // note that tags are preserved with rotation automatically
            checkBox.setTag(modifier.getNumber());
            checkBox.setChecked(modifierSet.contains(modifier));
            checkBoxes[i++] = checkBox;
            layout.addView(checkBox);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        boolean[] checkBoxState = new boolean[checkBoxes.length];
        for (int i = 0; i < checkBoxes.length; i++) {
            checkBoxState[i] = checkBoxes[i].isChecked();
        }
        savedInstanceState.putBooleanArray("ModifierState", checkBoxState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void saveModifiers() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Set<String> selectedNumbers = selectedModifierNumbers();
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putStringSet(code.getCodeNumber(), selectedNumbers);
        prefsEditor.apply();

    }

    private void resetModifiers(Intent intent) {
        // send back special StringSet and have caller reset code
        String [] resetArray = new String[1];
        resetArray[0] = Constants.RESET_MODIFIERS;
        intent.putExtra(Constants.MODIFIER_RESULT, resetArray);
        setResult(Activity.RESULT_OK, intent);

    }

    private Set<String> selectedModifierNumbers() {
        Set<String> numbers = new HashSet<>();
        for (CheckBox checkBox : checkBoxes) {
            if (checkBox.isChecked()) {
                numbers.add((String) checkBox.getTag());
            }
        }
        return numbers;
    }

    private void addModifiers(Intent intent) {
        Set<String> modifierNumbers = selectedModifierNumbers();
        intent.putExtra(Constants.MODIFIER_RESULT,
                Code.makeCodeAndModifierArray(code.getCodeNumber(), modifierNumbers));
        setResult(Activity.RESULT_OK, intent);
        if (saveAddedModifiersToPrefs) {
            saveAddedModifiersToPrefs(modifierNumbers);
        }
    }

    private void saveAddedModifiersToPrefs(Set<String> modifierNumbers) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putStringSet("TEMP" + code.getCodeNumber(), modifierNumbers);
        prefsEditor.apply();
    }

}
