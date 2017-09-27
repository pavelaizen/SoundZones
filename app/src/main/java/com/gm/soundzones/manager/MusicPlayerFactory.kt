package com.gm.soundzones.manager

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
object MusicPlayerFactory {
    private const val LOCAL_PLAYER = true
    fun getMusicPlayer(baselineVolume:Int) = if (LOCAL_PLAYER) LocalMusicPlayer(baselineVolume) else TODO()
}