package it.rortos.aztecroll.ui

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Message
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import it.rortos.aztecroll.data.Aztec
import it.rortos.aztecroll.ui.viewmodel.AztecViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

private var redirected: Boolean = false
private var message: ValueCallback<Array<Uri?>>? = null

@Composable
fun WebViewScreen(
    navController: NavController,
    url: String?,
    viewModel: AztecViewModel
) {
    var webView: WebView? = null
    AndroidView(factory = { context ->
        WebView(context).apply {
            webView = this
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                    super.onPageStarted(view, url, favicon)
                    redirected = true
                }

                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    if (url == "https://firewolf.site/") {
                        navController.navigate(Screens.CloakScreen.route)
                    } else {
                        GlobalScope.launch {
                            viewModel.getAllFromDb()?.savedUrl?.let {
                                viewModel.insertIntoDb(Aztec(1, url!!, true))
                            }
                        }
                    }
                }
            }
            loadUrl(url ?: "")
            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
            settings.javaScriptEnabled = true
            settings.userAgentString.replace("wv", "")
            settings.domStorageEnabled = true
            settings.loadWithOverviewMode = false
            webChromeClient = object : WebChromeClient() {
                override fun onProgressChanged(view: WebView?, newProgress: Int) {
                    super.onProgressChanged(view, newProgress)
                }

                override fun onShowFileChooser(
                    webView: WebView?,
                    filePathCallback: ValueCallback<Array<Uri?>>?,
                    fileChooserParams: FileChooserParams?
                ): Boolean {
                    message = filePathCallback
                    chooseImageIfNeeded()
                    return true
                }

                override fun onCreateWindow(
                    view: WebView?,
                    isDialog: Boolean,
                    isUserGesture: Boolean,
                    resultMsg: Message?
                ): Boolean {
                    val newWebView = webView!!
                    newWebView.webChromeClient = this
                    newWebView.settings.javaScriptEnabled = true
                    newWebView.settings.javaScriptCanOpenWindowsAutomatically = true
                    newWebView.settings.domStorageEnabled = true
                    newWebView.settings.setSupportMultipleWindows(true)
                    val transport = resultMsg?.obj as WebView.WebViewTransport
                    transport.webView = newWebView
                    newWebView.webViewClient = object : WebViewClient() {
                        override fun shouldOverrideUrlLoading(
                            view: WebView?,
                            request: WebResourceRequest?
                        ): Boolean {
                            view?.loadUrl(url ?: "")
                            redirected = true
                            return true
                        }
                    }
                    return true
                }
            }
        }
    }, update = {
        webView?.loadUrl(url ?: "")
    })

    BackHandler(enabled = true) {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        }
    }
}

private fun chooseImageIfNeeded() {
    val intent = Intent(Intent.ACTION_GET_CONTENT)
    intent.addCategory((Intent.CATEGORY_OPENABLE))
    intent.type = "image/*"
}