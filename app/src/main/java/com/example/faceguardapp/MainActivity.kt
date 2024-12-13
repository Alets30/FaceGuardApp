package com.example.faceguardapp

import FotografiaScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faceguardapp.home.HomeScreen
import com.example.faceguardapp.login.LoginScreen
import com.example.faceguardapp.notificaciones.components.NotificacionesScreen
import com.example.faceguardapp.routes.MainRoutes
import com.example.faceguardapp.stores.StoreDarkMode
import com.example.faceguardapp.ui.theme.FaceGuardAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val darkModeStore = StoreDarkMode(this)
            val darkMode = darkModeStore.getDarkMode.collectAsState(initial = false)
            FaceGuardAppTheme(darkTheme = darkMode.value ?: false) {
                val navigationController = rememberNavController()
                Column(
                    Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    NavHost(
                        navController = navigationController,
                        startDestination = MainRoutes.Login.route
                    )
                    {
                        composable(MainRoutes.Login.route) {
                            LoginScreen(modifier = Modifier, navigationController)
                        }
                        composable(MainRoutes.Home.route) {
                            HomeScreen(navigationController)
                        }
                        composable(MainRoutes.Notificaciones.route) {
                            NotificacionesScreen()
                        }
                        composable(MainRoutes.Reconocimiento.route) {
                            FotografiaScreen()
                        }
//                        composable(
//                            Routes.Screen4.route,
//                            arguments = listOf(navArgument("age") {
//                                type = NavType.IntType
//                            },
//                                navArgument("name") {
//                                    type = NavType.StringType
//                                })
//                        )
//                        { backStackEntry ->
//                            Screen4N(
//                                navigationController,
//                                backStackEntry.arguments?.getInt("age") ?: 0,
//                                backStackEntry.arguments?.getString("name") ?: ""
//                            )
//                        }
                    }
                }
            }
        }
    }
}