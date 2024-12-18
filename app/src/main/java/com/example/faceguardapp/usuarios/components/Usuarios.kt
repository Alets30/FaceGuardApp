package com.example.faceguardapp.usuarios.components

import androidx.compose.foundation.clickable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.routes.ScaffoldRoutes
import com.example.faceguardapp.usuarios.models.Profile
import com.example.faceguardapp.usuarios.viewmodels.ProfileViewModel

@Composable
fun UsuariosListScreen(navController: NavController, viewModel: ProfileViewModel = viewModel()) {
    val usuarios by viewModel.usuarios.observeAsState(emptyList())
    val mensajeEstado by viewModel.mensajeEstado.observeAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {
        Text(text = "Usuarios", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))

        if (usuarios.isEmpty()) {
            Text(text = "No se encontraron usuarios.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(usuarios) { usuario ->
                    UsuarioItem(profile = usuario, navController = navController, viewModel = viewModel)
                }
            }
        }

        mensajeEstado?.let { mensaje ->
            Text(
                text = mensaje,
                modifier = Modifier.padding(top = 16.dp),
                color = MaterialTheme.colorScheme.error
            )
        }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
    }
}

@Composable
fun UsuarioItem(profile: Profile, navController: NavController, viewModel: ProfileViewModel) {
    var showRoleDialog by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Clave: ${profile.user_id}", style = MaterialTheme.typography.titleMedium)
            Text(text = "Nombre: ${profile.nombre} ${profile.apellido_p} ${profile.apellido_m}")
            Text(text = "Teléfono: ${profile.telefono}")
            Spacer(modifier = Modifier.height(8.dp))

            // Botón Editar
            Button(
                onClick = {
                    navController.navigate(ScaffoldRoutes.EditProfile.createRoute(profile.id))
                },
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray,
                    contentColor = Color(Constantes.WHITE),
                    containerColor = Color(Constantes.SECONDARY_BLUE)
                )
            ) {
                Text("Editar")
            }

            // Botón Asignar Rol
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { showRoleDialog = true },
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray,
                    contentColor = Color(Constantes.WHITE),
                    containerColor = Color(Constantes.SECONDARY_BLUE)
                )
            ) {
                Text("Asignar Rol")
            }
        }
    }

    // Mostrar el Dialog cuando el botón "Asignar Rol" es presionado
    if (showRoleDialog) {
        AsignarRolDialog(
            profile = profile,
            onDismiss = { showRoleDialog = false },
            viewModel = viewModel
        )
    }
}

@Composable
fun AsignarRolDialog(
    profile: Profile,
    onDismiss: () -> Unit,
    viewModel: ProfileViewModel
) {
    val roles by viewModel.rolesNoAsignados.observeAsState(emptyList())
    var selectedRoleId by remember { mutableStateOf<Int?>(null) }
    var fechaVencimiento by remember { mutableStateOf(null) }
    val isModified = remember(
        selectedRoleId){
        selectedRoleId != null
    }

    // Cargar roles no asignados cuando se abre el Dialog
    LaunchedEffect(profile.id) {
        viewModel.obtenerRolesNoAsignados(profile.id)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Asignar Rol a ${profile.nombre}") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // Mostrar roles no asignados
                LazyColumn {
                    items(roles) { role ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                                .clickable {
                                    selectedRoleId = role.id
                                },
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(role.nombre)
                            if (selectedRoleId == role.id) {
                                Icon(Icons.Filled.Check, contentDescription = "Seleccionado")
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        },
        confirmButton = {
            Button(
                onClick = {
                        viewModel.asignarRol(profile.id, selectedRoleId!!, fechaVencimiento)
                        onDismiss()
                },
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray,
                    contentColor = Color(Constantes.WHITE),
                    containerColor = Color(0xFF4CAF50)
                ),
                enabled = isModified
            ) {
                Text("Asignar Rol")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}
