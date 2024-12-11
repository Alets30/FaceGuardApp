package com.example.faceguardapp.notificaciones.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
            .padding(top = 40.dp, start = 10.dp, end = 10.dp)){
            items(notificaciones) { notificacion ->
                NotificacionItem(
                    notificacion = notificacion,
                    onMarcarComoLeida = { viewModel.marcarComoLeida(notificacion.id) }
                )
            }
        }
    }

@Composable
fun NotificacionItem(
    notificacion: Notificacion,
    onMarcarComoLeida: () -> Unit
) {
    // Usamos un Row para organizar el avatar y el contenido de la notificación
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .background(Color(0xffefefef), RoundedCornerShape(8.dp))
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = notificacion.mensaje,
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Black
            )

            Text(
                text = notificacion.fecha_creacion.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = Color.Gray
            )
        }

        // Botón "Marcar como leída"
        if (!notificacion.leida) {
            TextButton(onClick = onMarcarComoLeida) {
                Text(
                    text = "Leer",
                    color = Color(0xff0a5483)
                )
            }
        }
    }
}


