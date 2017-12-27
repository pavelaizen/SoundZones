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
import kotlinx.coroutines.experimental.Job
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
    private val jobs:MutableList<Job> = mutableListOf()
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
        btnNext.visibility = View.INVISIBLE
        val job = launch(UI){
            wheel.setOnClickListener {
                snackBar?.isShown ?: run{
                    btnNext.visibility = View.VISIBLE
                    wheel.setText(null)
                }

            }
            wheel.onChange = {
                jobs.add(launch(UI) {
                    wheel.setText(R.string.set)
                    btnNext.visibility = View.INVISIBLE
                    selectedVolumeLevel = it.div(WheelView.MAX_PERCENTAGE / 100).toInt()
                    errorHandler{audioPlayer.setVolumeSecondary(selectedVolumeLevel)}
                })
            }
            wheel.setPosition(WheelView.MAX_PERCENTAGE / 3.0)
            val slaveVolume = 33
            audioPlayer = MusicPlayerFactory().getMusicPlayer(slaveVolume)
            errorHandler{audioPlayer.playTrack2(soundSet.secondaryTrack.fullPath)}
            if (soundSet.hasNoise) {
                errorHandler{audioPlayer.playNoise(NOISE_FILE)}
            }


        }
        btnNext.setOnClickListener {
            audioPlayer.stop()
            onVolumeLevelSelected(selectedVolumeLevel)
        }
        jobs.add(job)

    }

    fun onVolumeLevelSelected(volume: Int) {
        val bundle = Bundle()
        bundle.putInt(EXTRA_VOLUME_LEVEL, volume)
        onClickNext(bundle)
    }
    private fun errorHandler(result: suspend ()->Result) {
        launch(UI) {
            when (result()) {
                Result.IO_ERROR -> showError("CONNECTION_PROBLEM")
                Result.UNKNOWN_HOST -> showError("UNKNOWN HOST")
            }
        }
    }

    private fun showError(text:String){
        snackBar = Snackbar.make(activity.findViewById(R.id.coordinateLayout), text, Snackbar.LENGTH_INDEFINITE).also { it.show() }
    }

    override fun onDestroy() {
        audioPlayer.stop()
        for (job in jobs) {
            job.cancel()
        }
        super.onDestroy()
    }


}