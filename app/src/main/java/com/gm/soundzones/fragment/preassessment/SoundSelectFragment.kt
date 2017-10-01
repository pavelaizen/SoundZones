package com.gm.soundzones.fragment.preassessment

import android.os.Bundle
import android.support.design.widget.Snackbar
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
import kotlinx.android.synthetic.main.activity_toolbar_container.*
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
    private var snackBar:Snackbar?=null
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
            val result = audioPlayer.playTrack2(soundSet.secondaryTrack.fullPath)
            errorHandler(result)
            if (soundSet.hasNoise) {
                errorHandler(audioPlayer.playNoise(NOISE_FILE))
            }
            wheel.onChange = {
                launch(UI) {
                    wheel.setText(R.string.set)
                    selectedVolumeLevel = it.div(WheelView.MAX_PERCENTAGE / 100).toInt()
                    errorHandler(audioPlayer.setVolumeSecondary(selectedVolumeLevel))
                    btnNext.visibility = View.INVISIBLE
                }
            }


            wheel.setOnClickListener {
                snackBar?.isShown ?: run{
                    btnNext.visibility = View.VISIBLE
                    wheel.setText(null)
                }

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
            Result.IO_ERROR-> showError("CONNECTION_PROBLEM")
            Result.UNKNOWN_HOST->showError("UNKNOWN HOST")
        }
    }

    private fun showError(text:String){
        snackBar = Snackbar.make(activity.findViewById(R.id.coordinateLayout), text, Snackbar.LENGTH_INDEFINITE).also { it.show() }
    }

    override fun onDestroy() {
        audioPlayer.stop()
        super.onDestroy()
    }


}