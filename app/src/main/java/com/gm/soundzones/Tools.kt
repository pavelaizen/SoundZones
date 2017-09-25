package com.gm.soundzones

import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log
import java.io.File
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    val fragmentTransaction = supportFragmentManager
            .beginTransaction()
            .replace(containerId, fragment, fragment.javaClass.simpleName);
    if (addToBackStack) {
        fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
    }
    fragmentTransaction.commit()
}

private val LOG_DATE_FORMAT= SimpleDateFormat("dd-MM HH:mm:ss.SSS", Locale.getDefault())
private val logFile= File(Environment.getExternalStorageDirectory(),"soundzones.log")
private var startWriteLog:Continuation<Unit>?=null;
internal var hasWritePermission =false
    set(value) {
        field=value
        if(value) startWriteLog?.resume(Unit)
    }
private val logChannel:Channel<LogInfo> by lazy {
    launch(CommonPool){
        if(!hasWritePermission) suspendCoroutine<Unit> {
            startWriteLog=it
        }
        while (!logChannel.isClosedForReceive) {
            val (message, timestamp, exception) = logChannel.receive()
            logFile.appendText("<${LOG_DATE_FORMAT.format(Date(timestamp))}> $message\n")
        }
    }
    Channel<LogInfo>(Channel.UNLIMITED)
}
internal fun log(message:String){
    Log.d("dada",message)
    logChannel.offer(LogInfo(message,System.currentTimeMillis(),null))
}

private data class LogInfo(val message: String,val timestamp:Long,val exception: Exception?)
const val EXTRA_SOUND_SET = "extra_sound_set"
const val SOUND_ZONES_DIR = "SoundZones"
val APP_DIR = Environment.getExternalStorageDirectory().absolutePath + File.separator + SOUND_ZONES_DIR
val NOISE_FILE = Environment.getExternalStorageDirectory().absolutePath + File.separator + SOUND_ZONES_DIR + File.separator + "noise.wav"

