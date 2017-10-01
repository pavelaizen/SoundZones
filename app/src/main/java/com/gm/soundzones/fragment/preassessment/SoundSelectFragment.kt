package com.gm.soundzones.fragment.preassessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.EXTRA_VOLUME_LEVEL
import com.gm.soundzones.NOISE_FILE
import com.gm.soundzones.R
import com.gm.soundzones.activity.PreAssessmentActivity
import com.gm.soundzones.fragment.BaseFragment
import com.gm.soundzones.manager.AudioPlayer
import com.gm.soundzones.manager.MusicPlayerFactory
import com.gm.soundzones.manager.Result
import com.gm.soundzones.model.SoundSet
import com.gm.soundzones.view.WheelView
import kotlinx.android.synthetic.main.preset_layout.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

/**
 * Created by Pavel Aizendorf on 24/09/2017.
 */
class SoundSelectFragment : BaseFragment() {
    private lateinit var soundSet: SoundSet
    private lateinit var audioPlayer: AudioPlayer
    private var selectedVolumeLevel = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundSet = (activity as PreAssessmentActivity).getCurrentSoundSet
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?)
            = inflater?.inflate(R.layout.preset_layout, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvPairName.text = soundSet.pair
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        launch(UI){
            wheel.setPosition(WheelView.MAX_PERCENTAGE / 3.0)
            val slaveVolume = AudioPlayer.getSlaveBaselineVolume(33)
            audioPlayer = MusicPlayerFactory.getMusicPlayer(slaveVolume)
            audioPlayer.playTrack2(soundSet.secondaryTrack.fullPath)
            if (soundSet.hasNoise) {
                audioPlayer.playNoise(NOISE_FILE)
            }
            wheel.onChange = {
                launch(UI) {
                    wheel.setText(R.string.set)
                    selectedVolumeLevel = it.div(WheelView.MAX_PERCENTAGE / 100).toInt()
                    audioPlayer.setVolumeSecondary(selectedVolumeLevel)
                    btnNext.visibility = View.INVISIBLE
                }
            }
            wheel.setOnClickListener {
                btnNext.visibility = View.VISIBLE
                wheel.setText(null)
            }
            btnNext.visibility = View.INVISIBLE
            btnNext.setOnClickListener {
                audioPlayer.stop()
                onVolumeLevelSelected(selectedVolumeLevel)
            }
        }


    }

    fun onVolumeLevelSelected(volume: Int) {
        val bundle = Bundle()
        bundle.putInt(EXTRA_VOLUME_LEVEL, volume)
        onClickNext(bundle)
    }
    private fun errorHandler(errorResult: Result){
        when(errorResult){
            Result.IO_ERROR-> TODO("handle error reason for IO")
            Result.UNKNOWN_HOST->TODO("handle error reason for unknown host")
        }
    }


}