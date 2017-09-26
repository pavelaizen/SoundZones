package com.gm.soundzones.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.View
import android.widget.TextView
import com.gm.soundzones.listener.OnClickNextListener

/**
 * Created by Pavel Aizendorf on 19/09/2017.
 */
abstract class BaseFragment : Fragment() {

    open fun update(args:Bundle) {

    }

    fun onClickNext(args: Bundle = Bundle.EMPTY){
        if (activity is OnClickNextListener) {
            (activity as OnClickNextListener).onClickNext(this, args)
        }
    }
}