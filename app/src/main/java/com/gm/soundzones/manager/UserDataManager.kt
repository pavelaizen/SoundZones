package com.gm.soundzones.manager

import android.content.Context
import android.content.SharedPreferences

/**
 * Created by Pavel Aizendorf on 26/09/2017.
 */
object UserDataManager {
    private const val SP_NAME = "SoundZonesSharedPrefs"
    private const val KEY_USER_ID = "key_user_id"
    private lateinit var sp: SharedPreferences
    private val usersOrder = arrayOf(
            1, 6, 11, 16, 21, 26, 31, 36,
            2, 7, 12, 17, 22, 27, 32, 37,
            3, 8, 13, 18, 23, 28, 33, 38,
            4, 9, 14, 19, 24, 29, 34, 39,
            5, 10, 15, 20, 25, 30, 35, 40)

    fun setup(context: Context) {
        sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
    }

    var userID: Int
        get() = sp.getInt(KEY_USER_ID, 1)
        private set(value) = sp.edit().putInt(KEY_USER_ID, value).apply()

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
}