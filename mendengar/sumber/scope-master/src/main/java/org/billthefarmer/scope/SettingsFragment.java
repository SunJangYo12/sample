////////////////////////////////////////////////////////////////////////////////
//
//  Scope - An Android scope written in Java.
//
//  Copyright (C) 2014	Bill Farmer
//
//  This program is free software; you can redistribute it and/or modify
//  it under the terms of the GNU General Public License as published by
//  the Free Software Foundation; either version 3 of the License, or
//  (at your option) any later version.
//
//  This program is distributed in the hope that it will be useful,
//  but WITHOUT ANY WARRANTY; without even the implied warranty of
//  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
//  GNU General Public License for more details.
//
//  You should have received a copy of the GNU General Public License
//  along with this program.  If not, see <http://www.gnu.org/licenses/>.
//
//  Bill Farmer	 william j farmer [at] yahoo [dot] co [dot] uk.
//
///////////////////////////////////////////////////////////////////////////////

package org.billthefarmer.scope;

import android.app.ActionBar;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

// SettingsFragment
public class SettingsFragment extends PreferenceFragment
    implements SharedPreferences.OnSharedPreferenceChangeListener
{
    private static final String KEY_PREF_INPUT = "pref_input";
    private static final String KEY_PREF_ABOUT = "pref_about";

    // onCreate
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.preferences);

        SharedPreferences preferences =
            PreferenceManager.getDefaultSharedPreferences(getActivity());

        preferences.registerOnSharedPreferenceChangeListener(this);

        ListPreference preference =
            (ListPreference)findPreference(KEY_PREF_INPUT);
        preference.setSummary(preference.getEntry());

        // Get about summary
        Preference about = findPreference(KEY_PREF_ABOUT);

      
    }

    // On preference tree click
    @Override
    public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                                         Preference preference)
    {
        boolean result =
            super.onPreferenceTreeClick(preferenceScreen, preference);

        if (preference instanceof PreferenceScreen)
        {
            Dialog dialog = ((PreferenceScreen)preference).getDialog();
            ActionBar actionBar = dialog.getActionBar();
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        return result;
    }

    // On shared preference changed
    @Override
    public void onSharedPreferenceChanged(SharedPreferences preferences,
                                          String key)
    {
        if (key.equals(KEY_PREF_INPUT))
        {
            Preference preference = findPreference(key);

            // Set summary to be the user-description for the selected
            // value
            preference.setSummary(((ListPreference) preference).getEntry());
        }
    }
}
