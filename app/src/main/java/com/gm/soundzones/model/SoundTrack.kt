package com.gm.soundzones.model

import android.os.Environment
import android.os.Parcel
import android.os.Parcelable
import com.gm.soundzones.APP_DIR
import com.gm.soundzones.SOUND_ZONES_DIR
import java.io.File

/**
 * Created by Pavel Aizendorf on 17/09/2017.
 */
data class SoundTrack(private val track: String) : Parcelable {

    val dirName: String
    val songName: String

    init {
        val split = track.split("-")
        songName = split[0]
        dirName = split[1]
    }

    val fullPath: String
        get() {
            val trackDir = File(APP_DIR, dirName)
            return File(trackDir, "$songName - $dirName.wav").absolutePath
        }

    constructor(parcel: Parcel) : this(parcel.readString()) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(track)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<SoundTrack> {
        override fun createFromParcel(parcel: Parcel): SoundTrack = SoundTrack(parcel)

        override fun newArray(size: Int): Array<SoundTrack?> = arrayOfNulls(size)
    }
}