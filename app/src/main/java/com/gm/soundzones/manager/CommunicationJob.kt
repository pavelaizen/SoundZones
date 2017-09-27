package com.gm.soundzones.manager

import java.io.InputStream

interface CommunicationJob {
    val ipAddress:String
    val port:Int

    fun getInputStream():InputStream

}