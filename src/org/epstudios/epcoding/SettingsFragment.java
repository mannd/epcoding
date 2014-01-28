package org.epstudios.epcoding;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;

public class SettingsFragment extends PreferenceFragment implements
		OnSharedPreferenceChangeListener {
	private String defaultVerbosity;
	private String verbosityKey;
	private String showDescriptionsKey;
	private String truncateDescriptionsKey;

	public SettingsFragment() {
		// empty constructor
	}

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
