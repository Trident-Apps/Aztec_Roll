package it.rortos.aztecroll.ui

sealed class Screens(val route: String) {
    object LoadingScreen : Screens("loading_screen")
    object WebViewScreen : Screens("web_view_screen")
    object CloakScreen : Screens("cloak_screen")
    object VictoryScreen : Screens("victory_screen")

    fun passUrl(vararg url: String): String {
        return buildString {
            append(route)
            url.forEach { url ->
                append("/$url")
            }
        }
    }
}
