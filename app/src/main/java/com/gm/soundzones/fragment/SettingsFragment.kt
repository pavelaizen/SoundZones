package com.gm.soundzones.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.EditTextPreference
import android.preference.Preference
import android.preference.PreferenceFragment
import android.preference.PreferenceGroup
import android.text.TextUtils
import com.gm.soundzones.R
import com.gm.soundzones.manager.UserDataManager


/**
 * Created by titan on 30-Sep-17.
 */
class SettingsFragment : PreferenceFragment(), SharedPreferences.OnSharedPreferenceChangeListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preferenceManager.sharedPreferencesName = UserDataManager.SP_NAME
        addPreferencesFromResource(R.xml.prefs)
        initSummary(preferenceScreen)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences
                .unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences
                .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String) {
        val pref = findPreference(key)
        val value = sharedPreferences.getString(key, null)
        pref.summary = value

    }

    private fun updatePrefSummary(p: Preference) {
        if (p is EditTextPreference) {
            if (!TextUtils.isEmpty(p.text)){
                p.setSummary(p.text)
            }
        }
    }

    private fun initSummary(p: Preference) {
        if (p is PreferenceGroup) {
            for (i in 0 until p.preferenceCount) {
                initSummary(p.getPreference(i))
            }
        } else {
            updatePrefSummary(p)
        }
    }
}