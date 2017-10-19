package com.gm.soundzones

import android.os.Environment
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.TextView
import java.io.File
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.channels.Channel
import kotlinx.coroutines.experimental.launch
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.suspendCoroutine

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
fun AppCompatActivity.loadFragment(addToBackStack: Boolean = false, load: FragmentTransaction.() -> Any) {
    val fragmentTransaction = supportFragmentManager
            .beginTransaction();

    val fragmentName = fragmentTransaction.load()
    if (addToBackStack) {
        fragmentTransaction.addToBackStack(fragmentName.javaClass.simpleName)
    }
    fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
    fragmentTransaction.commit()
}

private val LOG_DATE_FORMAT= SimpleDateFormat("dd-MM HH:mm:ss.SSS", Locale.US)
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
            logFile.appendText("<${LOG_DATE_FORMAT.format(Date(timestamp))}> $message\n${StringWriter().also {
                exception?.printStackTrace(PrintWriter(it))
            }}")
        }
    }
    Channel<LogInfo>(Channel.UNLIMITED)
}
internal fun log(message:String,exception: Exception?=null){
    Log.d("dada",message,exception)
    logChannel.offer(LogInfo(message,System.currentTimeMillis(),exception))
}

fun TextView.setTextOrHide(text: CharSequence?) {
    visibility = if (TextUtils.isEmpty(text)) {
        View.GONE
    } else {
        setText(text)
        View.VISIBLE
    }
}

private data class LogInfo(val message: String,val timestamp:Long,val exception: Exception?)
const val EXTRA_USER_ID = "extra_user_id"
const val EXTRA_SOUND_SET = "extra_sound_set"
const val EXTRA_VOLUME_LEVEL = "extra_volume_Level"
const val SOUND_ZONES_DIR = "SoundZones"
internal val APP_DIR = Environment.getExternalStorageDirectory().absolutePath + File.separator + SOUND_ZONES_DIR
internal val NOISE_FILE = Environment.getExternalStorageDirectory().absolutePath + File.separator + SOUND_ZONES_DIR + File.separator + "noise.wav"
const val SECONDARY_VOLUME_DIFF_PERCENT = 20 //20%
const val TRAINING_RUN = "Training"
const val KEY_IP_ADDRESS = "pref_key_ip_address"
const val KEY_MASTER_PORT = "pref_master_music_port"
const val KEY_SECONDARY_PORT = "pref_secondary_music_port"
const val KEY_VOLUME_PORT = "pref_volume_port"
const val KEY_NOISE_PORT = "pref_noise_port"
const val KEY_PLAYER_TYPE = "pref_player_type"
const val KEY_WAIT_DELAY = "key_wait_sec"
const val KEY_USER_ID = "key_user_id"


