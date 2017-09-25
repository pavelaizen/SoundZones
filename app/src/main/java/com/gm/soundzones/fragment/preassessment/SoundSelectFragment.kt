package com.gm.soundzones.fragment.preassessment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.EXTRA_SOUND_SET
import com.gm.soundzones.R
import com.gm.soundzones.fragment.BaseFragment
import com.gm.soundzones.model.SoundSet
import kotlinx.android.synthetic.main.preset_layout.*

/**
 * Created by Pavel Aizendorf on 24/09/2017.
 */
class SoundSelectFragment :BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?) = inflater?.inflate(R.layout.preset_layout, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val soundSet = arguments.getParcelable<SoundSet>(EXTRA_SOUND_SET)
        tvPairName.text = soundSet.pair
    }


}