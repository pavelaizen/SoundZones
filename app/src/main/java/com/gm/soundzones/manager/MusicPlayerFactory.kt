package com.gm.soundzones.manager

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
object MusicPlayerFactory {
    const val LOCAL_PLAYER = true
    val musicPlayer: AudioPlayer
        get() = if (LOCAL_PLAYER) LocalMusicPlayer() else TODO()
}