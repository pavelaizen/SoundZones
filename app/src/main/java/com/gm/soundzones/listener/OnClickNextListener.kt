package com.gm.soundzones.listener

import android.os.Bundle
import android.support.v4.app.Fragment


interface OnClickNextListener {
    fun onClickNext(fragment: Fragment, args: Bundle)
}