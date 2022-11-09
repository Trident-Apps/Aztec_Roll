package it.rortos.aztecroll.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import it.rortos.aztecroll.AztecApp.Companion.adID
import it.rortos.aztecroll.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@AndroidEntryPoint
class HostActivity : AppCompatActivity(R.layout.host_activity_layout) {

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {
            adID =
                AdvertisingIdClient.getAdvertisingIdInfo(applicationContext).id.toString()
            OneSignal.setExternalUserId(adID)
            Log.d("myTag", "initialized gadId - $adID")
        }
    }
}