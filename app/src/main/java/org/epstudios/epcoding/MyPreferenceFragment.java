package org.epstudios.epcoding;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 Copyright (C) 2013, 2014 EP Studios, Inc.
www.epstudiossoftware.com

Created by mannd on 11/8/14.

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

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;


public class MyPreferenceFragment extends PreferenceFragment implements
        OnSharedPreferenceChangeListener {


private String defaultVerbosity;
private String verbosityKey;
private String showDescriptionsKey;
private String truncateDescriptionsKey;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultVerbosity = getActivity().getString(R.string.default_verbosity);
        verbosityKey = getActivity().getString(R.string.code_verbosity_key);
        showDescriptionsKey = getActivity().getString(
                R.string.show_details_code_summary_key);
        truncateDescriptionsKey = getActivity().getString(
                R.string.truncate_long_descriptions_code_summary_key);
        addPreferencesFromResource(R.xml.settings);
        Preference codeVerbosity = findPreference(verbosityKey);
        codeVerbosity.setSummary(getPreferenceScreen().getSharedPreferences()
                .getString(verbosityKey, defaultVerbosity));
        Preference truncatePreference = findPreference(truncateDescriptionsKey);
        truncatePreference.setEnabled(getPreferenceScreen()
                .getSharedPreferences().getBoolean(showDescriptionsKey, true));
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(verbosityKey)) {
            Preference codeVerbosity = findPreference(key);
            codeVerbosity.setSummary(sharedPreferences.getString(key,
                    defaultVerbosity));
        }
        if (key.equals(showDescriptionsKey)) {
            boolean isEnabled = sharedPreferences.getBoolean(key, true);
            Preference truncateDescriptions = findPreference(truncateDescriptionsKey);
            truncateDescriptions.setEnabled(isEnabled);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences()
                .registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences()
                .unregisterOnSharedPreferenceChangeListener(this);
    }

}