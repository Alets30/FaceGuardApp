package com.example.faceguardapp.home

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.faceguardapp.notificaciones.viewmodels.NotificacionViewModel
import com.example.faceguardapp.routes.MainRoutes
import com.example.faceguardapp.routes.ScaffoldRoutes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBarTarea(
    onClickDrawer: () -> Unit,
    navigationController: NavController,
    viewModel: NotificacionViewModel,
    notificacionesPendientes: Int
) {


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
                IconButton(onClick = { navigationController.navigate(ScaffoldRoutes.Notificaciones.route) }) {
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