package com.gm.soundzones.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import com.gm.soundzones.R
import com.gm.soundzones.fragment.WelcomeMessageFragment
import com.gm.soundzones.listener.OnClickNextListener

class PreAssessmentActivity : AppCompatActivity(), OnClickNextListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (savedInstanceState == null){
            val welcomeFragment = WelcomeMessageFragment.newInstance(
                    getString(R.string.welcome_text_title),
                    getString(R.string.press_next_when_ready))
            supportFragmentManager.beginTransaction().add(R.id.container, welcomeFragment)
                    .commit()
        }
    }

    override fun onClickNext(fragment: Fragment, args: Bundle) {

    }
}
