package com.earlyense.soundzones.manager

import android.media.AudioManager
import android.media.MediaPlayer
import android.os.Handler
import android.os.Looper
import com.earlyense.soundzones.model.SoundSet

/**
 * Created by Pavel Aizendorf on 18/09/2017.
 */
class SongManager(private val audioManager: AudioManager) {
    var players = arrayOfNulls<MediaPlayer>(3)


    fun playMediaPlayed(soundSet: SoundSet) {
        val firstFile = soundSet.primaryTrack.fullPath
        val lastFile = soundSet.secondaryTrack.fullPath
        players[0] = playFile(firstFile)
        Handler(Looper.getMainLooper()).postDelayed({
            players[1] = playFile(lastFile)
        }, 15000)
    }

    private fun playFile(filePath: String): MediaPlayer {
        val player = MediaPlayer()
        player.setDataSource(filePath)
        player.setOnPreparedListener {
            player.start()
        }
        player.setOnCompletionListener {
            player.release()
        }
        player.prepareAsync()
        return player
    }

    fun stop() {
        for (player in players) {
            if (player != null) {
                if (player.isPlaying) {
                    player.stop()
                }
                player.release()
            }
        }
        players = arrayOfNulls<MediaPlayer>(3)
    }


}