package com.gm.soundzones.manager

import android.media.MediaPlayer
import com.gm.soundzones.log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
class LocalMusicPlayer(baselineVolume: Int) : AudioPlayer(baselineVolume) {
    suspend override fun setVolumeMaster(volume: Int): Result {
        val adaptedVolume = volume / 100.toFloat()
        mp1?.setVolume(adaptedVolume, adaptedVolume).also {
            log("adaptedVolume $adaptedVolume")
        }
        return Result.SUCCESS
    }

    suspend override fun setVolumeSecondary(volume: Int): Result {
        val adaptedVolume = volume / 100.toFloat()
        mp2?.setVolume(adaptedVolume, adaptedVolume).also {
            log("adaptedVolume $adaptedVolume")
        }
        return Result.SUCCESS
    }

    private var mp1: MediaPlayer? = null
    private var mp2: MediaPlayer? = null
    private var mpNoise: MediaPlayer? = null
    private val primaryVolume:Float = getMasterBaselineVolume()/100f
    private val secondaryVolume:Float = getSlaveBaselineVolume(getMasterBaselineVolume())/100f
    init {
        log("primary volume ${getMasterBaselineVolume()}")
        log("secondary volume ${getSlaveBaselineVolume(getMasterBaselineVolume())}")
    }
    override suspend fun playTrack1(audioFile: String) = MediaPlayer().let {
        mp1 = it
        initAndPlay(it, audioFile, primaryVolume)
    }


    override suspend fun playTrack2(audioFile: String) = MediaPlayer().let {
        mp2 = it
        initAndPlay(it, audioFile, secondaryVolume)
    }


    override suspend fun playNoise(noiseFile: String) = MediaPlayer().let {
        mpNoise = it
        initAndPlay(it, noiseFile, 1f)
    }




    override fun stop(){
        releasePlayer(mp1).also { mp1 = null }
        releasePlayer(mp2).also { mp2 = null }
        releasePlayer(mpNoise).also { mpNoise = null }
    }

    private fun releasePlayer(player: MediaPlayer?) {
        player?.let {
            if (it.isPlaying) {
                it.stop()
            }
            it.release()
        }
    }

    private suspend fun initAndPlay(player: MediaPlayer, filePath: String, volume:Float) =
            async(CommonPool) {
                log("playing ${filePath} with volume ${volume*100}")
                player.setDataSource(filePath)
                player.setVolume(volume, volume)
                player.setOnPreparedListener {
                    player.start()
                }
                player.isLooping = true
                player.prepare()
                Result.SUCCESS
            }.await()

}



