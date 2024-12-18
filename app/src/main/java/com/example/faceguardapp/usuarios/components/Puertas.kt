package com.example.faceguardapp.usuarios.components

import androidx.compose.foundation.background
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
import com.example.faceguardapp.usuarios.models.Puerta
import com.example.faceguardapp.usuarios.viewmodels.ProfileViewModel
import com.example.faceguardapp.usuarios.viewmodels.PuertaViewModel

@Composable
fun PuertasListScreen(viewModel: PuertaViewModel = viewModel(), profileViewModel: ProfileViewModel = viewModel()) {
    val puertas by viewModel.puertas.observeAsState(emptyList())
    val mensajeEstado by viewModel.mensajeEstado.observeAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var puertaToDelete by remember { mutableStateOf<Puerta?>(null) }

    //val perfilUsuario by profileViewModel.perfilUsuario.observeAsState()
    //val esSuperUsuario = perfilUsuario?.usuario?.is_superuser ?: false

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {
        Text(text = "Puertas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
       // if(esSuperUsuario) {
            Button(
                onClick = { showCreateDialog = true },
                modifier = Modifier.align(Alignment.Start),
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color.Gray,
                    contentColor = Color.White,
                    containerColor = Color(Constantes.SECONDARY_BLUE)
                )
            ) {
                Text("Crear Puerta")
            }
            Spacer(modifier = Modifier.height(16.dp))
        //}

        if (puertas.isEmpty()) {
            Text(text = "No se encontraron puertas.")
        } else {
            //if(esSuperUsuario) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(puertas) { puerta ->
                        PuertaItem(
                            puerta = puerta,
                            onActualizarClick = { puertaActualizada ->
                                viewModel.actualizarPuerta(puerta.id, puertaActualizada)
                            },
                            onEliminarClick = {
                                puertaToDelete = puerta
                            }
                        )
                    }
                }
            /*}
            else{
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(puertas) { puerta ->
                        // Solo muestra la información, sin los botones de actualizar o eliminar
                        PuertaItemSoloLectura(puerta)
                    }
                }
            }*/
        }

        // Mostrar mensaje de estado si existe
        mensajeEstado?.let { mensaje ->
            Text(
                text = mensaje,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }

    // Mostrar el diálogo para crear una nueva puerta
    if (showCreateDialog) {
        CreatePuertaDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { nuevaPuerta ->
                viewModel.crearPuerta(nuevaPuerta)
                showCreateDialog = false
            }
        )
    }

    // Mostrar el diálogo de confirmación de eliminación
    puertaToDelete?.let { puerta ->
        AlertDialog(
            onDismissRequest = { puertaToDelete = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar la puerta \"${puerta.nombre}\"?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.eliminarPuerta(puerta.id)
                    puertaToDelete = null
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
                Button(onClick = { puertaToDelete = null },
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

    // Cargar puertas si aún no están cargadas
    LaunchedEffect(Unit) {
        viewModel.cargarPuertas()
    }
}

@Composable
fun CreatePuertaDialog(
    onDismiss: () -> Unit,
    onCreate: (Puerta) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Puerta") },
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
                    val nuevaPuerta = Puerta(
                        id = 0, // El ID será asignado por el backend
                        nombre = nombre,
                        descripcion = descripcion,
                        activo = activo
                    )
                    onCreate(nuevaPuerta)
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
fun PuertaItem(
    puerta: Puerta,
    onActualizarClick: (Puerta) -> Unit,
    onEliminarClick: () -> Unit
) {
    var nombre by remember { mutableStateOf(puerta.nombre) }
    var descripcion by remember { mutableStateOf(puerta.descripcion) }
    var activo by remember { mutableStateOf(puerta.activo) }

    // Determinar si el botón debe estar habilitado
    val isModified = remember(puerta, nombre, descripcion, activo) {
        nombre != puerta.nombre || descripcion != puerta.descripcion || activo != puerta.activo
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            TextField(
                value = nombre,
                onValueChange = { nombre = it },
                label = { Text("Nombre") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextField(
                value = descripcion,
                onValueChange = { descripcion = it },
                label = { Text("Descripción") },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedIndicatorColor = Color.LightGray,
                    focusedIndicatorColor = Color(Constantes.SECONDARY_BLUE)
                )
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
                        val puertaActualizada = puerta.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            activo = activo
                        )
                        onActualizarClick(puertaActualizada)
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

@Composable
fun PuertaItemSoloLectura(puerta: Puerta) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White,
        )
    )
    {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Nombre: ${puerta.nombre}", style = MaterialTheme.typography.bodyLarge)
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (puerta.descripcion.isBlank()) {
                    "Sin descripción"
                } else {
                    "Descripción: ${puerta.descripcion}"
                },
                style = MaterialTheme.typography.bodyMedium
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Estado: ${if (puerta.activo) "Activo" else "Inactivo"}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
