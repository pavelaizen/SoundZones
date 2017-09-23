package com.earlyense.soundzones.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.earlyense.soundzones.R
import com.earlyense.soundzones.listener.OnClickNextListener
import kotlinx.android.synthetic.main.welcome_layout.*

/**
 * Created by Pavel Aizendorf on 19/09/2017.
 */
class GetReadyFragment : BaseFragment() {
    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.welcome_layout, container, false)


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        tvText.setText(arguments!!.getInt(ARG_TEXT, 0))
//        btnNext.setOnClickListener({
//            if (activity is OnClickNextListener) {
//                (activity as OnClickNextListener).onClickNext(it)
//            }
//        })
    }

    companion object {
        val ARG_TEXT = "arg_text"
    }
}