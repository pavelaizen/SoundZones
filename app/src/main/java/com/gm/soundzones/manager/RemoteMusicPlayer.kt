package com.gm.soundzones.manager

import java.io.File

class RemoteMusicPlayer(baselineVolume: Int) : AudioPlayer(baselineVolume) {
    val IP_ADDRESS:String = UserDataManager.getIpAddress()
    private var track1CommWorker:CommunicationWorker?=null
    private var track2CommWorker:CommunicationWorker?=null
    private var noiseCommWorker:CommunicationWorker?=null

    suspend override fun setVolumeMaster(volume: Int) =
            CommunicationWorker(StringJob(IP_ADDRESS, UserDataManager.getVolumePort().toInt(),"m=$volume")).start().await()

    suspend override fun setVolumeSecondary(volume: Int) =
            CommunicationWorker(StringJob(IP_ADDRESS, UserDataManager.getVolumePort().toInt(),"s=$volume")).start().await()


    suspend override fun playTrack1(audioFile: String):Result{
        track1CommWorker?.waitTillComplete()
        return CommunicationWorker(FileJob(IP_ADDRESS, UserDataManager.getMasterPort().toInt(), File(audioFile))).also { track1CommWorker=it }.start().await()
    }

    suspend override fun playTrack2(audioFile: String):Result{
        track2CommWorker?.waitTillComplete()
        return CommunicationWorker(FileJob(IP_ADDRESS, UserDataManager.getSecondaryPort().toInt(),File(audioFile))).also { track2CommWorker=it }.start().await()
    }

    suspend override fun playNoise(noiseFile: String):Result{
        noiseCommWorker?.waitTillComplete()
        return CommunicationWorker(FileJob(IP_ADDRESS, UserDataManager.getNoisePort().toInt(),File(noiseFile))).also { noiseCommWorker=it }.start().await()
    }

    private suspend fun CommunicationWorker.waitTillComplete(){
        this.defer?.takeIf { !it.isCompleted }?.await()
    }
    override fun stop() {
        track1CommWorker?.stop()
        track2CommWorker?.stop()
        noiseCommWorker?.stop()
    }
}