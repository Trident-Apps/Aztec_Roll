package it.rortos.aztecroll.util

import android.content.Context
import android.util.Log
import com.facebook.applinks.AppLinkData
import it.rortos.aztecroll.AztecApp

class FbFetcher(app: Context) {
    private var _deepLink: String = ""
    private val deepLink get() = _deepLink
    val appppp = AztecApp()
    fun getDLink(app: Context): String {
        AppLinkData.fetchDeferredAppLinkData(app.applicationContext) {
            _deepLink = it?.targetUri.toString()
        }
        Log.d("mytag", "this is _deeplink - $_deepLink")
        Log.d("mytag", "this is deeplink - $deepLink")
        return deepLink
    }

//    val testDeep =
//        AppLinkData.fetchDeferredAppLinkData(app) {
//        it?.targetUri.toString()
//    }
//
//    val testDeep = "null"
}