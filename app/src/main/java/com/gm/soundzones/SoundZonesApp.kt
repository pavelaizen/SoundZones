package com.gm.soundzones

import android.app.Application
import com.gm.soundzones.manager.UserDataManager

/**
 * Created by Pavel Aizendorf on 26/09/2017.
 */
class SoundZonesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        UserDataManager.setup(this)
    }
}