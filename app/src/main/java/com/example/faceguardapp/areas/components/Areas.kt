package com.example.faceguardapp.areas.components

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
import com.example.faceguardapp.areas.models.Area
import com.example.faceguardapp.areas.models.AreaRequest
import com.example.faceguardapp.areas.viewmodels.AreaViewModel
import com.example.faceguardapp.zonas.model.Zona
import com.example.faceguardapp.zonas.viewmodels.ZonaViewModel

@Composable
fun AreasListScreen(
    viewModel: AreaViewModel = viewModel(),
    zonaViewModel: ZonaViewModel = viewModel()
) {
    val areas by viewModel.areas.observeAsState(emptyList())
    val zonas by zonaViewModel.zonas.observeAsState(emptyList())
    val mensajeEstado by viewModel.mensajeEstado.observeAsState()
    var showCreateDialog by remember { mutableStateOf(false) }
    var areaToDelete by remember { mutableStateOf<Area?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .padding(top = 30.dp)
    ) {
        Text(text = "Áreas", style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                showCreateDialog = true
                println("Botón Crear Area presionado")
            },
            modifier = Modifier.align(Alignment.Start),
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color.Gray,
                contentColor = Color.White,
                containerColor = Color(Constantes.SECONDARY_BLUE)
            )
        ) {
            Text("Crear Área")
        }
        Spacer(modifier = Modifier.height(16.dp))

        if (areas.isEmpty()) {
            Text(text = "No se encontraron áreas.")
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(areas) { area ->
                    AreaItem(
                        listaZonas = zonas,
                        area = area,
                        onActualizarClick = { areaActualizada ->
                            viewModel.actualizarArea(area.id, areaActualizada)
                        },
                        onEliminarClick = {
                            areaToDelete = area
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

    // Mostrar el diálogo para crear una nueva área
    if (showCreateDialog) {
        println("Mostrando el diálogo para crear una área")
        CreateAreaDialog(
            onDismiss = { showCreateDialog = false },
            onCreate = { nuevaArea ->
                val nuevaAreaFormateada = AreaRequest(
                    nombre = nuevaArea.nombre,
                    descripcion = nuevaArea.descripcion,
                    activo = nuevaArea.activo,
                    zonas = nuevaArea.zonas.map { it.id }
                )
                viewModel.crearArea(nuevaAreaFormateada)
                showCreateDialog = false
            }
        )
    }

    // Mostrar el diálogo de confirmación de eliminación
    areaToDelete?.let { area ->
        AlertDialog(
            onDismissRequest = { areaToDelete = null },
            title = { Text("Confirmar Eliminación") },
            text = { Text("¿Estás seguro de que deseas eliminar el area \"${area.nombre}\"?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.eliminarArea(area.id)
                        areaToDelete = null
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
                    onClick = { areaToDelete = null },
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


    // Cargar áreas si aún no están cargados
    LaunchedEffect(Unit) {
        viewModel.cargarAreas()
        zonaViewModel.cargarZonas()
    }
}

@Composable
fun CreateAreaDialog(
    onDismiss: () -> Unit,
    onCreate: (Area) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var activo by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Crear Área") },
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
                    val nuevaArea = Area(
                        id = 0, // El ID será asignado por el backend
                        nombre = nombre,
                        descripcion = descripcion,
                        activo = activo,
                        zonas = listOf()
                    )
                    onCreate(nuevaArea)
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
fun AreaItem(
    listaZonas: List<Zona>,
    area: Area,
    onActualizarClick: (AreaRequest) -> Unit,
    onEliminarClick: () -> Unit
) {
    var nombre by remember { mutableStateOf(area.nombre) }
    var descripcion by remember { mutableStateOf(area.descripcion) }
    var activo by remember { mutableStateOf(area.activo) }
    var zonas by remember { mutableStateOf(area.zonas) }

    // Determinar si el botón debe estar habilitado
    val isModified = remember(area, nombre, descripcion, activo, zonas) {
        nombre != area.nombre || descripcion != area.descripcion || activo != area.activo
                || zonas != area.zonas
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
            Text(text = "Zona asignada:")
            ZonaDropdown(
                zonas = listaZonas,
                selectedZona = zonas.firstOrNull(),
                onZonaSelected = { selectedZona ->
                    zonas = listOf(selectedZona)
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
                        val areaActualizada = AreaRequest(
                            nombre = nombre,
                            descripcion = descripcion,
                            activo = activo,
                            zonas = zonas.map { it.id }
                        )
                        onActualizarClick(areaActualizada)
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
fun ZonaDropdown(
    zonas: List<Zona>, // Lista de zonas que se mostrarán
    selectedZona: Zona?, // Zona actualmente seleccionada
    onZonaSelected: (Zona) -> Unit // Callback para manejar la selección de una zona
) {
    var expanded by remember { mutableStateOf(false) } // Controla si el dropdown está expandido o no

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        // Botón que muestra la zona seleccionada o un texto por defecto
        OutlinedButton(
            onClick = { expanded = true },
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
            ),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = selectedZona?.nombre ?: "Seleccione una zona",
                modifier = Modifier.padding(8.dp)
            )
        }

        // DropdownMenu con las opciones de zonas
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }, // Cierra el menú cuando se hace clic fuera
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
        ) {
            zonas.forEach { zona ->
                DropdownMenuItem(
                    onClick = {
                        onZonaSelected(zona) // Notifica la selección al callback
                        expanded = false // Cierra el menú
                    },
                    text = {
                        Text(
                            text = zona.nombre,
                            color = Color.Black,
                            modifier = Modifier.padding(8.dp)
                        )
                    }
                )
            }
        }
    }
}
