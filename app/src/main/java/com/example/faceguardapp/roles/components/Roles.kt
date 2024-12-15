package com.example.faceguardapp.roles.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.viewmodels.RolViewModel

@Composable
fun RolesListScreen(viewModel: RolViewModel = viewModel()) {
    val roles by viewModel.roles.observeAsState(emptyList())
    val mensajeEstado by viewModel.mensajeEstado.observeAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var rolToDelete by remember { mutableStateOf<Rol?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {
        Text(text = "Roles")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { showCreateDialog = true
                println("Botón Crear Rol presionado")},
            modifier = Modifier.align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color.Gray,
                contentColor = Color.White,
                containerColor = Color(Constantes.SECONDARY_BLUE)
            )
        ) {
            Text("Crear Rol")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (roles.isEmpty()) {
            Text(text = "No se encontraron roles.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(roles) { rol ->
                    RoleItem(
                        rol = rol,
                        onActualizarClick = { rolActualizado ->
                            viewModel.actualizarRol(rol.id, rolActualizado)
                        },
                        onEliminarClick = {
                            rolToDelete = rol
                        }
                    )
                }
            }
        }

        // Mostrar mensaje de estado si existe
        mensajeEstado?.let { mensaje ->
            Text(
                text = mensaje,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    // Mostrar el diálogo para crear un nuevo rol
    if (showCreateDialog) {
        println("Mostrando el diálogo para crear un rol")
        CreateRoleDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { nuevoRol ->
                viewModel.crearRol(nuevoRol)
                showCreateDialog = false
            }
        )
    }

    // Mostrar el diálogo de confirmación de eliminación
    rolToDelete?.let { rol ->
        AlertDialog(
            onDismissRequest = { rolToDelete = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar el rol \"${rol.nombre}\"?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.eliminarRol(rol.id)
                    rolToDelete = null
                },
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Gray,
                        contentColor = Color(Constantes.WHITE),
                        containerColor = Color.Red
                    )
                    ) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { rolToDelete = null },
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Gray,
                        contentColor = Color(Constantes.WHITE),
                        containerColor = Color.Black
                    )
                    ) {
                    Text("Cancelar")
                }
            }
        )
    }


    // Cargar roles si aún no están cargados
    LaunchedEffect(Unit) {
        viewModel.cargarRoles()
    }
}

@Composable
fun CreateRoleDialog(
    onDismiss: () -> Unit,
    onCreate: (Rol) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Rol") },
        text = {
            Column {
                TextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Activo")
                    Spacer(modifier = Modifier.width(8.dp))
                    Switch(
                        checked = activo,
                        onCheckedChange = { activo = it }
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nuevoRol = Rol(
                        id = 0, // El ID será asignado por el backend
                        nombre = nombre,
                        descripcion = descripcion,
                        activo = activo
                    )
                    onCreate(nuevoRol)
                },
                enabled = nombre.isNotBlank(), // Validación
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray,
                    contentColor = Color(Constantes.WHITE),
                    containerColor = Color(Constantes.SECONDARY_BLUE)
                )
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray,
                    contentColor = Color(Constantes.WHITE),
                    containerColor = Color.Black
                )) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun RoleItem(
    rol: Rol,
    onActualizarClick: (Rol) -> Unit,
    onEliminarClick: () -> Unit
) {
    var nombre by remember { mutableStateOf(rol.nombre) }
    var descripcion by remember { mutableStateOf(rol.descripcion) }
    var activo by remember { mutableStateOf(rol.activo) }

    // Determinar si el botón debe estar habilitado
    val isModified = remember(rol, nombre, descripcion, activo) {
        nombre != rol.nombre || descripcion != rol.descripcion || activo != rol.activo
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Estado: ${if (activo) "Activo" else "Inactivo"}")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = activo,
                    onCheckedChange = { activo = it },
                    colors = SwitchDefaults.colors(
                        uncheckedThumbColor = Color.LightGray,
                        checkedTrackColor = Color(0xFF4CAF50),
                        uncheckedTrackColor = Color.Gray
                    )
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(onClick = onEliminarClick,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Gray,
                        contentColor = Color(Constantes.WHITE),
                        containerColor = Color.Red
                    )) {
                    Text("Eliminar")
                }
                Button(
                    onClick = {
                        val rolActualizado = rol.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            activo = activo
                        )
                        onActualizarClick(rolActualizado)
                    },
                    enabled = isModified,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Gray,
                        contentColor = Color(Constantes.WHITE),
                        containerColor = Color(Constantes.SECONDARY_BLUE)
                    )
                ) {
                    Text("Actualizar")
                }
            }
        }
    }
}
