package com.gm.soundzones.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gm.soundzones.Utils
import com.gm.soundzones.log

/**
 * Created by Pavel Aizendorf on 19/10/2017.
 */
abstract class BaseActivity : AppCompatActivity(){
    override fun onBackPressed() {
//        super.onBackPressed()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        log(Utils.getAppVersion(this))
    }
}