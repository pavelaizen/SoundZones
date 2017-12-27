package com.gm.soundzones.manager

import com.gm.soundzones.SECONDARY_VOLUME_DIFF_PERCENT


/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */

abstract class AudioPlayer(val baselineVolume: Int) {
    abstract suspend fun playTrack1(audioFile: String): Result
    abstract suspend fun playTrack2(audioFile: String): Result
    abstract suspend fun playNoise(noiseFile: String): Result
    abstract suspend fun setVolumeMaster(volume: Int): Result
    abstract suspend fun setVolumeSecondary(volume: Int): Result
    abstract fun stop()

    fun getMasterBaselineVolume() = baselineVolume

    companion object {
        fun getSlaveBaselineVolume(baselineVolume: Int) = baselineVolume * (100 + SECONDARY_VOLUME_DIFF_PERCENT) / 100
    }
}
