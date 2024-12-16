package com.example.faceguardapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faceguardapp.home.HomeScreen
import com.example.faceguardapp.login.LoginScreen
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
                    }
                }
            }
        }
    }
}