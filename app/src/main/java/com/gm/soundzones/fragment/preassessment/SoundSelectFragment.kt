package com.gm.soundzones.fragment.preassessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.EXTRA_SOUND_SET
import com.gm.soundzones.NOISE_FILE
import com.gm.soundzones.R
import com.gm.soundzones.fragment.BaseFragment
import com.gm.soundzones.manager.AudioPlayer
import com.gm.soundzones.manager.MusicPlayerFactory
import com.gm.soundzones.model.SoundSet
import kotlinx.android.synthetic.main.preset_layout.*

/**
 * Created by Pavel Aizendorf on 24/09/2017.
 */
class SoundSelectFragment : BaseFragment() {
    lateinit var soundSet: SoundSet
    lateinit var audioPlayer: AudioPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        soundSet = arguments.getParcelable<SoundSet>(EXTRA_SOUND_SET)
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater?.inflate(R.layout.preset_layout, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        tvPairName.text = soundSet.pair
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wheel.setPosition(300.0)
        audioPlayer = MusicPlayerFactory.musicPlayer
        audioPlayer.playTrack2(soundSet.secondaryTrack.fullPath)
        if (soundSet.hasNoise) {
            audioPlayer.playNoise(NOISE_FILE)
        }
        wheel.onChange = {
            var value = when {
                it < 0 -> 0.0
                it > 600.0 -> 600.0
                else -> it
            };
            value = value.div(6)
            audioPlayer.setVolume(value.toInt())
        }


    }


}