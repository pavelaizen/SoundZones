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
        setupViews(arguments)
        btnNext.setOnClickListener({
            if (activity is OnClickNextListener) {
                (activity as OnClickNextListener).onClickNext(this)
            }
        })
    }

    override fun update(args: Bundle) {
        super.update(args)
        setupViews(args)
    }


    private fun setupViews(args: Bundle) {
        args.apply {
            takeIf { it.containsKey(EXTRA_TITLE) }?.run {
                tvTitle.setTextOrHide(getString(EXTRA_TITLE))
            }

            takeIf { it.containsKey(EXTRA_DESC1) }?.run {
                tvDesc1.setTextOrHide(getString(EXTRA_DESC1))
            }
            takeIf { it.containsKey(EXTRA_DESC2) }?.run {
                tvDesc2.setTextOrHide(getString(EXTRA_DESC2))
            }
            takeIf { it.containsKey(EXTRA_DESC3) }?.run {
                tvDesc3.setTextOrHide(getString(EXTRA_DESC3))
            }
            takeIf { it.containsKey(EXTRA_BTN_NAME) }?.run {
                btnNext.text = getString(EXTRA_BTN_NAME)
            }
            takeIf { it.containsKey(EXTRA_BTN_VISIBILITY) }?.run {
                btnNext.visibility = getInt(EXTRA_BTN_VISIBILITY)
            }
        }
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