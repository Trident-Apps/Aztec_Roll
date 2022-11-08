package it.rortos.aztecroll.ui

import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import dagger.hilt.android.AndroidEntryPoint
import it.rortos.aztecroll.R
import it.rortos.aztecroll.ui.fragments.LoadingFragment
import it.rortos.aztecroll.ui.theme.AztecRollTheme
import it.rortos.aztecroll.ui.viewmodel.AztecViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HostActivity : AppCompatActivity(R.layout.host_activity_layout) {

//    private val viewModel: AztecViewModel by viewModels()
    private var url: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)

        lifecycleScope.launch(Dispatchers.IO) {

            adID = withContext(Dispatchers.Default){
                AdvertisingIdClient.getAdvertisingIdInfo(this@HostActivity.applicationContext).id.toString()
            }
//            delay(5000L)
            OneSignal.initWithContext(this@HostActivity.applicationContext)
            OneSignal.setAppId(applicationContext.getString(R.string.onesignal_id))
            OneSignal.setExternalUserId(adID)
            Log.d("myTag", "initialized gadId - $adID")
        }
//            delay(1000L)
//        url = with(viewModel.getAllFromDb()) {
//            this?.savedUrl ?: viewModel.createNewUrl(this@HostActivity)
//        }
        Log.d("myTag", "url after checking $url")

//            lifecycleScope.launch(Dispatchers.Main) {
//        with(Bundle()) {
//            this.putString("url", url)
//            val fragment = LoadingFragment()
//            fragment.arguments = this
//            supportFragmentManager.beginTransaction()
//                .add(R.id.fragmentContainerView, fragment)
//        }
//            }

    }

    companion object {
        lateinit var adID: String
    }
}