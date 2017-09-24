package com.earlyense.soundzones.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import com.earlyense.soundzones.R
import com.earlyense.soundzones.excel.DataProvider
import com.earlyense.soundzones.listener.OnClickNextListener
import kotlinx.android.synthetic.main.initialization_layout.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import kotlin.coroutines.experimental.Continuation
import kotlin.coroutines.experimental.intrinsics.COROUTINE_SUSPENDED
import kotlin.coroutines.experimental.intrinsics.suspendCoroutineOrReturn
//created by Pavel
class InitializationActivity : AppCompatActivity(), OnClickNextListener {
    private var continuation: Continuation<Boolean>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.initialization_layout)
        if (savedInstanceState == null) {
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
