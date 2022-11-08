package it.rortos.aztecroll.ui

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import it.rortos.aztecroll.ui.viewmodel.AztecViewModel

@Composable
fun Navigation(activity: Activity, url: String) {
    val navController = rememberNavController()
    val viewModel: AztecViewModel = viewModel()
    NavHost(navController = navController, startDestination = Screens.LoadingScreen.route) {
        composable(route = Screens.LoadingScreen.route,
            arguments = listOf(
                navArgument("url") {
                    type = NavType.StringType
                }
            )
        ) {
            LoadingScreen(
                navController = navController,
                activity = activity,
                viewModel = viewModel,
                url = url
            )
        }
        composable(route = Screens.WebViewScreen.route + "/{url}",
//            arguments = listOf(
//                navArgument("url") {
//                    type = NavType.StringType
//                    defaultValue = "smth"
//                }
//            )
        ) { backStackEntry ->
            WebViewScreen(
                url = backStackEntry.arguments?.getString("url"),
                navController = navController,
                viewModel = viewModel
            )
        }
        composable(Screens.CloakScreen.route) {
            GameScreen()
        }

        composable(Screens.VictoryScreen.route) {
            VictoryScreen()
        }
    }
}