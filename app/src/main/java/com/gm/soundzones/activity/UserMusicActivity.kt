package com.gm.soundzones.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import com.gm.soundzones.R
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.fragment.SoundFragment
import com.gm.soundzones.listener.OnClickNextListener
import com.gm.soundzones.manager.UserDataManager
import com.gm.soundzones.replaceFragment

/**
 * Created by Pavel Aizendorf on 26/09/2017.
 */
class UserMusicActivity : AppCompatActivity(), OnClickNextListener {
    override fun onClickNext(fragment: Fragment, args: Bundle) {
        val soundRunLastIndex = user.soundRuns.lastIndex
        val soundSet = user.soundRuns[runIndex]
        val soundSetLastIndex = soundSet.soundSets.lastIndex
        if (setIndex<soundSetLastIndex){
            setIndex++
        }else{
            setIndex = 0
            if (runIndex<soundRunLastIndex){
                runIndex++
            }else{
                UserDataManager.incrementUser()
                startActivity(Intent(this, PreAssessmentActivity::class.java))
                finish()
                return
            }
        }
        replaceFragment(R.id.container, SoundFragment())

    }

    val user = DataProvider.getUser(UserDataManager.userID)
    var runIndex = 0
    var setIndex = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (savedInstanceState == null) {
            runIndex = 0
            setIndex = 0
            replaceFragment(R.id.container, SoundFragment())
        }

    }


}