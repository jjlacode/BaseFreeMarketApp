package com.jjlacode.freelanceproject.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.jjlacode.freelanceproject.R;

public class Settings extends PreferenceFragment {

    public Settings() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
