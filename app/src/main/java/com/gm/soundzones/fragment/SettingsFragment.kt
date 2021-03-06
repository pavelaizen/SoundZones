package com.gm.soundzones.fragment

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.*
import android.text.TextUtils
import android.widget.Toast
import com.gm.soundzones.KEY_USER_ID
import com.gm.soundzones.KEY_WAIT_DELAY
import com.gm.soundzones.R
import com.gm.soundzones.excel.DataProvider
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
        findPreference("delete_output").setOnPreferenceClickListener {
            val deleted = DataProvider.excelFile.delete()
            if (deleted){
                Toast.makeText(it.context, "OUTPUT file deleted successfully", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(it.context, "Failed to delete OUTPUT file", Toast.LENGTH_LONG).show()
            }
            true
        }
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
        if (pref is EditTextPreference) {
            val value = sharedPreferences.getString(key, null)
            pref.summary = value
        } else if (pref is SwitchPreference) {
            if (pref.isChecked) {
                pref.summary = "Remote Player"
            } else {
                pref.summary = "Tablet Player"
            }
        } else if (pref is ListPreference) {
            pref.summary = sharedPreferences.getString(key, null)
        }

    }

    private fun updatePrefSummary(p: Preference) {
        if (p is EditTextPreference) {
            if (!TextUtils.isEmpty(p.text)) {
                p.setSummary(p.text)
            }
        } else if (p is SwitchPreference) {
            if (p.isChecked) {
                p.summary = "Remote Player"
            } else {
                p.summary = "Tablet Player"
            }
        } else if (p is ListPreference) {
            when (p.key) {
                KEY_USER_ID -> {
                    val usersOrder = UserDataManager.usersOrder.copyOf()
                    val userIds = usersOrder.map { it.toString() }.toTypedArray()
                    p.entries = userIds
                    p.entryValues = userIds
                    p.summary = UserDataManager.userID.toString()
                }
                KEY_WAIT_DELAY -> {

                }
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