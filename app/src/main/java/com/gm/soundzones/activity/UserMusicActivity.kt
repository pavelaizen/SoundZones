    package com.gm.soundzones.activity

    import android.os.Bundle
    import android.support.v7.app.AppCompatActivity
    import com.gm.soundzones.R

    /**
 * Created by Pavel Aizendorf on 26/09/2017.
 */
class UserMusicActivity:AppCompatActivity() {
        var userID : Int = 1
        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_container)
            if (savedInstanceState == null){

            }
        }
}