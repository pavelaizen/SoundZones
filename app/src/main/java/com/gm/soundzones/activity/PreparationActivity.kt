package com.gm.soundzones.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gm.soundzones.R
import com.gm.soundzones.fragment.InformationFragment
import com.gm.soundzones.listener.OnClickNextListener

//created by Pavel
class PreparationActivity : AppCompatActivity(), OnClickNextListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (savedInstanceState == null) {
            val welcomeFragment = InformationFragment.newInstance(
                    getString(R.string.intro_title),
                    getString(R.string.intro_desc1),
                    getString(R.string.intro_desc2),
                    getString(R.string.intro_ready),
                    btnVisibility = View.INVISIBLE)
            supportFragmentManager.beginTransaction().add(R.id.container, welcomeFragment)
                    .commitNow()
        }
    }

    override fun onClickNext(fragment: Fragment, args: Bundle) {
//        startActivity(Intent(this, PreAssessmentActivity::class.java))
//        finish()
    }


}
