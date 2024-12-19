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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.areas.models.Area
import com.example.faceguardapp.areas.viewmodels.AreaViewModel
import com.example.faceguardapp.stores.IsStaffStore
import com.example.faceguardapp.usuarios.models.Puerta
import com.example.faceguardapp.usuarios.models.PuertaRequest
import com.example.faceguardapp.usuarios.viewmodels.ProfileViewModel
import com.example.faceguardapp.usuarios.viewmodels.PuertaViewModel

@Composable
fun PuertasListScreen(
    viewModel: PuertaViewModel = viewModel(),
    areaViewModel: AreaViewModel = viewModel()  // Cambio a AreaViewModel
) {
    val context = LocalContext.current
    val isStaffStore = remember { IsStaffStore(context) }
    val isStaff by isStaffStore.getIsStaff.collectAsState(initial = false)
    val puertas by viewModel.puertas.observeAsState(emptyList())
    val areas by areaViewModel.areas.observeAsState(emptyList())  // Cambio de Zonas a Áreas
    val mensajeEstado by viewModel.mensajeEstado.observeAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var puertaToDelete by remember { mutableStateOf<Puerta?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {
        Text(text = "Puertas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        if(isStaff) {
            Button(
                onClick = {
                    showCreateDialog = true
                    println("Botón Crear Puerta presionado")
                },
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
        }
        if (puertas.isEmpty()) {
            Text(text = "No se encontraron puertas.")
        } else {
            if(isStaff) {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(puertas) { puerta ->
                        PuertaItem(
                            listaAreas = areas,  // Cambio de zonas a áreas
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
            }
            else
            {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(puertas) { puerta ->
                        // Solo muestra la información, sin los botones de actualizar o eliminar
                        PuertaItemSoloLectura(puerta)
                    }
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

    // Mostrar el diálogo para crear una nueva puerta
    if (showCreateDialog) {
        println("Mostrando el diálogo para crear una puerta")
        CreatePuertaDialog(
            listaAreas = areas,  // Cambio de zonas a áreas
            onDismiss = { showCreateDialog = false },
            onCreate = { nuevaPuerta ->
                val nuevaPuertaFormateada = PuertaRequest(
                    nombre = nuevaPuerta.nombre,
                    descripcion = nuevaPuerta.descripcion,
                    activo = nuevaPuerta.activo,
                    areas = nuevaPuerta.areas  // Cambio a areas
                )
                viewModel.crearPuerta(nuevaPuertaFormateada)
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
                Button(
                    onClick = {
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
                Button(
                    onClick = { puertaToDelete = null },
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
        areaViewModel.cargarAreas()  // Cambio a cargar áreas
    }
}

@Composable
fun CreatePuertaDialog(
    listaAreas: List<Area>,  // Cambio de zonas a áreas
    onDismiss: () -> Unit,
    onCreate: (PuertaRequest) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var areas by remember { mutableStateOf<List<Area>>(emptyList()) }  // Cambio a áreas
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
                AreaDropdown(
                    areas = listaAreas,  // Cambio de zonas a áreas
                    selectedArea = areas.firstOrNull(),
                    onAreaSelected = { selectedArea ->
                        areas = listOf(selectedArea)
                    }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val nuevaPuerta = PuertaRequest(
                        nombre = nombre,
                        descripcion = descripcion,
                        activo = activo,
                        areas = areas.map { it.id }  // Cambio a áreas
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
            Button(
                onClick = onDismiss,
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

@Composable
fun AreaDropdown(
    areas: List<Area>,  // Lista de áreas
    selectedArea: Area?, // Área seleccionada
    onAreaSelected: (Area) -> Unit  // Callback para seleccionar el área
) {
    var expanded by remember { mutableStateOf(false) } // Controla si el dropdown está expandido o no

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Botón que muestra el área seleccionada o un texto por defecto
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedArea?.nombre ?: "Seleccione un área",
                modifier = Modifier.padding(8.dp)
            )
        }

        // DropdownMenu con las opciones de áreas
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, // Cierra el menú cuando se hace clic fuera
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            areas.forEach { area ->
                DropdownMenuItem(
                    onClick = {
                        onAreaSelected(area) // Notifica la selección al callback
                        expanded = false // Cierra el menú
                    },
                    text = {
                        Text(
                            text = area.nombre,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                )
            }
        }
    }
}

@Composable
fun PuertaItem(
    listaAreas: List<Area>,  // Lista de áreas
    puerta: Puerta,
    onActualizarClick: (PuertaRequest) -> Unit,
    onEliminarClick: () -> Unit
) {
    var nombre by remember { mutableStateOf(puerta.nombre) }
    var descripcion by remember { mutableStateOf(puerta.descripcion) }
    var activo by remember { mutableStateOf(puerta.activo) }
    var areas by remember { mutableStateOf(puerta.areas) }  // Cambio de zonas a áreas

    // Determinar si el botón debe estar habilitado
    val isModified = remember(puerta, nombre, descripcion, activo, areas) {
        nombre != puerta.nombre || descripcion != puerta.descripcion || activo != puerta.activo
                || areas != puerta.areas
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
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Área asignada:")
            AreaDropdown(  // Dropdown para seleccionar área
                areas = listaAreas,
                selectedArea = areas.firstOrNull(),
                onAreaSelected = { selectedArea ->
                    areas = listOf(selectedArea)
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Button(
                    onClick = onEliminarClick,
                    colors = ButtonDefaults.buttonColors(
                        disabledContainerColor = Color.Gray,
                        contentColor = Color(Constantes.WHITE),
                        containerColor = Color.Red
                    )
                ) {
                    Text("Eliminar")
                }
                Button(
                    onClick = {
                        val puertaActualizada = PuertaRequest(
                            nombre = nombre,
                            descripcion = descripcion,
                            activo = activo,
                            areas = areas.map { it.id }  // Cambio a áreas
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


