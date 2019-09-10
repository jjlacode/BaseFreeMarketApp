package com.codevsolution.freemarketsapp.settings;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import com.codevsolution.freemarketsapp.R;

public class Settings extends PreferenceFragment {

    public Settings() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.settings);
    }
}
