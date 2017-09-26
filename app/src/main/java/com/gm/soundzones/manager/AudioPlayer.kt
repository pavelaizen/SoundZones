package com.gm.soundzones.manager

import java.io.File

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */

interface AudioPlayer {
    suspend fun playTrack1(audioFile: String):Result
    suspend fun playTrack2(audioFile: String):Result
    suspend fun playNoise(noiseFile: String):Result
    suspend fun setVolume(volume: Int):Result
    fun stop()
}
