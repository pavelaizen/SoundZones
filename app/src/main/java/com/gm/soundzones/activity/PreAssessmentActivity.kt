package com.gm.soundzones.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.gm.soundzones.*
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.fragment.InformationFragment
import com.gm.soundzones.fragment.preassessment.SoundSelectFragment
import com.gm.soundzones.listener.OnClickNextListener
import com.gm.soundzones.manager.UserDataManager
import com.gm.soundzones.model.SoundRun
import com.gm.soundzones.model.SoundSet
import com.gm.soundzones.model.SoundTrack
import com.gm.soundzones.model.User
import kotlinx.android.synthetic.main.activity_toolbar_container.*
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.experimental.intrinsics.suspendCoroutineOrReturn

class PreAssessmentActivity : BaseActivity(), OnClickNextListener {
    private var continuation: Continuation<Boolean>? = null

    private var stepIndex = 0
    private val lastStep = 6
    private var prevSelectedVolume = 0
    private val soundCheckUser: User by lazy {
        val mi = SoundSet("MI_Pre_0", SoundTrack("40-MI"), SoundTrack("39-MI"))
        val miNoise = SoundSet("MI_Pre_40", SoundTrack("40-MI"), SoundTrack("39-MI"))
        val ml = SoundSet("ML_Pre_0", SoundTrack("40-ML"), SoundTrack("39-ML"))
        val mlNoise = SoundSet("ML_Pre_40", SoundTrack("40-ML"), SoundTrack("39-ML"))
        val tr = SoundSet("TR_Pre_0", SoundTrack("40-TR"), SoundTrack("39-TR"))
        val trNoise = SoundSet("TR_Pre_40", SoundTrack("40-TR"), SoundTrack("39-TR"))
        val run1 = SoundRun("1", arrayOf(mi, miNoise))
        val run2 = SoundRun("2", arrayOf(ml, mlNoise))
        val run3 = SoundRun("3", arrayOf(tr, trNoise))
        User(0, arrayOf(run1, run2, run3))
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)
        setSupportActionBar(toolbar)
        toolbar.visibility = View.VISIBLE
        if (savedInstanceState == null) {
            val welcomeFragment = InformationFragment.newInstance(
                    getString(R.string.welcome_text_title),
                    desc3 = getString(R.string.press_next_when_ready),
                    desc4 = UserDataManager.userID.toString(),
                    btnVisibility = View.INVISIBLE)
            loadFragment { replace(R.id.container, welcomeFragment) }

            launch(UI) {
                do {
                    val isGranted = suspendCoroutineOrReturn<Boolean> {
                        continuation = it
                        requestWritePermission()
                        COROUTINE_SUSPENDED
                    }
                } while (!isGranted)
                hasWritePermission = true
                welcomeFragment.update(Bundle().also {
                    it.putInt(InformationFragment.EXTRA_BTN_VISIBILITY, View.VISIBLE)
                })
            }
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        val frag = supportFragmentManager.findFragmentById(R.id.container)
        if (frag is InformationFragment){
            frag.update(Bundle().apply {
                putString(InformationFragment.EXTRA_DESC4, UserDataManager.userID.toString())
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        toolbar.inflateMenu(R.menu.main_menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?) = when (item?.itemId) {
        R.id.settings_menu -> {
            startActivity(Intent(this, SettingsActivity::class.java))
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    val getCurrentSoundSet: SoundSet
        get() {
            val runNumber = stepIndex / 2
            val setNumber = stepIndex % 2
            return soundCheckUser.soundRuns[runNumber].soundSets[setNumber]
        }

    override fun onClickNext(fragment: Fragment, args: Bundle) {
        if (fragment is InformationFragment) {
            supportActionBar?.hide()
        }
        if (fragment is SoundSelectFragment) {
            val volumeLevel = args.getInt(EXTRA_VOLUME_LEVEL)
            val currentSoundSet = getCurrentSoundSet
            val dirName = currentSoundSet.secondaryTrack.dirName

            if (stepIndex % 2 == 0) {
                prevSelectedVolume = volumeLevel
            } else {
                val savedVolume = Math.round((prevSelectedVolume + volumeLevel) / 2.0).toInt()
                log("PreAssessment volume $savedVolume for $dirName userID ${UserDataManager.userID} average between $prevSelectedVolume and $volumeLevel and noise = " + currentSoundSet.hasNoise)
                DataProvider.setDefaultVolume(dirName, currentSoundSet.hasNoise, savedVolume)
                prevSelectedVolume = 0
            }
            if (stepIndex < lastStep) {
                stepIndex++
            }
        }
        if (stepIndex < lastStep) {
            val soundSelectFragment = SoundSelectFragment()
            loadFragment { replace(R.id.container, soundSelectFragment) }
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
