package com.example.faceguardapp.usuarios.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import android.content.Intent
import android.os.Environment
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.TakePicture
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.usuarios.viewmodels.PuertaViewModel

@Composable
fun ReconocimientoFacial(puertaId: Int, username: String, navController: NavController) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var imageBase64 by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val puertaViewModel: PuertaViewModel = viewModel()

    // Observando el estado de acceso verificado y mensaje de estado
    val accesoVerificado by puertaViewModel.accesoVerificado.observeAsState(null)
    val mensajeEstado by puertaViewModel.mensajeEstado.observeAsState("")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture(),
        onResult = { success ->
            if (success) {
                imageUri?.let {
                    // Convierte la imagen a base64 después de que se haya capturado
                    val base64String = imageUriToBase64(it, context)
                    imageBase64 = base64String
                }
            }
        }
    )

    Box(
        modifier = Modifier.fillMaxSize(), // Ocupar todo el espacio disponible
        contentAlignment = Alignment.Center // Centra el contenido (el botón) en el Box
    ) {
        // Botón para capturar la foto
        Button(onClick = {
            val photoUri = createImageFile(context)  // Función para crear el archivo de imagen
            imageUri = photoUri
            launcher.launch(photoUri)
        },
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color.Gray,
                contentColor = Color(Constantes.WHITE),
                containerColor = Color(Constantes.SECONDARY_BLUE)
            )) {
            Text("Tomar Foto")
        }
    }

    // Llamar a la función de enviar imagen cuando la imagen base64 esté lista
    imageBase64?.let { base64 ->
        val base64WithPrefix = "data:image/webp;base64,$base64"
        puertaViewModel.verificarRostroAcceso(puertaId, username, base64WithPrefix)
    }

    // Mostrar el estado de acceso y mensaje
    accesoVerificado?.let { acceso ->
        Text("Acceso: ${if (acceso) "Concedido" else "Denegado"}")
    }

    // Mostrar el mensaje de estado
    if (mensajeEstado.isNotEmpty()) {
        Text(text = mensajeEstado)
    }
}

// Convierte la imagen a base64
fun imageUriToBase64(uri: Uri, context: Context): String {
    val inputStream = context.contentResolver.openInputStream(uri)
    val byteArray = inputStream?.readBytes()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

// Crea el archivo para almacenar la foto
fun createImageFile(context: Context): Uri {
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val storageDir: File = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!
    val imageFile = File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir)
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", imageFile)
}
