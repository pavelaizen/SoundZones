package com.gm.soundzones.activity

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.gm.soundzones.R
import kotlinx.android.synthetic.main.splash.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit


/**
 * Created by Pavel Aizendorf on 23/09/2017.
 */
class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)
        splashView.startAnim()
        splashView.setOnParticleAnimListener {
            launch(UI) {
                delay(1, TimeUnit.SECONDS)
                startActivity(Intent(this@SplashActivity, PreparationActivity::class.java))
                finish()
            }
        }
    }

    override fun onBackPressed() {

    }
}