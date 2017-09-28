package com.gm.soundzones.model

/**
 * Created by Pavel Aizendorf on 17/09/2017.
 */
data class SoundSet(val pair: String, val primaryTrack: SoundTrack, val secondaryTrack: SoundTrack) {

    val hasNoise: Boolean by lazy {
        pair.substringAfterLast("_").toIntOrNull()?.takeIf { it == 40 }?.run { true } ?: false

    }

    var greatVolume: Int = -99
    var acceptableVolume: Int = -99


}