package it.rortos.aztecroll.util

import android.content.Context
import android.util.Log
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import it.rortos.aztecroll.R

class DataFetcher() {
    fun getData(app: Context, callback: (MutableMap<String, Any>?) -> Unit) {
        AppsFlyerLib.getInstance()
            .init(app.getString(R.string.af_id_key), object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    callback(p0)
                    Log.d("myTag", "apps success")
                }

                override fun onConversionDataFail(p0: String?) {
                    callback(null)
                    Log.d("myTag", "apps fail")
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    TODO("Not yet implemented")
                }

                override fun onAttributionFailure(p0: String?) {
                    TODO("Not yet implemented")
                }
            }, app.applicationContext)
        AppsFlyerLib.getInstance().start(app.applicationContext)
    }


    val mockData: MutableMap<String, Any> = mutableMapOf(
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

}