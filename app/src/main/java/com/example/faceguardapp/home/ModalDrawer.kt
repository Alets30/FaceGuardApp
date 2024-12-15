package com.example.faceguardapp.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddLocation
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoorBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LockPerson
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material.icons.filled.Repeat
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.routes.MainRoutes
import com.example.faceguardapp.routes.ScaffoldRoutes
import com.example.faceguardapp.stores.StoreDarkMode
import kotlinx.coroutines.launch

@Composable
fun MyModalDrawer(
    navigationController: NavController,
    onCloseDrawer: () -> Unit,
    showLogoutDialog: (Boolean) -> Unit
) {
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
        TextButton(onClick = {
            navigationController.navigate(ScaffoldRoutes.Roles.route)
            onCloseDrawer()
        }) {
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
        TextButton(onClick = {
            navigationController.navigate(ScaffoldRoutes.Reconocimiento.route)
            onCloseDrawer()
        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Icon(
                    imageVector = Icons.Filled.PersonSearch,
                    contentDescription = "Reconocimiento",
                    tint = Color.White
                )
                Text(
                    text = "Reconocimiento",
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
        TextButton(onClick = {
            navigationController.navigate(ScaffoldRoutes.Notificaciones.route)
            onCloseDrawer()
        }) {
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
//        TextButton(onClick = { onCloseDrawer() }) {
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Icon(
//                    imageVector = Icons.Default.Settings,
//                    contentDescription = "Configuraciones",
//                    tint = Color.White
//                )
//                Text(
//                    text = "Configuraciones",
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(8.dp),
//                    color = Color.White,
//                    fontSize = 22.sp,
//                    fontWeight = FontWeight.Normal
//                )
//            }
//        }
        TextButton(onClick = { showLogoutDialog(true) }) {
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
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = "Modo Oscuro",
                modifier = Modifier
                    .padding(8.dp),
                color = Color.White,
                fontSize = 22.sp,
                fontWeight = FontWeight.Normal,
            )
            Switch(
                checked = darkMode.value ?: false,
                onCheckedChange = { isChecked ->
                    scope.launch {
                        darkModeStore.saveDarkMode(isChecked)
                    }
                },
                thumbContent = if (darkMode.value == true) {
                    {
                        Icon(
                            imageVector = Icons.Filled.Nightlight,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = Color.White
                        )
                    }
                } else {
                    {
                        Icon(
                            imageVector = Icons.Filled.Brightness4,
                            contentDescription = null,
                            modifier = Modifier.size(SwitchDefaults.IconSize),
                            tint = Color.White
                        )
                    }
                },
                modifier = Modifier.padding(end = 30.dp),
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color(Constantes.PRIMARY_BLUE),
                    checkedTrackColor = Color(Constantes.WHITE),
                    uncheckedThumbColor = Color(Constantes.PRIMARY_BLUE),
                    uncheckedTrackColor = Color(Constantes.WHITE),
                )
            )
        }
    }
}