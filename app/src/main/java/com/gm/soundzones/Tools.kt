package com.gm.soundzones

import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.util.Log

/**
 * Created by Pavel Aizendorf on 25/09/2017.
 */
fun AppCompatActivity.replaceFragment(containerId: Int, fragment: Fragment, addToBackStack: Boolean = false) {
    val fragmentTransaction = supportFragmentManager
            .beginTransaction()
            .replace(containerId, fragment, fragment.javaClass.simpleName);
    if (addToBackStack) {
        fragmentTransaction.addToBackStack(fragment.javaClass.simpleName)
    }
    fragmentTransaction.commit()
}

internal fun log(message:String){
    Log.d("dada",message)
}
const val EXTRA_SOUND_SET = "extra_sound_set"