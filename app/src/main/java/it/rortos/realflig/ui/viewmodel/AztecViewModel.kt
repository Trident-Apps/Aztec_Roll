package it.rortos.realflig.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Settings
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import dagger.hilt.android.lifecycle.HiltViewModel
import it.rortos.realflig.R
import it.rortos.realflig.AztecApp.Companion.adID
import it.rortos.realflig.data.Aztec
import it.rortos.realflig.di.repository.DatabaseRepInt
import it.rortos.realflig.util.Const
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class AztecViewModel @Inject constructor(private val repository: DatabaseRepInt, app: Application) :
    BaseVIewModel(app) {

    fun collectDeepFlow(activity: Context): Flow<String> = callbackFlow {
        AppLinkData.fetchDeferredAppLinkData(activity) {
            trySend(it?.targetUri.toString())
//            trySend(mockDeep)
//            trySend("null")
        }
        awaitClose()
    }

    fun collectAppsData(activity: Context): Flow<MutableMap<String, Any>?> = callbackFlow {
        AppsFlyerLib.getInstance()
            .init(activity.getString(R.string.apps_dev_key), object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    Log.d("YYY", "onConversionDataSuccess")
                    trySend(p0)
                }

                override fun onConversionDataFail(p0: String?) {
                    Log.d("YYY", "onConversionDataFail")
                    Log.d("YYY", p0 ?: "asdasdasd")
                    trySend(null)
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    TODO("Not yet implemented")
                }

                override fun onAttributionFailure(p0: String?) {
                    TODO("Not yet implemented")
                }
            }, activity)
        AppsFlyerLib.getInstance().start(activity)
        awaitClose()
    }

    val mockDeep = "myapp://test1/test2/test3/test4/test5"
    val mockData: MutableMap<String, Any>? = mutableMapOf(
        "af_status" to "Non-organic",
        "media_source" to "testSource",
        "campaign" to "test1_test2_test3_test4_test5",
        "adset" to "testAdset",
        "adset_id" to "testAdsetId",
        "campaign_id" to "testCampaignId",
        "orig_cost" to "1.22",
        "af_site_id" to "testSiteID",
        "adgroup" to "testAdgroup"
    )

    fun buildUrl(
        deeplink: String,
        data: MutableMap<String, Any>?,
        activity: Activity
    ): String {
        val afId: String
        val source: String

        if (deeplink == "null") {
            source = data?.get(Const.MEDIA_SOURCE).toString()
            afId = AppsFlyerLib.getInstance().getAppsFlyerUID(activity).toString()
        } else {
            source = "deeplink"
            afId = "null"
        }
        val url: String = activity.getString(R.string.url_base).toUri().buildUpon().apply {
            appendQueryParameter(
                activity.getString(R.string.secure_get_parametr),
                activity.getString(R.string.secure_key)
            )
            appendQueryParameter(
                activity.getString(R.string.dev_tmz_key),
                TimeZone.getDefault().id
            )
            appendQueryParameter(activity.getString(R.string.gadid_key), adID)
            appendQueryParameter(activity.getString(R.string.deeplink_key), deeplink)
            appendQueryParameter(activity.getString(R.string.source_key), source)
            appendQueryParameter(
                activity.getString(R.string.af_id_key),
                afId
            )
            appendQueryParameter(
                activity.getString(R.string.adset_id_key),
                data?.get(Const.ADSET_ID).toString()
            )
            appendQueryParameter(
                activity.getString(R.string.campaign_id_key),
                data?.get(Const.CAMPAIGN_ID).toString()
            )
            appendQueryParameter(
                activity.getString(R.string.app_campaign_key),
                data?.get(Const.CAMPAIGN).toString()
            )
            appendQueryParameter(
                activity.getString(R.string.adset_key),
                data?.get(Const.ADSET).toString()
            )
            appendQueryParameter(
                activity.getString(R.string.adgroup_key),
                data?.get(Const.ADGROUP).toString()
            )
            appendQueryParameter(
                activity.getString(R.string.orig_cost_key),
                data?.get(Const.ORIG_COST).toString()
            )
            appendQueryParameter(
                activity.getString(R.string.af_siteid_key),
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

    fun getAllFromDb() = repository.getUser()

    fun insertIntoDb(aztec: Aztec) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertUser(aztec)
    }
}