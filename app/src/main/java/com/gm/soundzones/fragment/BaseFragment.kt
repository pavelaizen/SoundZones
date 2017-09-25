package com.gm.soundzones.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.View
import android.widget.TextView

/**
 * Created by Pavel Aizendorf on 19/09/2017.
 */
abstract class BaseFragment : Fragment() {
    fun TextView.setTextOrHide(text: CharSequence?) {
        visibility = if (TextUtils.isEmpty(text)) {
            View.GONE
        } else {
            setText(text)
            View.VISIBLE
        }
    }

    open fun update(args:Bundle) {

    }
}