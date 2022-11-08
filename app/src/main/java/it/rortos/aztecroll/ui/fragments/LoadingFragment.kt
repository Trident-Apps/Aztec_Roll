package it.rortos.aztecroll.ui.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.res.painterResource
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import it.rortos.aztecroll.R
import it.rortos.aztecroll.ui.viewmodel.AztecViewModel

@AndroidEntryPoint
class LoadingFragment : Fragment() {
    private val viewModel: AztecViewModel by viewModels()
    private lateinit var args: String
    lateinit var url: String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d("myTag", " started Loading Fragment")
        args = arguments?.getString("url", "") ?: ""
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

                    url = with(viewModel.getAllFromDb()) {
                        this?.savedUrl ?: viewModel.createNewUrl(requireContext())
                    }
                    if (!viewModel.isDeviceSecured(requireActivity())) {
                        navigateToCloak()
                        Log.d("myTag", "navigated to cloak")
                    } else {
                        navigateToWeb(args)
                        Log.d("myTag", " navigated to web with this url: $args")
                    }
                }
            }
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