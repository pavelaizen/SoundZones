package com.gm.soundzones.manager

import com.gm.soundzones.model.User
import java.io.File

class RemoteMusicPlayer(baselineVolume: Int) : AudioPlayer(baselineVolume) {
    val IP_ADDRESS:String = UserDataManager.getIpAddress()

    suspend override fun setVolumeMaster(volume: Int): Result =
            CommunicationWorker(StringJob(IP_ADDRESS, UserDataManager.getVolumePort().toInt(),"m=$volume")).start()

    suspend override fun setVolumeSecondary(volume: Int): Result =
            CommunicationWorker(StringJob(IP_ADDRESS, UserDataManager.getVolumePort().toInt(),"s=$volume")).start()


    suspend override fun playTrack1(audioFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, UserDataManager.getMasterPort().toInt(),File(audioFile))).start()

    suspend override fun playTrack2(audioFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, UserDataManager.getSecondaryPort().toInt(),File(audioFile))).start()

    suspend override fun playNoise(noiseFile: String)=CommunicationWorker(FileJob(IP_ADDRESS, UserDataManager.getNoisePort().toInt(),File(noiseFile))).start()

    override fun stop() {
        //todo need API to stop
    }

}