package it.rortos.aztecroll.util

import android.app.Application
import android.content.Context
import com.facebook.applinks.AppLinkData

class FbFetcher {
    private var _deepLink: String = ""
    private val deepLink get() = _deepLink
    fun getDLink(app: Context): String {
        AppLinkData.fetchDeferredAppLinkData(app.applicationContext) {
            _deepLink = it?.targetUri.toString()
        }
        return deepLink
    }
}