package com.gm.soundzones.manager

import android.content.Context
import android.content.SharedPreferences
import com.gm.soundzones.*

/**
 * Created by Pavel Aizendorf on 26/09/2017.
 */
object UserDataManager {
    const val SP_NAME = "SoundZonesSharedPrefs"
    private lateinit var sp: SharedPreferences
    val usersOrder = arrayOf(
            1, 6, 11, 16, 21, 26, 31, 36,
            2, 7, 12, 17, 22, 27, 32, 37,
            3, 8, 13, 18, 23, 28, 33, 38,
            4, 9, 14, 19, 24, 29, 34, 39,
            5, 10, 15, 20, 25, 30, 35, 40)


    fun setup(context: Context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    var userID: Int
        get() = sp.getString(KEY_USER_ID, "1").toInt()
        private set(value) = sp.edit().putString(KEY_USER_ID, value.toString()).apply()

    fun incrementUser() {
        var position = usersOrder.indexOf(userID)
        if (position == usersOrder.lastIndex) {
            position = 0
        } else {
            position++
        }
        val nextUserID = usersOrder[position]
        userID = nextUserID
    }

    fun putString(key: String, value: String) = sp.edit().putString(key, value).apply()

    fun getString(key: String) = getString(key, null)
    fun getString(key: String, defValue:String?) = sp.getString(key, defValue)

    fun getIpAddress() = sp.getString(KEY_IP_ADDRESS, "127.0.0.1")
    fun getMasterPort() = sp.getString(KEY_MASTER_PORT, "10000")
    fun getSecondaryPort() = sp.getString(KEY_SECONDARY_PORT, "10001")
    fun getVolumePort() = sp.getString(KEY_VOLUME_PORT, "10002")
    fun getNoisePort() = sp.getString(KEY_NOISE_PORT, "10003")
    fun isRemotePlayer() = sp.getBoolean(KEY_PLAYER_TYPE, false)
}