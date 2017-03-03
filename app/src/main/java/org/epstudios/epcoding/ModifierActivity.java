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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class ModifierActivity extends ActionBarActivity {

    private Code code;
    private List<Modifier> modifiers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.modifiers);

        String codeNumber = getIntent().getStringExtra("ACTIVE_CODE_NUMBER");
        setTitle("Code " + codeNumber);

        LinearLayout checkBoxLayout = (LinearLayout) findViewById(
                R.id.modifiers_checkbox_layout);


        code = Codes.getCode(codeNumber);
        modifiers = code.getModifiers();

        List<Modifier> allModifiers = Modifiers.allModifiersSorted();

        createCheckBoxLayoutAndModifierMap(allModifiers, checkBoxLayout);

        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.helpmenu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                // NavUtils.navigateUpFromSameTask(this);
                finish();
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    private void createCheckBoxLayoutAndModifierMap(List<Modifier> modifiers,
                                                    LinearLayout layout) {

        for (Modifier modifier : modifiers) {
            CheckBox checkBox = new CheckBox(this);
            checkBox.setText(modifier.modifierDescription());
            layout.addView(checkBox);
        }

//        for (int i = 0; i < codes.length; ++i) {
//            CodeCheckBox codeCheckBox = new CodeCheckBox(context);
//            // codes[i].setPlusShown(plusShownInDisplay);
//            codeCheckBox.setCodeFirst(mItem == allProcedures);
//            codeCheckBox.setCode(codes[i]);
//            codeCheckBoxMap.put(codes[i].getCodeNumber(), codeCheckBox);
//
//
//            layout.addView(codeCheckBox);
    }
}
