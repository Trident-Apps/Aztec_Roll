package it.rortos.aztecroll.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import dagger.hilt.android.lifecycle.HiltViewModel
import it.rortos.aztecroll.AztecApp.Companion.adID
import it.rortos.aztecroll.R
import it.rortos.aztecroll.data.Aztec
import it.rortos.aztecroll.di.repository.DatabaseRepInt
import it.rortos.aztecroll.util.Const
import it.rortos.aztecroll.util.DataFetcher
import it.rortos.aztecroll.util.FbFetcher
import it.rortos.aztecroll.util.OneSignalTagSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AztecViewModel @Inject constructor(private val repository: DatabaseRepInt, app: Application) :
    BaseVIewModel(app) {

    private val fbFetcher = FbFetcher(app)
    private val appsDataFetcher = DataFetcher()
    private val tag = OneSignalTagSender()
    private var _data: MutableMap<String, Any>? = null
    private val data get() = _data
    private var deeplink: String? = null
    private lateinit var createdUrl: String

    fun createNewUrl(app: Context): String {
        AppLinkData.fetchDeferredAppLinkData(app.applicationContext) {
//            deeplink = it?.targetUri.toString()
            deeplink = "myapp://test1/test2/test3/test4/test5"
        }
        Log.d("mytag", "this is deeplink - $deeplink")

        createdUrl = if (deeplink == "null") {
            appsDataFetcher.getData(app.applicationContext) {
                _data = it
            }
            Log.d("myTag", "the returned data is ${_data.toString()}")

            tag.makeTag("null", data)
            buildUrl("null", data, context.applicationContext)
        } else {
            tag.makeTag(deeplink!!, null)
            buildUrl(deeplink!!, null, context.applicationContext)
        }
        return createdUrl
    }

    private fun buildUrl(
        deeplink: String,
        data: MutableMap<String, Any>?,
        app: Context
    ): String {
        val afId: String
        val source: String

        if (deeplink == "null") {
            source = data?.get(Const.MEDIA_SOURCE).toString()
            afId = AppsFlyerLib.getInstance().getAppsFlyerUID(app).toString()
        } else {
            source = "deeplink"
            afId = "null"
        }

        val url: String = app.getString(R.string.url_base).toUri().buildUpon().apply {
            appendQueryParameter(
                app.getString(R.string.secure_get_parametr),
                app.getString(R.string.secure_key)
            )
            appendQueryParameter(
                app.getString(R.string.dev_tmz_key),
                TimeZone.getDefault().id
            )

            appendQueryParameter(app.getString(R.string.gadid_key), adID)
            appendQueryParameter(app.getString(R.string.deeplink_key), deeplink)
            appendQueryParameter(app.getString(R.string.secure_key), source)
            appendQueryParameter(
                app.getString(R.string.af_id_key),
                afId
            )
            appendQueryParameter(
                app.getString(R.string.adset_id_key),
                data?.get(Const.ADSET_ID).toString()
            )
            appendQueryParameter(
                app.getString(R.string.campaign_id_key),
                data?.get(Const.CAMPAIGN_ID).toString()
            )
            appendQueryParameter(
                app.getString(R.string.app_campaign_key),
                data?.get(Const.CAMPAIGN).toString()
            )
            appendQueryParameter(
                app.getString(R.string.adset_key),
                data?.get(Const.ADSET).toString()
            )
            appendQueryParameter(
                app.getString(R.string.adgroup_key),
                data?.get(Const.ADGROUP).toString()
            )
            appendQueryParameter(
                app.getString(R.string.orig_cost_key),
                data?.get(Const.ORIG_COST).toString()
            )
            appendQueryParameter(
                app.getString(R.string.af_siteid_key),
                data?.get(Const.AF_SITEID).toString()
            )

        }.toString()

        return url
    }


    fun isDeviceSecured(activity: Activity): Boolean {
        return Settings.Global.getString(
            activity.contentResolver,
            Settings.Global.ADB_ENABLED
        ) == "1"
    }

    fun getAllFromDb(): Aztec? = repository.getUser()

    fun insertIntoDb(aztec: Aztec) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertUser(aztec)
    }
}