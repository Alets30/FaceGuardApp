package com.example.faceguardapp.usuarios.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.routes.ScaffoldRoutes
import com.example.faceguardapp.usuarios.viewmodels.ProfileViewModel


@Composable
fun EditProfileView(profileId: Int, navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }
    val profile = viewModel.usuarios.value?.find { it.id == profileId }

    if (profile != null) {
        var nombre by remember { mutableStateOf(profile.nombre) }
        var apellidoP by remember { mutableStateOf(profile.apellido_p) }
        var apellidoM by remember { mutableStateOf(profile.apellido_m) }
        var telefono by remember { mutableStateOf(profile.telefono) }
        var direccion by remember { mutableStateOf(profile.direccion) }
        var estado by remember { mutableStateOf(profile.estado) }
        var ciudad by remember { mutableStateOf(profile.ciudad) }

        val isModified = remember(
            nombre, apellidoP, apellidoM, telefono, direccion, estado, ciudad
        ) {
            nombre != profile.nombre ||
                    apellidoP != profile.apellido_p ||
                    apellidoM != profile.apellido_m ||
                    telefono != profile.telefono ||
                    direccion != profile.direccion ||
                    estado != profile.estado ||
                    ciudad != profile.ciudad
        }

        Column(modifier = Modifier
            .padding(16.dp)
            .padding(top = 14.dp))
        {
            Text(text = "Editar", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = apellidoP,
                    onValueChange = { apellidoP = it },
                    label = { Text("Apellido Paterno") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = apellidoM,
                    onValueChange = { apellidoM = it },
                    label = { Text("Apellido Materno") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = telefono,
                    onValueChange = { telefono = it },
                    label = { Text("Teléfono") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = direccion,
                    onValueChange = { direccion = it },
                    label = { Text("Dirección") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = estado,
                    onValueChange = { estado = it },
                    label = { Text("Estado") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = ciudad,
                    onValueChange = { ciudad = it },
                    label = { Text("Ciudad") },
                    colors = TextFieldDefaults.colors(
                        unfocusedContainerColor = Color.White,
                        focusedContainerColor = Color.White,
                        unfocusedIndicatorColor = Color.LightGray,
                        focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                )
                {
                    Button(
                        onClick = {
                            navController.navigate(ScaffoldRoutes.Usuarios.route) // Regresa a la lista
                        },
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.Gray,
                            contentColor = Color.Black,
                            containerColor = Color.White
                        )
                    ) {
                        Text("Regresar")
                    }
                    Button(
                        onClick = {
                            val updatedProfile = profile.copy(
                                nombre = nombre,
                                apellido_p = apellidoP,
                                apellido_m = apellidoM,
                                telefono = telefono,
                                direccion = direccion,
                                estado = estado,
                                ciudad = ciudad
                            )
                            viewModel.actualizarUsuario(profileId, updatedProfile)
                            navController.navigate(ScaffoldRoutes.Usuarios.route) // Regresa a la lista
                        },
                        colors = ButtonDefaults.buttonColors(
                            disabledContainerColor = Color.Gray,
                            contentColor = Color(Constantes.WHITE),
                            containerColor = Color(Constantes.SECONDARY_BLUE)
                        ),
                        enabled = isModified
                    ) {
                        Text("Guardar Cambios")
                    }
                }
            }
        }
    } else {
        Text("Cargando perfil...")
    }
}