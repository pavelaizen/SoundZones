package com.gm.soundzones

import android.content.Context
import android.content.pm.PackageManager

/**
 * Created by titan on 19-Nov-17.
 */

class Utils{
    companion object {
        fun getAppVersion(context:Context):String{
            try {
                val pInfo = context.getPackageManager().getPackageInfo(context.packageName, 0)
                val versionName = pInfo.versionName
                val versionCode = pInfo.versionCode
                return "versionName $versionName, versionCode $versionCode"
            } catch (e: PackageManager.NameNotFoundException) {
                e.printStackTrace()
            }
            return ""
        }
    }

}
