package com.gm.soundzones

import android.app.Application
import android.util.Log
import com.gm.soundzones.manager.UserDataManager

/**
 * Created by Pavel Aizendorf on 26/09/2017.
 */
class SoundZonesApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, error ->
            logFile.appendText("UncaughtException in thread ${thread.name}: ${Log.getStackTraceString(error)}")
            defaultUncaughtExceptionHandler.uncaughtException(thread, error)
        }
        UserDataManager.setup(this)
    }
}