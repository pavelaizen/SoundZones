package com.gm.soundzones.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gm.soundzones.R
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.fragment.SoundFragment
import com.gm.soundzones.manager.UserDataManager
import com.gm.soundzones.replaceFragment

/**
 * Created by Pavel Aizendorf on 26/09/2017.
 */
class UserMusicActivity : AppCompatActivity() {
    val user = DataProvider.getUser(UserDataManager.userID)
    var runIndex = 0
    var setIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)

        if (savedInstanceState == null) {
            replaceFragment(R.id.container, SoundFragment())
        }

    }
}