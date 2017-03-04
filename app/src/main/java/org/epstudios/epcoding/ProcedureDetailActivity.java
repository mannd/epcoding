/*  
Copyright (C) 2013, 2014 EP Studios, Inc.
www.epstudiossoftware.com

This file is part of EP Coding.

    EP Coding is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    EP Coding is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with EP Coding.  If not, see <http://www.gnu.org/licenses/>.

    Note also:

    CPT copyright 2012 American Medical Association. All rights
    reserved. CPT is a registered trademark of the American Medical
    Association.

    A limited number of CPT codes are used in this program under the Fair Use
    doctrine of US Copyright Law.  See README.md for more information.
 */

package org.epstudios.epcoding;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

/**
 * An activity representing a single Procedure detail screen. This activity is
 * only used on handset devices. On tablet-size devices, item details are
 * presented side-by-side with a list of items in a
 * {@link ProcedureListActivity}.
 * <p>
 * This activity is mostly just a 'shell' activity containing nothing more than
 * a {@link ProcedureDetailFragment}.
 */
public class ProcedureDetailActivity extends ActionBarActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_procedure_detail);
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// savedInstanceState is non-null when there is fragment state
		// saved from previous configurations of this activity
		// (e.g. when rotating the screen from portrait to landscape).
		// In this case, the fragment will automatically be re-added
		// to its container so we don't need to manually add it.
		// For more information, see the Fragments API guide at:
		//
		// http://developer.android.com/guide/components/fragments.html
		//
		if (savedInstanceState == null) {
			// Create the detail fragment and add it to the activity
			// using a fragment transaction.
			Bundle arguments = new Bundle();
			arguments.putString(
					ProcedureDetailFragment.ARG_ITEM_ID,
					getIntent().getStringExtra(
							ProcedureDetailFragment.ARG_ITEM_ID));
			ProcedureDetailFragment fragment = new ProcedureDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.add(R.id.procedure_detail_container, fragment).commit();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		ProcedureDetailFragment fragment = (ProcedureDetailFragment) getSupportFragmentManager()
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
		case R.id.settings:
			startActivity(new Intent(this, Prefs.class));
			return true;
		case R.id.wizard:
			startActivity(new Intent(this, ScreenSlideActivity.class));
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
