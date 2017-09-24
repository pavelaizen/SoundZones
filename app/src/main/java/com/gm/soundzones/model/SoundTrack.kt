package com.gm.soundzones.model

import android.os.Environment
import java.io.File

/**
 * Created by Pavel Aizendorf on 17/09/2017.
 */
data class SoundTrack(private val track: String) {
    private val SOUND_ZONES_DIR = "SoundZones"

    val dirName: String
    val songName: String

    init {
        val split = track.split("-")
        songName = split[0]
        dirName = split[1]
    }

    val fullPath: String
        get() {
            val appDir = Environment.getExternalStorageDirectory().absolutePath + File.separator + SOUND_ZONES_DIR
            val trackDir = File(appDir, dirName)
            return File(trackDir, "$songName - $dirName.wav").absolutePath
        }
}