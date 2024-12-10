package com.example.faceguardapp.notificaciones.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.faceguardapp.notificaciones.models.Notificacion
import com.example.faceguardapp.notificaciones.viewmodels.NotificacionViewModel

    @Composable
    fun NotificacionesScreen() {
        val viewModel: NotificacionViewModel = viewModel()
        val notificaciones by viewModel.notificaciones.observeAsState(emptyList())
        val mensajeEstado by viewModel.mensajeEstado.observeAsState("")

        LaunchedEffect(Unit) {
            viewModel.cargarNotificaciones()
        }

        if (mensajeEstado.isNotEmpty()) {
            Text(text = mensajeEstado, color = Color.Red)
        }

        LazyColumn (modifier = Modifier
            .padding(16.dp)){
            items(notificaciones) { notificacion ->
                NotificacionItem(
                    notificacion = notificacion,
                    onMarcarComoLeida = { viewModel.marcarComoLeida(notificacion.id) }
                )
            }
        }
    }

@Composable
fun NotificacionItem(notificacion: Notificacion, onMarcarComoLeida: () -> Unit) {
    // Usamos un Card para darle un estilo más pulido
    Card (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .background(Color.White),
        shape = RoundedCornerShape(12.dp), // Bordes redondeados
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp), // Espaciado interno de la card
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Mensaje de la notificación
            Text(
                text = notificacion.mensaje,
                modifier = Modifier
                    .weight(1f), // Asegura que el texto ocupe todo el espacio disponible
                color = Color.Black
            )

            // Si la notificación no ha sido leída, mostramos el botón
            if (!notificacion.leida) {
                Button(
                    onClick = onMarcarComoLeida,
                    modifier = Modifier
                        .padding(start = 8.dp), // Separación entre el texto y el botón
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Blue)
                ) {
                    Text("Marcar como leída", color = Color.White)
                }
            }
        }
    }
}

