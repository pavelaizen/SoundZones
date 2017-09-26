package com.gm.soundzones.manager

import java.io.File

class RemoteMusicPlayer: AudioPlayer{
    companion object {
        private const val IP_ADDRESS ="1.2.3.4"
        private const val TRACK1_PORT=1234
        private const val TRACK2_PORT=1234
        private const val NOISE_PORT=1234
        private const val VOLUME_PORT=1234
    }
    suspend override fun playTrack1(audioFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, TRACK1_PORT,File(audioFile))).start()

    suspend override fun playTrack2(audioFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, TRACK2_PORT,File(audioFile))).start()

    suspend override fun playNoise(noiseFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, NOISE_PORT,File(noiseFile))).start()

    suspend override fun setVolume(volume: Int)=CommunicationWorker(StringJob(IP_ADDRESS, VOLUME_PORT,"Volume:$volume")).start()

    override fun stop() {
        //todo need API to stop
    }

}