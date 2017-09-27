package com.gm.soundzones.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.R
import com.gm.soundzones.activity.UserMusicActivity
import kotlinx.android.synthetic.main.user_music_layout.*

/**
 * Created by Pavel Aizendorf on 19/09/2017.
 */
class SoundFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.user_music_layout, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        wheel.setText(R.string.acceptable)
        wheel.isEnabled = false
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val musicActivity = getMusicActivity()
        val user = musicActivity.user
        val runIndex = musicActivity.runIndex
        val index = musicActivity.setIndex
        val soundRun = user.soundRuns[runIndex]
        val soundSets = soundRun.soundSets
        tvUserId.text = user.id.toString()
        tvBlock.text =soundRun.runId
        tvRunName.text = "$index/${soundSets.size}"

    }

    private fun getMusicActivity() = activity as UserMusicActivity



}