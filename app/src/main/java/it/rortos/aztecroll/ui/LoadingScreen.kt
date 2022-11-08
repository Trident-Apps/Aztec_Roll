package it.rortos.aztecroll.ui

import android.app.Activity
import android.webkit.WebView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import it.rortos.aztecroll.R
import it.rortos.aztecroll.ui.viewmodel.AztecViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun LoadingScreen(
    navController: NavController,
    activity: Activity,
    url: String?,
    viewModel: AztecViewModel

) {
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

        if (viewModel.isDeviceSecured(activity)) {
            navController.navigate(Screens.WebViewScreen.route)
        } else {
            navController.navigate(Screens.CloakScreen.route)
        }
    }
}