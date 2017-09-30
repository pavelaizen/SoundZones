package com.gm.soundzones.activity

import android.app.Activity
import android.os.Bundle
import com.gm.soundzones.fragment.SettingsFragment



/**
 * Created by titan on 30-Sep-17.
 */

class SettingsActivity : Activity(){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null){
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, SettingsFragment())
                    .commit()
        }
    }
}
