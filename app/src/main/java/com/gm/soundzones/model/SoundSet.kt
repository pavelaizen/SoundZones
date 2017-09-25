package com.gm.soundzones.model

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by Pavel Aizendorf on 17/09/2017.
 */
data class SoundSet(val pair: String, val primaryTrack: SoundTrack, val secondaryTrack: SoundTrack) : Parcelable {

    val hasNoise: Boolean by lazy {
        pair.substringAfterLast("_").toIntOrNull()?.takeIf { it==40 }?.run { true }?:false

    }

    var greatVolume: Int = 15
    var acceptableVolume: Int = 15

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readParcelable(SoundTrack::class.java.classLoader),
            parcel.readParcelable(SoundTrack::class.java.classLoader)) {
        greatVolume = parcel.readInt()
        acceptableVolume = parcel.readInt()
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(pair)
        parcel.writeParcelable(primaryTrack, flags)
        parcel.writeParcelable(secondaryTrack, flags)
        parcel.writeInt(greatVolume)
        parcel.writeInt(acceptableVolume)
    }

    override fun describeContents() = 0

    companion object CREATOR : Parcelable.Creator<SoundSet> {
        override fun createFromParcel(parcel: Parcel) = SoundSet(parcel)

        override fun newArray(size: Int): Array<SoundSet?> = arrayOfNulls(size)
    }
}