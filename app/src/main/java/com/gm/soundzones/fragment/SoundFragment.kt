package com.gm.soundzones.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.MUSIC_WAIT_TIME
import com.gm.soundzones.NOISE_FILE
import com.gm.soundzones.R
import com.gm.soundzones.activity.UserMusicActivity
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.listener.OnClickNextListener
import com.gm.soundzones.manager.AudioPlayer
import com.gm.soundzones.manager.MusicPlayerFactory
import com.gm.soundzones.model.SoundRun
import com.gm.soundzones.model.SoundSet
import com.gm.soundzones.model.User
import com.gm.soundzones.view.WheelView
import kotlinx.android.synthetic.main.user_music_layout.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit

/**
 * Created by Pavel Aizendorf on 19/09/2017.
 */
class SoundFragment : Fragment() {
    lateinit var user: User
    var runIndex: Int = 0
    var setIndex: Int = 0
    lateinit var soundRun: SoundRun
    lateinit var soundSet: SoundSet
    lateinit var player: AudioPlayer
    var selectedVolumeLevel: Int = 0

    enum class Phase {
        ACCEPTABLE, GREAT
    }

    var phase = Phase.ACCEPTABLE

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?) = inflater.inflate(R.layout.user_music_layout, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val musicActivity = activity as UserMusicActivity
        user = musicActivity.user
        runIndex = musicActivity.runIndex
        setIndex = musicActivity.setIndex
        soundRun = user.soundRuns[runIndex]
        soundSet = soundRun.soundSets[setIndex]

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        val baselineVolume = DataProvider.defaultVolumeLevels[soundSet.primaryTrack.dirName]
        baselineVolume?.let {
            val slaveBaselineVolume = AudioPlayer.getSlaveBaselineVolume(it)
            wheel.setPosition(slaveBaselineVolume*(WheelView.MAX_PERCENTAGE/100.0))
            player = MusicPlayerFactory.getMusicPlayer(it);
        }

        wheel.setOnClickListener {
            btnNext.visibility = View.VISIBLE
            wheel.setText(null)
        }
        btnNext.setOnClickListener {
            player.stop()
            if (phase == Phase.ACCEPTABLE) {
                phase = Phase.GREAT
                setUiControls()
                playMusic()
                soundSet.acceptableVolume = selectedVolumeLevel
            } else {

                soundSet.greatVolume = selectedVolumeLevel
                if (activity is OnClickNextListener) {
                    (activity as OnClickNextListener).onClickNext(this, Bundle())
                }
            }
        }
        setUiControls()
        playMusic()
    }

    private fun setUiControls() {
        val phaseName = when (phase) {
            Phase.ACCEPTABLE -> R.string.acceptable
            Phase.GREAT -> R.string.great
        }
        tvButtonName.text = getString(phaseName)
        wheel.setText(getString(phaseName))

        wheel.isEnabled = false
        tvUserId.text = user.id.toString()
        tvBlock.text = soundRun.runId
        tvRunName.text = getSetString()
        btnNext.visibility = View.INVISIBLE
    }

    private fun playMusic() {
        launch(UI) {
            val hasNoise = soundSet.hasNoise
            player.playTrack1(soundSet.primaryTrack.fullPath)
            if (hasNoise) {
                player.playNoise(NOISE_FILE)
            }
            takeIf { phase == Phase.ACCEPTABLE }.run { delay(MUSIC_WAIT_TIME, TimeUnit.SECONDS) }
            player.playTrack2(soundSet.secondaryTrack.fullPath)
            DataProvider.defaultVolumeLevels.get(soundSet.primaryTrack.dirName)?.let {
                player.setVolume(it)
            }
            wheel.isEnabled = true
            wheel.onChange = {
                launch(UI) {
                    selectedVolumeLevel = it.div(WheelView.MAX_PERCENTAGE / 100).toInt()
                    btnNext.visibility = View.INVISIBLE
                    wheel.setText(when (phase) {
                        Phase.ACCEPTABLE -> R.string.acceptable
                        Phase.GREAT -> R.string.great
                    })
                    player.setVolume(it.div(WheelView.MAX_PERCENTAGE / 100).toInt())
                }
            }

        }
    }

    fun getSetString(): String {
        val soundSetSize = soundRun.soundSets.size
        val currentSetReadableIndex = setIndex + 1
        when (phase) {
            Phase.ACCEPTABLE -> return "$currentSetReadableIndex/${soundSetSize * 2}"
            Phase.GREAT -> return "${currentSetReadableIndex + soundSetSize}/${soundSetSize * 2}"
        }
    }
}
