package org.epstudios.epcoding;

import android.content.SharedPreferences;
import android.os.Bundle;

import java.util.Objects;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;

/**
 * Copyright (C) 2013, 2014 EP Studios, Inc.
 * www.epstudiossoftware.com
 * <p>
 * Created by mannd on 11/8/14.
 * <p>
 * This file is part of EP Coding.
 * <p>
 * EP Coding is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * EP Coding is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * <p>
 * You should have received a copy of the GNU General Public License
 * along with EP Coding.  If not, see <http://www.gnu.org/licenses/>.
 * <p>
 * Note also:
 * <p>
 * CPT copyright 2012 American Medical Association. All rights
 * reserved. CPT is a registered trademark of the American Medical
 * Association.
 * <p>
 * A limited number of CPT codes are used in this program under the Fair Use
 * doctrine of US Copyright Law.  See README.md for more information.
 */


public class MyPreferenceFragment extends PreferenceFragmentCompat implements
        SharedPreferences.OnSharedPreferenceChangeListener {


    private String defaultVerbosity;
    private String verbosityKey;
    private String showDescriptionsKey;
    private String truncateDescriptionsKey;

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultVerbosity = requireActivity().getString(R.string.default_verbosity);
        verbosityKey = requireActivity().getString(R.string.code_verbosity_key);
        showDescriptionsKey = requireActivity().getString(
                R.string.show_details_code_summary_key);
        truncateDescriptionsKey = requireActivity().getString(
                R.string.truncate_long_descriptions_code_summary_key);
        Preference codeVerbosity = findPreference(verbosityKey);
        Objects.requireNonNull(codeVerbosity).setSummary(getPreferenceScreen().getSharedPreferences()
                .getString(verbosityKey, defaultVerbosity));
        Preference truncatePreference = findPreference(truncateDescriptionsKey);
        Objects.requireNonNull(truncatePreference).setEnabled(getPreferenceScreen()
                .getSharedPreferences().getBoolean(showDescriptionsKey, true));
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.settings, rootKey);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
                                          String key) {
        if (key.equals(verbosityKey)) {
            Preference codeVerbosity = findPreference(key);
            Objects.requireNonNull(codeVerbosity).setSummary(sharedPreferences.getString(key,
                    defaultVerbosity));
        }
        if (key.equals(showDescriptionsKey)) {
            boolean isEnabled = sharedPreferences.getBoolean(key, true);
            Preference truncateDescriptions = findPreference(truncateDescriptionsKey);
            Objects.requireNonNull(truncateDescriptions).setEnabled(isEnabled);
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