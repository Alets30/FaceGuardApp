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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
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
import com.example.faceguardapp.movimientos.components.MovimientoScreen
import com.example.faceguardapp.areas.components.AreasListScreen
import com.example.faceguardapp.notificaciones.components.NotificacionesScreen
import com.example.faceguardapp.notificaciones.viewmodels.NotificacionViewModel
import com.example.faceguardapp.roles.components.RolesListScreen
import com.example.faceguardapp.routes.MainRoutes
import com.example.faceguardapp.routes.ScaffoldRoutes
import com.example.faceguardapp.stores.IsStaffStore
import com.example.faceguardapp.usuarios.components.EditProfileView
import com.example.faceguardapp.usuarios.components.PuertasListScreen
import com.example.faceguardapp.usuarios.components.ReconocimientoFacial
import com.example.faceguardapp.usuarios.components.UsuariosListScreen
import com.example.faceguardapp.zonas.components.ZonasListScreen
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

    val viewModel: NotificacionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val notificaciones by viewModel.notificaciones.observeAsState(emptyList())
    val notificacionesPendientes = notificaciones.count { !it.leida }

    val isStaffStore = IsStaffStore(context)
    val isStaff by isStaffStore.getIsStaff.collectAsState(initial = false)

    LaunchedEffect(Unit) {
        viewModel.cargarNotificaciones()
    }

    ModalNavigationDrawer(
        //gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                MyModalDrawer(
                    navigationController = scaffoldNavigationController, onCloseDrawer = {
                        scope.launch {
                            drawerState.close()
                        }
                    },
                    showLogoutDialog = {
                        logoutDialog = it
                    }, isStaff = isStaff
                )

            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                MyTopAppBarTarea(
                    onClickDrawer = {
                        scope.launch {
                            drawerState.open()
                        }
                    }, scaffoldNavigationController,
                    notificacionesPendientes
                )
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
                            NotificacionesScreen(descontarBadge = {

                            })

                        }
                        composable(ScaffoldRoutes.Roles.route) {
                            RolesListScreen()
                        }
                        composable(ScaffoldRoutes.Zonas.route) {
                            ZonasListScreen()
                        }
                        composable(ScaffoldRoutes.Areas.route) {
                            AreasListScreen()
                        }
                        composable(ScaffoldRoutes.Usuarios.route) {
                            UsuariosListScreen(navController = scaffoldNavigationController)
                        }
                        composable(ScaffoldRoutes.EditProfile.route) { backStackEntry ->
                            val profileId = backStackEntry.arguments?.getString("id")?.toInt()
                                ?: return@composable
                            EditProfileView(
                                profileId = profileId,
                                navController = scaffoldNavigationController
                            )
                        }
                        composable(ScaffoldRoutes.Movimientos.route) {
                            MovimientoScreen()
                        }
                        composable(ScaffoldRoutes.Puertas.route) {
                            PuertasListScreen(navController = scaffoldNavigationController)
                        }
                        composable(ScaffoldRoutes.ReconocimientoFacial.route) { backStackEntry ->
                            val puertaId = backStackEntry.arguments?.getString("puertaId")?.toInt()
                                ?: return@composable
                            val username =
                                backStackEntry.arguments?.getString("username") ?: return@composable
                            ReconocimientoFacial(
                                puertaId = puertaId,
                                username = username,
                                navController = scaffoldNavigationController
                            )
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

