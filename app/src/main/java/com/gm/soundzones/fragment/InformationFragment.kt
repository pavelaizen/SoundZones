package com.gm.soundzones.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.gm.soundzones.R
import com.gm.soundzones.setTextOrHide
import kotlinx.android.synthetic.main.info_layout.*

/**
 * Created by Pavel Aizendorf on 24/09/2017.
 */
class InformationFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? = inflater?.inflate(R.layout.info_layout, container, false)

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews(arguments)
        btnNext.setOnClickListener({
            onClickNext()
        })
    }

    override fun update(args: Bundle) {
        super.update(args)
        setupViews(args)
    }


    private fun setupViews(args: Bundle?) {
        args?.apply {
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
            takeIf { it.containsKey(EXTRA_DESC3) }?.run {
                tvDesc3.setTextOrHide(getString(EXTRA_DESC3))
            }
            takeIf { it.containsKey(EXTRA_DESC4) }?.run {
                tvDesc4.setTextOrHide(getString(EXTRA_DESC4))
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
        val EXTRA_DESC4 = "extra_desc4"
        val EXTRA_BTN_NAME = "extra_btn_name"
        val EXTRA_BTN_VISIBILITY = "extra_btn_visibility"

        fun newInstance(title: String, desc1: String? = null, desc2: String? = null, desc3: String? = null, desc4: String? = null, btnName: String = "NEXT", btnVisibility: Int = View.VISIBLE): InformationFragment =
                InformationFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_TITLE, title)
                        putString(EXTRA_DESC1, desc1)
                        putString(EXTRA_DESC2, desc2)
                        putString(EXTRA_DESC3, desc3)
                        putString(EXTRA_DESC4, desc4)
                        putString(EXTRA_BTN_NAME, btnName)
                        putInt(EXTRA_BTN_VISIBILITY, btnVisibility)
                    }
                }
    }

}