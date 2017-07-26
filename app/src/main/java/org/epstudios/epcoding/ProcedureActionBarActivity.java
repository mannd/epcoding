package org.epstudios.epcoding;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

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

public class ProcedureActionBarActivity extends SimpleActionBarActivity {

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ProcedureDetailFragment fragment = (ProcedureDetailFragment) getFragmentManager()
                .findFragmentById(R.id.procedure_detail_container);
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            case R.id.help:
                startActivity(new Intent(this, Help.class));
                return true;
            case R.id.saveCodeSelection:
                if (fragment != null)
                    fragment.saveCoding();
                return true;
            case R.id.icd10:
                startActivity(new Intent(this, ICD10CodeList.class));
                return true;
            case R.id.settings:
                startActivity(new Intent(this, Prefs.class));
                return true;
            case R.id.wizard:
                startActivity(new Intent(this, ScreenSlideActivity.class));
                return true;
            case R.id.about:
                startActivity(new Intent(this, About.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.shortmenu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search)
                .getActionView();
        // Assumes current activity is the searchable activity
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget;
        // expand it by default

        return super.onCreateOptionsMenu(menu);
    }

}
