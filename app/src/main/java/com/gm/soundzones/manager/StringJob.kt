package com.gm.soundzones.manager

import java.io.InputStream

class StringJob(override val ipAddress: String, override val port: Int,val message:String) :CommunicationJob{
    override fun getInputStream(): InputStream {
        return message.byteInputStream()
    }

    override fun toString(): String {
        return "StringJob string: $message to IP $ipAddress port $port"
    }
}