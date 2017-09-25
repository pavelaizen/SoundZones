package com.gm.soundzones.manager

import java.io.File

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */

interface AudioPlayer {
    fun playTrack1(audioFile: String)
    fun playTrack2(audioFile: String)
    fun playNoise(noiseFile: String)
    fun setVolume(volume: Int)
    fun stop()
}
