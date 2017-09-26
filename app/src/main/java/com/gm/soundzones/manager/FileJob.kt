package com.gm.soundzones.manager

import java.io.File
import java.io.InputStream

class FileJob(override val ipAddress: String, override val port: Int,val file:File) :CommunicationJob{

    override fun getInputStream(): InputStream {
        return file.inputStream()
    }

    override fun toString(): String {
        return "FileJob file name: ${file.name} to IP $ipAddress port $port"
    }
}