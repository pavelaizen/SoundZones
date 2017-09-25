package com.gm.soundzones.activity

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.gm.soundzones.Consts
import com.gm.soundzones.R
import com.gm.soundzones.fragment.WelcomeMessageFragment
import com.gm.soundzones.fragment.preassessment.SoundSelectFragment
import com.gm.soundzones.listener.OnClickNextListener
import com.gm.soundzones.model.SoundRun
import com.gm.soundzones.model.SoundSet
import com.gm.soundzones.model.SoundTrack
import com.gm.soundzones.model.User
import com.gm.soundzones.replaceFragment

class PreAssessmentActivity : AppCompatActivity(), OnClickNextListener {
    private var stepIndex = 0
    private val lastStep = 5
    private val soundCheckUser: User by lazy {
        val mi = SoundSet("MI_Pre_0", SoundTrack("40-MI"), SoundTrack("40-MI"))
        val miNoise = SoundSet("MI_Pre_40", SoundTrack("40-MI"), SoundTrack("40-MI"))
        val ml = SoundSet("ML_Pre_0", SoundTrack("40-ML"), SoundTrack("40-ML"))
        val mlNoise = SoundSet("ML_Pre_40", SoundTrack("40-ML"), SoundTrack("40-ML"))
        val tr = SoundSet("TR_Pre_0", SoundTrack("40-TR"), SoundTrack("40-TR"))
        val trNoise = SoundSet("TR_Pre_40", SoundTrack("40-TR"), SoundTrack("40-TR"))
        val run1 = SoundRun("1", arrayOf(mi, miNoise))
        val run2 = SoundRun("2", arrayOf(ml, mlNoise))
        val run3 = SoundRun("3", arrayOf(tr, trNoise))
        User(0, arrayOf(run1, run2, run3))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (savedInstanceState == null) {
            val welcomeFragment = WelcomeMessageFragment.newInstance(
                    getString(R.string.welcome_text_title),
                    getString(R.string.press_next_when_ready))
            supportFragmentManager.beginTransaction().add(R.id.container, welcomeFragment)
                    .commit()
        }
    }

    private val getCurrentSoundSet: SoundSet
        get() {
            val runNumber = stepIndex / 2
            val setNumber = stepIndex % 2
            return soundCheckUser.soundRuns[runNumber].soundSets[setNumber]
        }

    override fun onClickNext(fragment: Fragment, args: Bundle) {
        if (stepIndex <= lastStep) {
            val bundle = Bundle()
            val currentSoundSet = getCurrentSoundSet
            bundle.putParcelable(Consts.EXTRA_SOUND_SET, currentSoundSet)
            val soundSelectFragment = SoundSelectFragment()
            soundSelectFragment.arguments = bundle
            replaceFragment(R.id.container, soundSelectFragment)
            stepIndex++
        } else {
            Toast.makeText(this, "pre complete", Toast.LENGTH_LONG).show()
        }
    }
}
