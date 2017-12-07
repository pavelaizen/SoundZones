package com.gm.soundzones.fragment

import android.os.Build
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.*
import com.gm.soundzones.activity.UserMusicActivity
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.manager.AudioPlayer
import com.gm.soundzones.manager.MusicPlayerFactory
import com.gm.soundzones.manager.Result
import com.gm.soundzones.manager.UserDataManager
import com.gm.soundzones.model.SoundRun
import com.gm.soundzones.model.SoundSet
import com.gm.soundzones.model.User
import com.gm.soundzones.view.WheelView
import kotlinx.android.synthetic.main.user_music_layout.*
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit
import android.content.pm.PackageManager
import android.R.attr.versionName
import android.content.pm.PackageInfo



/**
 * Created by Pavel Aizendorf on 19/09/2017.
 */
class SoundFragment : BaseFragment() {
    lateinit var user: User
    private var runIndex: Int = 0
    var setIndex: Int = 0
    lateinit var soundRun: SoundRun
    lateinit var soundSet: SoundSet
    lateinit var player: AudioPlayer
    var selectedVolumeLevel: Int = 0
    var slaveBaselineVolume: Int = 0
    var snackBar: Snackbar? = null
    var job: Job? = null

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


        val baselineVolume = DataProvider.defaultVolumeLevels.getOrElse(soundSet.primaryTrack.dirName, { 0 })
        slaveBaselineVolume = AudioPlayer.getSlaveBaselineVolume(baselineVolume)
        log("playing ${soundSet.pair} $baselineVolume/$slaveBaselineVolume")
        wheel.setPosition(slaveBaselineVolume * (WheelView.MAX_PERCENTAGE / 100.0))
        player = MusicPlayerFactory().getMusicPlayer(baselineVolume);

        wheel.setOnClickListener {
            snackBar?.isShown ?: run {
                btnNext.visibility = View.VISIBLE
                wheel.setText(null)
            }
        }
        btnNext.setOnClickListener {
            if (phase == Phase.ACCEPTABLE) {
                phase = Phase.GREAT
                setUiControls(true)

                soundSet.acceptableVolume = selectedVolumeLevel
//                soundRun.runId.takeUnless { it == TRAINING_RUN }?.let {
                DataProvider.applyVolumeAccept(user.id, soundRun.runId, setIndex, selectedVolumeLevel)
//                }

            } else {
                player.stop()
                soundSet.greatVolume = selectedVolumeLevel
//                soundRun.runId.takeUnless { it == TRAINING_RUN }?.let {
                DataProvider.applyVolumeGreat(user.id, soundRun.runId, setIndex, selectedVolumeLevel)
//                }
                onClickNext()
            }
        }
        setUiControls(false)
        playMusic()
    }

    private fun setUiControls(isSoftUpdate: Boolean) {
        val phaseName = when (phase) {
            Phase.ACCEPTABLE -> R.string.acceptable
            Phase.GREAT -> R.string.great
        }
        tvButtonName.text = getString(phaseName)
        wheel.setText(null)
        if (!isSoftUpdate) {
            wheel.setPosition(slaveBaselineVolume * (WheelView.MAX_PERCENTAGE / 100.0))
        }
        log("wheel enabled = $isSoftUpdate")
        wheel.isEnabled = isSoftUpdate
        tvUserId.text = user.id.toString()
        tvBlock.text = soundRun.runId
        tvRunName.text = getNameOfTheSet()
        btnNext.visibility = View.INVISIBLE
    }

    private fun playMusic() {
        job = launch(UI) {
            val hasNoise = soundSet.hasNoise
            log("SF playing first track")
            errorHandler{player.playTrack1(soundSet.primaryTrack.fullPath)}
            if (hasNoise) {
                errorHandler{player.playNoise(NOISE_FILE)}
            }
            takeIf { phase == Phase.ACCEPTABLE }.run { delay(UserDataManager.getString(KEY_WAIT_DELAY, "15").toLong(), TimeUnit.SECONDS) }
            log("playing second track after delay")
            log("SF playing second track")
            DataProvider.defaultVolumeLevels.get(soundSet.secondaryTrack.dirName)?.let {
                log("Setting slave volume to $it")
                errorHandler{player.setVolumeSecondary(it)}
            }
            errorHandler{player.playTrack2(soundSet.secondaryTrack.fullPath)}

            DataProvider.defaultVolumeLevels.get(soundSet.primaryTrack.dirName)?.let {
                log("Setting master volume to $it")
                errorHandler{player.setVolumeMaster(it)}
            }
            log("SF wheel enabled after delay")
            wheel.isEnabled = true
            wheel.onChange = {
                launch(UI) {
                    selectedVolumeLevel = it.div(WheelView.MAX_PERCENTAGE / 100).toInt()
                    btnNext.visibility = View.INVISIBLE
                    log("SF btnNext invisible")
                    wheel.setText(when (phase) {
                        Phase.ACCEPTABLE -> R.string.acceptable
                        Phase.GREAT -> R.string.great
                    })
                    errorHandler{player.setVolumeSecondary(it.div(WheelView.MAX_PERCENTAGE / 100).toInt())}
                }
            }

        }
    }

    fun getNameOfTheSet(): String {
        val soundSetSize = soundRun.soundSets.size
        val currentSetReadableIndex = setIndex * 2 + 1
        when (phase) {
            Phase.ACCEPTABLE -> return "$currentSetReadableIndex/${soundSetSize * 2}"
            Phase.GREAT -> return "${currentSetReadableIndex + 1}/${soundSetSize * 2}"
        }
    }

    private fun errorHandler(result: suspend ()->Result) {
        launch(UI) {
            when (result()) {
                Result.IO_ERROR -> showError("CONNECTION_PROBLEM")
                Result.UNKNOWN_HOST -> showError("UNKNOWN HOST")
            }
        }
    }

    private fun showError(text: String) {
        snackBar = Snackbar.make(activity.findViewById(R.id.coordinateLayout), text, Snackbar.LENGTH_INDEFINITE).also {
            it.show()
        }
    }

    override fun onDestroy() {
        job?.cancel()
        player.stop()
        super.onDestroy()
    }
}
