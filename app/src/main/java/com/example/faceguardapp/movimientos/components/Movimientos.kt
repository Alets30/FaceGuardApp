package com.example.faceguardapp.movimientos.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.faceguardapp.movimientos.models.Movimiento
import com.example.faceguardapp.movimientos.viewmodels.MovimientoViewModel

@Composable
fun MovimientoScreen() {
    val viewModel: MovimientoViewModel = viewModel()
    val movimientos by viewModel.movimientos.observeAsState(emptyList())
    val mensajeEstado by viewModel.error.observeAsState("")


    LaunchedEffect(Unit) {
        viewModel.fetchMovimientos()
    }


    if (mensajeEstado.isNotEmpty()) {
        Text(
            text = mensajeEstado,
            color = Color.Red,
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentWidth(Alignment.CenterHorizontally)
        )
    }


    LazyColumn(
        modifier = Modifier
            .padding(top = 40.dp, start = 10.dp, end = 10.dp)
    ) {
        items(movimientos) { movimiento ->
            MovimientoItem(
                movimiento = movimiento,
                onClick = {  }
            )
        }
    }
}

@Composable
fun MovimientoItem(movimiento: Movimiento, onClick: () -> Unit) {
    val (icon, iconColor) = when (movimiento.tipo) {
        "Acceso" -> Icons.Default.Check to Color(0xFF4CAF50)
        "Acceso denegado" -> Icons.Default.Close to Color(0xFFF44336)
        "Salida" -> Icons.Default.ExitToApp to Color(0xff0a5483)
        else -> Icons.Default.Info to Color(0xFF9E9E9E)
    }

    Card(
        modifier = Modifier
            .padding(12.dp)
            .clickable(onClick = onClick)
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF5F5F5)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconColor,
                modifier = Modifier
                    .size(40.dp)
                    .padding(end = 16.dp)
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Tipo: ${movimiento.tipo}",
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = Color(0xFF333333)
                )
                Text(
                    text = "Hora: ${movimiento.hora}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )
                Text(
                    text = "Fecha: ${movimiento.fecha}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color(0xFF666666)
                )
            }
        }
        Divider(color = Color(0xFFDDDDDD), thickness = 1.dp)
        Row(
            modifier = Modifier
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            movimiento.usuario?.let {
                Text(
                    text = "Usuario: ${it.nombre}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333)
                )
            }
            Spacer(modifier = Modifier.padding(start = 15.dp))
            movimiento.puerta?.let {
                Text(
                    text = "Puerta: ${it.nombre}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF333333)
                )
            }
        }
    }
}