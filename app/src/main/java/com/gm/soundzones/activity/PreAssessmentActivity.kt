package com.gm.soundzones.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.gm.soundzones.EXTRA_VOLUME_LEVEL
import com.gm.soundzones.R
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.fragment.InformationFragment
import com.gm.soundzones.fragment.preassessment.SoundSelectFragment
import com.gm.soundzones.hasWritePermission
import com.gm.soundzones.listener.OnClickNextListener
import com.gm.soundzones.manager.UserDataManager
import com.gm.soundzones.model.*
import com.gm.soundzones.replaceFragment
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.experimental.intrinsics.suspendCoroutineOrReturn

class PreAssessmentActivity : AppCompatActivity(), OnClickNextListener {
    private var continuation: Continuation<Boolean>? = null

    private var stepIndex =0
    private val lastStep = 6
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
            val welcomeFragment = InformationFragment.newInstance(
                    getString(R.string.welcome_text_title),
                    getString(R.string.press_next_when_ready),
                    desc4 = UserDataManager.userID.toString(),
                    btnVisibility = View.INVISIBLE)
            supportFragmentManager.beginTransaction().add(R.id.container, welcomeFragment)
                    .commitNow()

            launch(UI) {
                do {
                    val isGranted = suspendCoroutineOrReturn<Boolean> {
                        continuation = it
                        requestWritePermission()
                        COROUTINE_SUSPENDED
                    }
                } while (!isGranted)
                hasWritePermission =true
                welcomeFragment.update(Bundle().also {
                    it.putInt(InformationFragment.EXTRA_BTN_VISIBILITY, View.VISIBLE)
                })
            }
        }


    }

    val getCurrentSoundSet: SoundSet
        get() {
            val runNumber = stepIndex / 2
            val setNumber = stepIndex % 2
            return soundCheckUser.soundRuns[runNumber].soundSets[setNumber]
        }

    override fun onClickNext(fragment: Fragment, args: Bundle) {
        if (fragment is SoundSelectFragment) {
            val volumeLevel = args.getInt(EXTRA_VOLUME_LEVEL)
            val currentSoundSet = getCurrentSoundSet
            val dirName = currentSoundSet.secondaryTrack.dirName
            val hasNoise = currentSoundSet.hasNoise
            DataProvider.defaultVolumeLevels.put(UserDefaultVolume(dirName, hasNoise), volumeLevel)
            if(stepIndex<lastStep){
                stepIndex++
            }
        }
        if (stepIndex < lastStep) {
            val soundSelectFragment = SoundSelectFragment()
            replaceFragment(R.id.container, soundSelectFragment)
        } else {
            startActivity(Intent(this, PreparationActivity::class.java))
            finish()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_WRITE_PERMISSION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    continuation?.resume(true)
                } else {
                    continuation?.resume(false)
                }
                continuation = null
            }
        }
    }

    private fun requestWritePermission(): Boolean {
        val writePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && writePermission != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), REQUEST_WRITE_PERMISSION)
        } else {
            continuation?.resume(true)
            continuation = null
        }
        return false;
    }

    companion object {
        val REQUEST_WRITE_PERMISSION = 1
    }
}
