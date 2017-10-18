package com.gm.soundzones.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.text.Html
import android.view.View
import com.gm.soundzones.R
import com.gm.soundzones.excel.DataProvider
import com.gm.soundzones.fragment.InformationFragment
import com.gm.soundzones.listener.OnClickNextListener
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

//created by Pavel
class PreparationActivity : AppCompatActivity(), OnClickNextListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_toolbar_container)
        if (savedInstanceState == null) {
            val welcomeFragment = InformationFragment.newInstance(
                    getString(R.string.intro_title),
                    Html.fromHtml(getString(R.string.intro_desc1)).toString(),
                    getString(R.string.intro_desc2),
                    getString(R.string.intro_ready),
                    btnVisibility = View.INVISIBLE)
            supportFragmentManager.beginTransaction().add(R.id.container, welcomeFragment)
                    .commitNow()

            launch(UI){
                val job = launch(CommonPool){
                    DataProvider.setup(applicationContext)
                }

                job.join()
                welcomeFragment.update(Bundle().also {
                    it.putInt(InformationFragment.EXTRA_BTN_VISIBILITY, View.VISIBLE)
                })
            }
        }
    }

    override fun onClickNext(fragment: Fragment, args: Bundle) {
        startActivity(Intent(this, UserMusicActivity::class.java))
        finish()
    }




}
