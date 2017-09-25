package com.gm.soundzones.manager

import android.media.MediaPlayer
import com.gm.soundzones.log

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
class LocalMusicPlayer : AudioPlayer {
    private var mp1: MediaPlayer? = null
    private var mp2: MediaPlayer? = null
    private var mpNoise: MediaPlayer? = null


    override fun playTrack1(audioFile: String) {
        mp1 = MediaPlayer().also {
            initAndPlay(it, audioFile)
        }
    }

    override fun playTrack2(audioFile: String) {
        mp2 = MediaPlayer().also {
            initAndPlay(it, audioFile)
        }
    }

    override fun playNoise(noiseFile: String) {
        mpNoise = MediaPlayer().also {
            initAndPlay(it, noiseFile)
        }
    }

    override fun setVolume(volume: Int) {
        val adaptedVolume = volume / 100.toFloat()
        mp2?.setVolume(adaptedVolume, adaptedVolume).also {
            log("adaptedVolume $adaptedVolume")
        }
    }

    override fun stop() {
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

    private fun initAndPlay(player: MediaPlayer, filePath: String) {
        player.setDataSource(filePath)
        player.setVolume(0.5f, 0.5f)
        player.setOnPreparedListener {
            player.start()
        }
        player.isLooping = true
        player.prepareAsync()
    }
}



