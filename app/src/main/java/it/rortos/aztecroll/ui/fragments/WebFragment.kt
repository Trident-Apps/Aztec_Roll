package it.rortos.aztecroll.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.*
import androidx.activity.OnBackPressedCallback
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.rortos.aztecroll.R
import it.rortos.aztecroll.data.Aztec
import it.rortos.aztecroll.ui.viewmodel.AztecViewModel
import it.rortos.aztecroll.util.Const
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WebFragment : Fragment() {
    lateinit var webView: WebView
    private var redirected = true
    private var valueCallback: ValueCallback<Array<Uri?>>? = null
    private val viewModel: AztecViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(requireContext()).apply {
            setContent {
                AndroidView(factory = {
                    WebView(it).apply {
                        layoutParams = ViewGroup.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT
                        )
                        webView = this
                        webView.webViewClient = object : WebViewClient() {
                            override fun onReceivedError(
                                view: WebView?,
                                request: WebResourceRequest?,
                                error: WebResourceError?
                            ) {
                                super.onReceivedError(view, request, error)
                            }

                            override fun shouldOverrideUrlLoading(
                                view: WebView?,
                                request: WebResourceRequest?
                            ): Boolean {
                                return super.shouldOverrideUrlLoading(view, request)
                            }

                            override fun onPageStarted(
                                view: WebView?,
                                url: String?,
                                favicon: Bitmap?
                            ) {
                                super.onPageStarted(view, url, favicon)
                                redirected = false
                            }

                            override fun onPageFinished(view: WebView?, url: String?) {
                                super.onPageFinished(view, url)
                                CookieManager.getInstance().flush()
                                if (!redirected) {
                                    if (url == "https://firewolf.site/") {
                                        findNavController().navigate(R.id.cloakFragment)
                                    } else {
                                        lifecycleScope.launch {
                                            if (viewModel.getAllFromDb()?.savedUrl == null) {
                                                viewModel.insertIntoDb(Aztec(1, url!!, true))
                                                Log.d("myTag", url)
                                            }

                                        }
                                    }
                                }
                            }
                        }
                        webView.loadUrl(arguments?.getString("url", "") ?: "null")
//                        loadUrl("https://ru.imgbb.com/")
                        CookieManager.getInstance().setAcceptCookie(true)
                        CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
                        webView.settings.javaScriptEnabled = true
                        webView.settings.userAgentString.replace("wv", "")
                        webView.settings.domStorageEnabled = true
                        webView.settings.loadWithOverviewMode = false
                        webView.webChromeClient = object : WebChromeClient() {
                            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                                super.onProgressChanged(view, newProgress)
                            }

                            override fun onShowFileChooser(
                                webView: WebView?,
                                filePathCallback: ValueCallback<Array<Uri?>>?,
                                fileChooserParams: FileChooserParams?
                            ): Boolean {
                                valueCallback = filePathCallback
                                chooseImageIfNeeded()
                                return true
                            }

                            override fun onCreateWindow(
                                view: WebView?,
                                isDialog: Boolean,
                                isUserGesture: Boolean,
                                resultMsg: Message?
                            ): Boolean {
                                val newWebView = webView
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
                })
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (webView.canGoBack()) {
                        webView.goBack()
                    }
                }

            })
    }

    private fun chooseImageIfNeeded() {
        with(Intent(Intent.ACTION_GET_CONTENT)) {
            this.addCategory(Intent.CATEGORY_OPENABLE)
            this.type = Const.INTENT_TYPE
            startActivityForResult(
                Intent.createChooser(this, Const.CHOOSER_TYPE), 1
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_CANCELED) {
            valueCallback?.onReceiveValue(null)
            return
        } else if (resultCode == Activity.RESULT_OK) {
            if (valueCallback == null) return

            valueCallback!!.onReceiveValue(
                WebChromeClient.FileChooserParams.parseResult(
                    resultCode,
                    data
                )
            )
            valueCallback = null
        }
    }
}