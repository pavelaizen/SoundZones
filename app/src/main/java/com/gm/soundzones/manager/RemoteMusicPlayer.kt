package com.gm.soundzones.manager

import java.io.File

class RemoteMusicPlayer(baselineVolume: Int) : AudioPlayer(baselineVolume) {
    companion object {
        private const val IP_ADDRESS ="1.2.3.4"
    }
    suspend override fun playTrack1(audioFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, Config.TRACK1_PORT,File(audioFile))).start()

    suspend override fun playTrack2(audioFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, Config.TRACK2_PORT,File(audioFile))).start()

    suspend override fun playNoise(noiseFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, Config.NOISE_PORT,File(noiseFile))).start()

    suspend override fun setVolume(volume: Int)=CommunicationWorker(StringJob(IP_ADDRESS, Config.CONTROL_PORT,"Volume:$volume")).start()

    override fun stop() {
        //todo need API to stop
    }

}