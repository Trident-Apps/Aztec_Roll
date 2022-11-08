package it.rortos.aztecroll.ui.viewmodel

import android.app.Activity
import android.app.Application
import android.content.Context
import android.provider.Settings
import androidx.core.net.toUri
import androidx.lifecycle.viewModelScope
import com.appsflyer.AppsFlyerLib
import dagger.hilt.android.lifecycle.HiltViewModel
import it.rortos.aztecroll.R
import it.rortos.aztecroll.data.Aztec
import it.rortos.aztecroll.di.repository.DatabaseRepInt
import it.rortos.aztecroll.ui.HostActivity
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

    private val fbFetcher = FbFetcher()
    private val appsDataFetcher = DataFetcher()
    private val tag = OneSignalTagSender()

    fun createNewUrl(app: Context): String {
        val createdUrl: String
        val deeplink = fbFetcher.getDLink(app)
        createdUrl = if (deeplink == "null") {
            val data = appsDataFetcher.getData(app)
            tag.makeTag("null", data)
            buildUrl("null", data, context.applicationContext)
        } else {
            tag.makeTag(deeplink, null)
            buildUrl(deeplink, null, context.applicationContext)
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
            appendQueryParameter(app.getString(R.string.gadid_key), HostActivity.adID)
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