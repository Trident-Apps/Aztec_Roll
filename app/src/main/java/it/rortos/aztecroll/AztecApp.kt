package it.rortos.aztecroll

import android.app.Application
import com.onesignal.OneSignal
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class AztecApp : Application() {
    override fun onCreate() {
        super.onCreate()
//        OneSignal.initWithContext(this)
//        OneSignal.setAppId(applicationContext.getString(R.string.onesignal_id))
    }
}