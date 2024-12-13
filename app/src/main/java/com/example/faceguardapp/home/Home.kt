package com.example.faceguardapp.home

import AuthTokenStore
import ReconocimientoScreen
import UsernameStore
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.notificaciones.components.NotificacionesScreen
import com.example.faceguardapp.routes.MainRoutes
import com.example.faceguardapp.routes.ScaffoldRoutes
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navigationController: NavController) {
    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val context = LocalContext.current
    val tokenStore = AuthTokenStore(context)
    val usernameStore = UsernameStore(context)
    var logoutDialog by rememberSaveable { mutableStateOf(false) }
    val scaffoldNavigationController = rememberNavController()

    ModalNavigationDrawer(
        //gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                MyModalDrawer(navigationController = scaffoldNavigationController, onCloseDrawer = {
                    scope.launch {
                        drawerState.close()
                    }
                },
                    showLogoutDialog = {
                        logoutDialog = it
                    })

            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                MyTopAppBarTarea(onClickDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                }, scaffoldNavigationController)
            },
            modifier = Modifier.background(Color(0xfff8f8ec)),
            contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.ime)
        ) { contentPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(Constantes.WHITE))
                    .padding(contentPadding)
            )
            {
                Column(
                    Modifier
                        .background(Color.White)
                        .fillMaxWidth()
                ) {
                    NavHost(
                        navController = scaffoldNavigationController,
                        startDestination = ScaffoldRoutes.Reconocimiento.route
                    )
                    {
                        composable(ScaffoldRoutes.Reconocimiento.route) {
                            ReconocimientoScreen()
                        }
                        composable(ScaffoldRoutes.Notificaciones.route) {
                            NotificacionesScreen()
                        }
                    }
                }
            }
        }
    }
    if (logoutDialog) {
        LogOutDialog(onDismiss = {
            logoutDialog = false
        }, onConfirm = {
            logoutDialog = false
            scope.launch {
                tokenStore.saveAuthToken("")
                usernameStore.saveUsername("")
            }
            RetrofitClient.setToken("")
            navigationController.popBackStack(route = MainRoutes.Login.route, inclusive = false)
        })
    }
}

