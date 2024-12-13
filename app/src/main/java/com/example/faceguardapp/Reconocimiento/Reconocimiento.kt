import android.graphics.Bitmap
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.launch
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import java.io.ByteArrayOutputStream
import android.util.Base64
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.platform.LocalContext
import com.example.faceguardapp.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun FotografiaScreen() {
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicturePreview(),
        onResult = {
            bitmap.value = it
        }
    )
    val imageBase64 = remember { mutableStateOf<String?>(null) }
    var success by rememberSaveable { mutableStateOf("") }
    var username =
        UsernameStore(LocalContext.current).getUsername.collectAsState(initial = "").value.toString()

    bitmap.value?.let {
        imageBase64.value = bitmapToBase64(it)
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                cameraLauncher.launch()
            }
        }
    )
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        bitmap.value?.asImageBitmap()?.let {
            Image(
                bitmap = it,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.width(300.dp)
            )
        }
        Button(onClick = {
            permissionLauncher.launch(android.Manifest.permission.CAMERA)
        }) {
            Text(text = "Tomar Foto")
        }
        bitmap.value?.let {
            Button(onClick = {
                reconocimiento(
                    username = username,
                    image = imageBase64.value.toString(),
                    onError = { error ->
                        println(error)
                    },
                    onSuccess = { result ->
                        success = result
                    }
                )
            }) {
                Text(text = "Reconocer")
            }
            Text(text = success)
        }
    }
}

fun bitmapToBase64(bitmap: Bitmap): String {
    val outputStream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream) // Comprime como PNG
    val byteArray = outputStream.toByteArray()
    return Base64.encodeToString(byteArray, Base64.DEFAULT)
}

fun reconocimiento(
    username: String,
    image: String,
    onError: (String) -> Unit,
    onSuccess: (String) -> Unit
) {
    CoroutineScope(Dispatchers.IO).launch {
        RetrofitClient.apiReconocimiento.reconocimiento(username, "data:image/png;base64,${image}")
            .enqueue(object : Callback<ReconocimientoResponse> {
                override fun onResponse(
                    call: Call<ReconocimientoResponse>,
                    response: Response<ReconocimientoResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onSuccess(it.success + " " + it.result)
                        } ?: onError("Error: Respuesta vacía")
                    } else {
                        onError("Error: ${response}")
                    }
                }

                override fun onFailure(call: Call<ReconocimientoResponse>, t: Throwable) {
                    onError("Error: ${t.message}")
                }
            })
    }
}
