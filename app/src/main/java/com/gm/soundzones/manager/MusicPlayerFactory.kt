package com.gm.soundzones.manager

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
object MusicPlayerFactory {
    fun getMusicPlayer(baselineVolume: Int) = if (UserDataManager.isRemotePlayer()) RemoteMusicPlayer(baselineVolume) else LocalMusicPlayer(baselineVolume)
}