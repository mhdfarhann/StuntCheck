package com.farhan.tugasakhir.navigation


sealed class Screen(val route: String) {
    object WelcomeScreen : Screen("welcome_screen")
    object LoginScreen : Screen("login_screen")
    object RegisterScreen : Screen("register_screen")
    object HomeScreen : Screen("home_screen")
    object MapScreen : Screen("map_screen")
    object CheckScreen: Screen("check_screen")
    object ListScreen: Screen("list_screen")
    object AddScreen: Screen("add_screen")
    object ProfileScreen: Screen("profile_screen")
    object ArtikelListScreen : Screen("artikelList_screen")
    object DetailScreen : Screen("home_screen/{artikelId}/{hasTable}/{hasPdf}") {
        fun createRoute(artikelId: Int, hasTable: Boolean, hasPdf: Boolean) = "home_screen/$artikelId/$hasTable/$hasPdf"
    }
}

