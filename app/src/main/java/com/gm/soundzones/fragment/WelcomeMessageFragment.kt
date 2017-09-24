package com.gm.soundzones.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.R
import com.gm.soundzones.listener.OnClickNextListener
import kotlinx.android.synthetic.main.welcome_layout.*

/**
 * Created by Pavel Aizendorf on 24/09/2017.
 */
class WelcomeMessageFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.welcome_layout, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments.getString(EXTRA_TITLE)
        val desc1 = arguments.getString(EXTRA_DESC1)
        val desc2 = arguments.getString(EXTRA_DESC2)
        val desc3 = arguments.getString(EXTRA_DESC3)
        val btnName = arguments.getString(EXTRA_BTN_NAME)
        tvTitle.setTextOrHide(title)
        tvDesc1.setTextOrHide(desc1)
        tvDesc2.setTextOrHide(desc2)
        tvDesc3.setTextOrHide(desc3)
        btnNext.text = btnName
        btnNext.visibility = arguments.getInt(EXTRA_BTN_VISIBILITY)
        btnNext.setOnClickListener({
            if (activity is OnClickNextListener) {
                (activity as OnClickNextListener).onClickNext(it)
            }
        })
    }


    companion object {
        val EXTRA_TITLE = "extra_title"
        val EXTRA_DESC1 = "extra_desc1"
        val EXTRA_DESC2 = "extra_desc2"
        val EXTRA_DESC3 = "extra_desc3"
        val EXTRA_BTN_NAME = "extra_btn_name"
        val EXTRA_BTN_VISIBILITY = "extra_btn_visibility"

        fun newInstance(title: String, desc1: String? = null, desc2: String? = null, desc3: String? = null, btnName: String = "NEXT", btnVisibility: Int = View.VISIBLE): WelcomeMessageFragment =
                WelcomeMessageFragment().also {
                    val bundle = Bundle()
                    bundle.putString(EXTRA_TITLE, title)
                    bundle.putString(EXTRA_DESC1, desc1)
                    bundle.putString(EXTRA_DESC2, desc2)
                    bundle.putString(EXTRA_DESC3, desc3)
                    bundle.putString(EXTRA_BTN_NAME, btnName)
                    bundle.putInt(EXTRA_BTN_VISIBILITY, btnVisibility)
                    it.arguments = bundle
                }
    }

}