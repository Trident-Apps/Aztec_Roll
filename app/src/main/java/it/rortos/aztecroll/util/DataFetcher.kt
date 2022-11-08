package it.rortos.aztecroll.util

import android.app.Application
import android.content.Context
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import it.rortos.aztecroll.R

class DataFetcher {
    private var _data: MutableMap<String, Any>? = null
    private val data get() = _data

    fun getData(app: Context): MutableMap<String, Any>? {
        AppsFlyerLib.getInstance().init(
            app.getString(R.string.af_id_key),
            object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(p0: MutableMap<String, Any>?) {
                    _data = p0
                }

                override fun onConversionDataFail(p0: String?) {
                    _data = null
                }

                override fun onAppOpenAttribution(p0: MutableMap<String, String>?) {
                    TODO("Not yet implemented")
                }

                override fun onAttributionFailure(p0: String?) {
                    TODO("Not yet implemented")
                }
            }, app.applicationContext
        )
        AppsFlyerLib.getInstance().start(app.applicationContext)
        return data
    }
}