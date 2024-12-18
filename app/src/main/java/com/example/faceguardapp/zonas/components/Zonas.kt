package com.example.faceguardapp.zonas.components

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
import com.example.faceguardapp.zonas.viewmodels.ZonaViewModel
import com.example.faceguardapp.zonas.model.Zona

@Composable
fun ZonasListScreen(viewModel: ZonaViewModel = viewModel()) {
    val zonas by viewModel.zonas.observeAsState(emptyList())
    val mensajeEstado by viewModel.mensajeEstado.observeAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var zonaToDelete by remember { mutableStateOf<Zona?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {
        Text(text = "Zonas")
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = { showCreateDialog = true
                println("Botón Crear Zona presionado")},
            modifier = Modifier.align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color.Gray,
                contentColor = Color.White,
                containerColor = Color(Constantes.SECONDARY_BLUE)
            )
        ) {
            Text("Crear Zona")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (zonas.isEmpty()) {
            Text(text = "No se encontraron zonas.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(zonas) { zona ->
                    ZonaItem(
                        zona = zona,
                        onActualizarClick = { zonaActualizado ->
                            viewModel.actualizarZona(zona.id, zonaActualizado)
                        },
                        onEliminarClick = {
                            zonaToDelete = zona
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

    // Mostrar el diálogo para crear un nuevo zona
    if (showCreateDialog) {
        println("Mostrando el diálogo para crear una zona")
        CreateZonaDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { nuevaZona ->
                viewModel.crearZona(nuevaZona)
                showCreateDialog = false
            }
        )
    }

    // Mostrar el diálogo de confirmación de eliminación
    zonaToDelete?.let { zona ->
        AlertDialog(
            onDismissRequest = { zonaToDelete = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar la zona \"${zona.nombre}\"?") },
            confirmButton = {
                Button(onClick = {
                    viewModel.eliminarZona(zona.id)
                    zonaToDelete = null
                }) {
                    Text("Eliminar")
                }
            },
            dismissButton = {
                Button(onClick = { zonaToDelete = null }) {
                    Text("Cancelar")
                }
            }
        )
    }


    // Cargar zonas si aún no están cargados
    LaunchedEffect(Unit) {
        viewModel.cargarZonas()
    }
}

@Composable
fun CreateZonaDialog(
    onDismiss: () -> Unit,
    onCreate: (Zona) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(true) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Zona") },
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
                    val nuevaZona = Zona(
                        id = 0, // El ID será asignado por el backend
                        nombre = nombre,
                        descripcion = descripcion,
                        activo = activo
                    )
                    onCreate(nuevaZona)
                },
                enabled = nombre.isNotBlank() // Validación
            ) {
                Text("Crear")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

@Composable
fun ZonaItem(
    zona: Zona,
    onActualizarClick: (Zona) -> Unit,
    onEliminarClick: () -> Unit
) {
    var nombre by remember { mutableStateOf(zona.nombre) }
    var descripcion by remember { mutableStateOf(zona.descripcion) }
    var activo by remember { mutableStateOf(zona.activo) }

    // Determinar si el botón debe estar habilitado
    val isModified = remember(zona, nombre, descripcion, activo) {
        nombre != zona.nombre || descripcion != zona.descripcion || activo != zona.activo
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
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
                        val zonaActualizada = zona.copy(
                            nombre = nombre,
                            descripcion = descripcion,
                            activo = activo
                        )
                        onActualizarClick(zonaActualizada)
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
