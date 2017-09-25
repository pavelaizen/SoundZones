package com.gm.soundzones.manager

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
class MusicPlayerFactory {
    companion object {
        val LOCAL_PLAYER = true
        val musicPlayer:AudioPlayer
        get() {
            if (LOCAL_PLAYER){
                return LocalMusicPlayer()
            }else{
                TODO()
            }
        }

    }
}