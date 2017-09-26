package com.gm.soundzones.manager

import com.gm.soundzones.log
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.async
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.net.Socket
import java.net.UnknownHostException

class CommunicationWorker(val job:CommunicationJob){
    companion object {
        private const val BUFFER_SIZE=4096
    }

    suspend fun start():Result{
        val result = async(CommonPool) {
            val startTime=System.currentTimeMillis()
            log("worker start job $job on thread ${Thread.currentThread().name}")
            var clientSocket:Socket?=null
            var outputStream:OutputStream?=null
            var inputStream:InputStream?=null
            try {
                clientSocket=Socket(job.ipAddress, job.port)
                outputStream = clientSocket.getOutputStream()
                inputStream = job.getInputStream()
                val buffer= ByteArray(BUFFER_SIZE)
                while(true){
                    inputStream.read(buffer).takeIf { it>=0 }?.also {
                        outputStream?.write(buffer,0,it)
                    }?:break
                }
                Result.SUCCESS
            }catch (e: UnknownHostException) {
                log("Worker Unknown Host IP ${job.ipAddress} port ${job.port}",e)
                Result.UNKNOWN_HOST
            }catch (e:IOException){
                log("Worker IO Exception",e)
                Result.IO_ERROR
            }finally {
                inputStream?.close()
                outputStream?.close()
                clientSocket?.close()
                log("worker finish job $job time = ${System.currentTimeMillis()-startTime}ms")
            }
        }
        return result.await();

    }

}