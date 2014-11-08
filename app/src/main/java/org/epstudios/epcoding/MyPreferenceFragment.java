package org.epstudios.epcoding;

import android.os.Bundle;
import android.preference.PreferenceFragment;

/**
 * Created by mannd on 11/8/14.
 */
public class MyPreferenceFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
