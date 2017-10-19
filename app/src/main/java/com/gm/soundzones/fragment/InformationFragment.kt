package com.gm.soundzones.fragment

import android.os.Bundle
import android.view.Gravity
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
                tvTitle.setTextOrHide(getCharSequence(EXTRA_TITLE))
            }
            takeIf { it.containsKey(EXTRA_DESC1) }?.run {
                tvDesc1.setTextOrHide(getCharSequence(EXTRA_DESC1))
            }
            takeIf { it.containsKey(EXTRA_DESC2) }?.run {
                tvDesc2.setTextOrHide(getCharSequence(EXTRA_DESC2))
//                tvDesc2.gravity = getInt(EXTRA_GRAVITY)
            }
            takeIf { it.containsKey(EXTRA_DESC3) }?.run {
                tvDesc3.setTextOrHide(getCharSequence(EXTRA_DESC3))
            }
            takeIf { it.containsKey(EXTRA_DESC3) }?.run {
                tvDesc3.setTextOrHide(getCharSequence(EXTRA_DESC3))
            }
            takeIf { it.containsKey(EXTRA_DESC4) }?.run {
                tvDesc4.setTextOrHide(getCharSequence(EXTRA_DESC4))
            }
            takeIf { it.containsKey(EXTRA_DESC5) }?.run {
                tvDesc5.setTextOrHide(getCharSequence(EXTRA_DESC5))
            }
            takeIf { it.containsKey(EXTRA_BTN_NAME) }?.run {
                btnNext.text = getCharSequence(EXTRA_BTN_NAME)
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
        val EXTRA_DESC5 = "extra_desc5"
        val EXTRA_GRAVITY = "extra_gravity"
        val EXTRA_BTN_NAME = "extra_btn_name"
        val EXTRA_BTN_VISIBILITY = "extra_btn_visibility"

        fun newInstance(title: String, desc1: CharSequence? = null, desc2: CharSequence? = null, desc3: CharSequence? = null, desc4: CharSequence? = null, desc5: CharSequence? = null, btnName: CharSequence = "NEXT", btnVisibility: Int = View.VISIBLE, infoGravity:Int = Gravity.CENTER): InformationFragment =
                InformationFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_TITLE, title)
                        desc1?.let {
                            putCharSequence(EXTRA_DESC1, it)
                        }
                        desc2?.let {
                            putCharSequence(EXTRA_DESC2, it)
                        }
                        desc3?.let {
                            putCharSequence(EXTRA_DESC3, it)
                        }
                        desc4?.let {
                            putCharSequence(EXTRA_DESC4, it)
                        }
                        desc5?.let {
                            putCharSequence(EXTRA_DESC5, it)
                        }
                        putInt(EXTRA_GRAVITY, infoGravity)
                        putCharSequence(EXTRA_BTN_NAME, btnName)
                        putInt(EXTRA_BTN_VISIBILITY, btnVisibility)
                    }
                }
    }

}