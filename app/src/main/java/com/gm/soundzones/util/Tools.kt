package com.gm.soundzones.util

import android.os.Environment
import com.gm.soundzones.model.SoundTrack
import java.io.File

/**
 * Created by Pavel Aizendorf on 18/09/2017.
 */
object Tools {
    private val SOUND_ZONES_DIR = "SoundZones"

    fun getFileFromTrack(soundTrack: SoundTrack): File {
        val dirName = soundTrack.dirName
        val songName = soundTrack.songName
        val appDir = Environment.getExternalStorageDirectory().absolutePath + File.separator + SOUND_ZONES_DIR
        val trackDir = File(appDir, dirName)
        return File(trackDir, "$songName - $dirName.wav")
    }
}