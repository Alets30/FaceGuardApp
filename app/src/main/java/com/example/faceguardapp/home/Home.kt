package com.example.faceguardapp.home

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.union
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoorBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.faceguardapp.notificaciones.viewmodels.NotificacionViewModel
import com.example.faceguardapp.routes.MainRoutes
import com.example.faceguardapp.stores.StoreDarkMode
import kotlinx.coroutines.launch


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeScreen(navigationController: NavController) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)

    ModalNavigationDrawer(
        //gesturesEnabled = false,
        drawerContent = {
            ModalDrawerSheet {
                MyModalDrawer(navigationController) {
                    scope.launch {
                        drawerState.close()
                    }
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                MyTopAppBarTarea(onClickIcon = {
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            "Se ha dado un click en el botón: $it"
                        )
                    }
                }, onClickDrawer = {
                    scope.launch {
                        drawerState.open()
                    }
                },navigationController)
            },
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState) { data ->
                    MySnackbar(data)
                }
            },
            bottomBar = {
                MyBottomNavigationTarea()
            },
            floatingActionButton = {
                FABTarea() {
                    scope.launch {
                        snackbarHostState.showSnackbar("Agregar Info")
                    }
                }
            },
            floatingActionButtonPosition = FabPosition.Center,
            modifier = Modifier.background(Color(0xfff8f8ec)),
            contentWindowInsets = WindowInsets.systemBars.union(WindowInsets.ime)
        ) { contentPadding ->
            Box(
                modifier = Modifier
                    .height(50.dp)
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(contentPadding)
            )
            {

            }
        }
    }
}

@Composable
fun MyModalDrawer(navigationController: NavController, onCloseDrawer: () -> Unit ) {
    val context = LocalContext.current
    val darkModeStore = StoreDarkMode(context)
    val darkMode = darkModeStore.getDarkMode.collectAsState(initial = false)
    val scope = rememberCoroutineScope()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xff0a5483))
            .padding(8.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
            IconButton(onClick = { onCloseDrawer() }, modifier = Modifier.padding(16.dp)) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
        Text(
            text = "FaceGuard",
            fontSize = 30.sp,
            fontWeight = FontWeight.ExtraBold,
            color = Color.White,
            modifier = Modifier
                .padding(bottom = 8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
        HorizontalDivider(thickness = 1.dp)
        Text(
            text = "ACCESO RÁPIDO",
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier.padding(8.dp),
        )
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "Dashboard",
                    tint = Color.White
                )
                Text(
                    text = "Dashboard",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Usuarios",
                    tint = Color.White
                )
                Text(
                    text = "Usuarios",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.LockPerson,
                    contentDescription = "Roles",
                    tint = Color.White
                )
                Text(
                    text = "Roles",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.DoorBack,
                    contentDescription = "Puertas",
                    tint = Color.White
                )
                Text(
                    text = "Puertas",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.AddLocation,
                    contentDescription = "Areas",
                    tint = Color.White
                )
                Text(
                    text = "Areas",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Repeat,
                    contentDescription = "Movimientos",
                    tint = Color.White
                )
                Text(
                    text = "Movimientos",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        HorizontalDivider(thickness = 1.dp)
        Text(
            text = "CUENTA",
            fontSize = 20.sp,
            fontWeight = FontWeight.Light,
            color = Color.White,
            modifier = Modifier.padding(8.dp),
        )
        TextButton(onClick = { navigationController.navigate(MainRoutes.Notificaciones.route)
            onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notificaciones",
                    tint = Color.White
                )
                Text(
                    text = "Notificaciones",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuraciones",
                    tint = Color.White
                )
                Text(
                    text = "Configuraciones",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        TextButton(onClick = { onCloseDrawer() }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Default.Logout,
                    contentDescription = "Log Out",
                    tint = Color.White
                )
                Text(
                    text = "Log Out",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Switch(
                checked = darkMode.value?:false, onCheckedChange = { isChecked ->
                    scope.launch {
                        darkModeStore.saveDarkMode(isChecked)
                    }
                },
            )
        }
    }
}

@Composable
fun FABTarea(onClickIcon: () -> Unit) {
    FloatingActionButton(
        onClick = { onClickIcon() },
        containerColor = Color(0xff0a5483),
        contentColor = Color(0xffaedd2b),
    ) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
    }
}

@Composable
fun MyBottomNavigationTarea() {
    var myIndex by rememberSaveable { mutableStateOf(0) }
    NavigationBar(containerColor = Color(0xff0a5483)) {
        NavigationBarItem(
            selected = myIndex == 0,
            onClick = { myIndex = 0 },
            icon = {
                Icon(imageVector = Icons.Default.Home, contentDescription = "Home")
            },
            label = { Text(text = "Inicio", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xff0a5483),
                unselectedIconColor = Color(0xffaedd2b),
                selectedTextColor = Color.White,
                unselectedTextColor = Color.LightGray,
            )
        )
        NavigationBarItem(
            selected = myIndex == 1,
            onClick = { myIndex = 1 },
            icon = {
                Icon(imageVector = Icons.Default.Favorite, contentDescription = "Favorite")
            },
            label = { Text(text = "Favorite", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xff0a5483),
                unselectedIconColor = Color(0xffaedd2b),
                selectedTextColor = Color.White,
                unselectedTextColor = Color.LightGray,
            )
        )
        NavigationBarItem(
            selected = myIndex == 2,
            onClick = { myIndex = 2 },
            icon = {
                Icon(imageVector = Icons.Default.Person, contentDescription = "Person")
            },
            label = { Text(text = "Inicio", color = Color.White) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color(0xff0a5483),
                unselectedIconColor = Color(0xffaedd2b),
                selectedTextColor = Color.White,
                unselectedTextColor = Color.LightGray,
            )
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarTarea(onClickIcon: (String) -> Unit, onClickDrawer: () -> Unit, navigationController: NavController) {
    val viewModel: NotificacionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val notificaciones by viewModel.notificaciones.observeAsState(emptyList())
    val notificacionesPendientes = notificaciones.count { !it.leida }

    LaunchedEffect(Unit) {
        viewModel.cargarNotificaciones()
    }


    TopAppBar(
        title = {
            Text(
                "FaceGuard",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
            )
        },
        navigationIcon = {
            IconButton(onClick = {
                //onClickIcon("Menu")
                onClickDrawer()
            }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            BadgedBox(
                badge = {
                    if (notificacionesPendientes > 0) {
                        Badge(
                            content = {
                                Text(
                                    text = notificacionesPendientes.toString(),
                                    color = Color.White,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            containerColor = Color.Red,
                            modifier = Modifier
                                .padding(0.dp)
                        )
                    }
                }
            ) {
                IconButton(onClick = { navigationController.navigate(MainRoutes.Notificaciones.route) }) {
                    Icon(
                        imageVector = Icons.Default.Notifications,
                        contentDescription = "Notificacion",
                        tint = Color(0xffaedd2b),
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color(0xff0a5483),
            scrolledContainerColor = Color(0xff0a5483),
            navigationIconContentColor = Color(0xffaedd2b),
            titleContentColor = Color.White,
            actionIconContentColor = Color(0xffaedd2b)

        )
    )
}

@Composable
fun MySnackbar(snackbarData: SnackbarData) {
    Snackbar(
        modifier = Modifier.padding(8.dp),
        containerColor = Color(0xFF066699),
        contentColor = Color.White,
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = "Info",
                tint = Color(0xffaedd2b),
                modifier = Modifier.padding(end = 8.dp)
            )
            Text(text = snackbarData.visuals.message)

            snackbarData.visuals.actionLabel?.let { actionLabel ->
                TextButton(onClick = { snackbarData.performAction() }) {
                    Text(actionLabel, color = Color.Yellow)
                }
            }
        }
    }
}