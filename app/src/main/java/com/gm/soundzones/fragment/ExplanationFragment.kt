package com.gm.soundzones.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.R
import kotlinx.android.synthetic.main.explanation_layout.*

/**
 * Created by Pavel Aizendorf on 19/10/2017.
 */
class ExplanationFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.explanation_layout, container, false)

    override fun update(args: Bundle) {
        super.update(args)
        args.takeIf { it.containsKey(EXTRA_BTN_VISIBILITY) }?.run {
            btnNext.visibility = getInt(EXTRA_BTN_VISIBILITY)
        }
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        btnNext.setOnClickListener {
            onClickNext()
        }
    }

    companion object {
        val EXTRA_BTN_VISIBILITY = "extra_btn_visibility"
    }

}