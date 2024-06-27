package com.farhan.tugasakhir.app

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.farhan.tugasakhir.data.model.Standard
import com.farhan.tugasakhir.viewmodel.AddScreenViewModel
import com.farhan.tugasakhir.viewmodel.CheckScreenViewModel
import com.farhan.tugasakhir.viewmodel.HomeViewModel
import com.farhan.tugasakhir.viewmodel.ListScreenViewModel
import com.farhan.tugasakhir.navigation.Screen
import com.farhan.tugasakhir.screen.AddScreen
import com.farhan.tugasakhir.screen.ArtikelListScreen
import com.farhan.tugasakhir.screen.CheckScreen
import com.farhan.tugasakhir.screen.DetailScreen
import com.farhan.tugasakhir.screen.HomeScreen
import com.farhan.tugasakhir.screen.ListScreen
import com.farhan.tugasakhir.screen.LoginScreen
import com.farhan.tugasakhir.screen.MapScreen
import com.farhan.tugasakhir.screen.ProfileScreen
import com.farhan.tugasakhir.screen.RegisterScreen
import com.farhan.tugasakhir.screen.WelcomeScreen

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun StuntCheckApp(navController: NavHostController = rememberNavController(), maleStandards: List<Standard>, femaleStandards: List<Standard>) {
    val homeViewModel: HomeViewModel = viewModel()
    val addScreenViewModel: AddScreenViewModel = viewModel()
    val listScreenViewModel: ListScreenViewModel = viewModel()
    val checkScreenViewModel : CheckScreenViewModel = viewModel()
    homeViewModel.checkForActiveSession()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White
    ) {
        NavHost(navController, startDestination = getStartDestination(homeViewModel)) {
            composable(Screen.WelcomeScreen.route) {
                WelcomeScreen(navController = navController)
            }
            composable(Screen.LoginScreen.route) {
                LoginScreen(navController = navController)
            }
            composable(Screen.RegisterScreen.route) {
                RegisterScreen(navController = navController)
            }
            composable(route = Screen.HomeScreen.route) {
                HomeScreen(
                    navigateToDetail = { artikelId, hasTable, hasPdf ->
                        navController.navigate(Screen.DetailScreen.createRoute(artikelId, hasTable, hasPdf))
                    },
                    navController = navController
                )
            }
            composable(
                route = Screen.DetailScreen.route,
                arguments = listOf(
                    navArgument("artikelId") { type = NavType.IntType },
                    navArgument("hasTable") { type = NavType.BoolType },
                    navArgument("hasPdf") { type = NavType.BoolType }
                )
            ) {
                val id = it.arguments?.getInt("artikelId") ?: -1
                val hasTable = it.arguments?.getBoolean("hasTable") ?: false
                val hasPdf = it.arguments?.getBoolean("hasPdf") ?: false
                DetailScreen(
                    artikelId = id,
                    hasTable = hasTable,
                    hasPdf = hasPdf,
                    navigateBack = {
                        navController.navigateUp()
                    },
                    maleStandards = maleStandards,
                    femaleStandards = femaleStandards
                )
            }
            composable(Screen.MapScreen.route){
                MapScreen(navController)
            }
            composable(Screen.CheckScreen.route){
                CheckScreen(checkScreenViewModel, navigateBack = {
                    navController.navigateUp()
                })
            }
            composable(Screen.ListScreen.route){
                ListScreen(listScreenViewModel,navigateBack = {
                    navController.navigateUp()
                })
            }
            composable(Screen.AddScreen.route){
                AddScreen(addScreenViewModel,navigateBack = {
                    navController.navigateUp()
                })
            }
            composable(Screen.ArtikelListScreen.route){
                ArtikelListScreen(navigateToDetail = { artikelId, hasTable, hasPdf ->
                    navController.navigate(Screen.DetailScreen.createRoute(artikelId, hasTable, hasPdf))
                },navigateBack = {
                    navController.navigateUp()
                })
            }
            composable(Screen.ProfileScreen.route){
                ProfileScreen(navigateBack = {
                    navController.navigateUp()
                },navController = navController)
            }
        }
    }
}


@Composable
private fun getStartDestination(homeViewModel: HomeViewModel): String {
    return if (homeViewModel.isUserLoggedIn.value == true) {
        Screen.HomeScreen.route
    } else {
        Screen.WelcomeScreen.route
    }
}
