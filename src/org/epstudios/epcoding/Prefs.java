package org.epstudios.epcoding;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;

public class Prefs extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {
	private static int prefs = R.xml.settings;

	@Override
	protected void onCreate(final Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		try {
			getClass().getMethod("getFragmentManager");
			AddResourceApi11AndGreater();
		} catch (NoSuchMethodException e) { // Api < 11
			AddResourceApiLessThan11();
		}
	}

	@SuppressWarnings("deprecation")
	protected void AddResourceApiLessThan11() {
		addPreferencesFromResource(prefs);
	}

	@TargetApi(11)
	protected void AddResourceApi11AndGreater() {
		getFragmentManager().beginTransaction()
				.replace(android.R.id.content, new PF()).commit();
	}

	@TargetApi(11)
	public static class PF extends PreferenceFragment {
		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(Prefs.prefs); // outer class private
														// members seem to be
														// visible for inner
														// class, and making it
														// static made things so
														// much easier
		}
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		if (key == "code_verbosity") {
			@SuppressWarnings("deprecation")
			Preference codeVerbosity = findPreference(key);
			codeVerbosity.setSummary(sharedPreferences
					.getString(key, "VERBOSE"));
		}

	}

}
