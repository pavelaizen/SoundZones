package com.gm.soundzones.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.gm.soundzones.R
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.fragment.WelcomeMessageFragment
import com.gm.soundzones.listener.OnClickNextListener
import kotlinx.android.synthetic.main.welcome_layout.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.experimental.intrinsics.suspendCoroutineOrReturn
import kotlin.system.measureTimeMillis

//created by Pavel
class InitializationActivity : AppCompatActivity(), OnClickNextListener {
    private var continuation: Continuation<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_container)
        if (savedInstanceState == null) {
            val welcomeFragment = WelcomeMessageFragment.newInstance(
                    getString(R.string.intro_title),
                    getString(R.string.intro_desc1),
                    getString(R.string.intro_desc2),
                    getString(R.string.intro_ready),
                    btnVisibility = View.INVISIBLE)
            supportFragmentManager.beginTransaction().add(R.id.container, welcomeFragment)
                    .commit()
            launch(UI) {
                val jobExcel = launch(CommonPool) {
                    DataProvider.setup(assets.open(DataProvider.EXCEL_NAME))
                }
                do {
                    val isGranted = suspendCoroutineOrReturn<Boolean> {
                        continuation = it
                        requestWritePermission()
                        COROUTINE_SUSPENDED
                    }
                } while (!isGranted)

                jobExcel.join()
                btnNext.visibility = View.VISIBLE
            }
        }
    }

    override fun onClickNext(view: View) {
        startActivity(Intent(this, PreAssessmentActivity::class.java))
        finish()
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
