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
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.PersonSearch
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.example.faceguardapp.Constantes
import com.example.faceguardapp.RetrofitClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun ReconocimientoScreen() {
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
    var isLoading by rememberSaveable() { mutableStateOf(false) }

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
        Box(
            modifier = Modifier
                .width(400.dp)
                .height(400.dp)
                .padding(vertical = 10.dp),
        ) {
            bitmap.value?.asImageBitmap()?.let {
                Image(
                    bitmap = it,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(400.dp)
                        .clip(RoundedCornerShape(100)),
                )
            }
        }
        Row {
            IconButton(
                onClick = {
                    permissionLauncher.launch(android.Manifest.permission.CAMERA)
                },
                enabled = !isLoading,
                modifier = Modifier
                    .padding(horizontal = 10.dp)
                    .size(80.dp)
                    .clip(RoundedCornerShape(100.dp)),
                colors = IconButtonDefaults.iconButtonColors(
                    containerColor = Color(Constantes.PRIMARY_BLUE),
                    contentColor = Color(Constantes.WHITE),
                    disabledContainerColor = Color(Constantes.TERTIARY_BLUE),
                    disabledContentColor = Color.LightGray
                )
            ) {
                Icon(
                    imageVector = Icons.Default.CameraAlt,
                    contentDescription = "Tomar foto",
                    tint = Color(Constantes.WHITE),
                    modifier = Modifier.size(30.dp)
                )
            }
            bitmap.value?.let {
                IconButton(
                    onClick = {
                        success = ""
                        reconocimiento(
                            username = username,
                            image = imageBase64.value.toString(),
                            onSuccess = { result ->
                                success = result
                            },
                            isLoading = {
                                isLoading = it
                            }
                        )
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .padding(horizontal = 10.dp)
                        .size(80.dp)
                        .clip(RoundedCornerShape(100.dp)),
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color(Constantes.PRIMARY_BLUE),
                        contentColor = Color(Constantes.WHITE),
                        disabledContainerColor = Color(Constantes.TERTIARY_BLUE),
                        disabledContentColor = Color.LightGray
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.PersonSearch,
                        contentDescription = "Reconocer",
                        tint = Color(Constantes.WHITE),
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier
                    .padding(top = 16.dp)
                    .width(32.dp),
                color = MaterialTheme.colorScheme.secondary,
                trackColor = MaterialTheme.colorScheme.surfaceVariant,
            )
        }
        Text(
            text = success,
            fontSize = 18.sp,
            fontWeight = FontWeight.Normal,
            color = Color.DarkGray,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth(),
            textAlign = TextAlign.Center
        )
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
    onSuccess: (String) -> Unit,
    isLoading: (Boolean) -> Unit
) {
    isLoading(true)
    CoroutineScope(Dispatchers.IO).launch {
        RetrofitClient.apiReconocimiento.reconocimiento(username, "data:image/png;base64,${image}")
            .enqueue(object : Callback<ReconocimientoResponse> {
                override fun onResponse(
                    call: Call<ReconocimientoResponse>,
                    response: Response<ReconocimientoResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let {
                            onSuccess(
                                if (it.success) "Acceso concedido: Rostros coincidentes"
                                else "Acceso denegado: Intento de ingreso no autorizado"
                            )
                            isLoading(false)
                        } ?: {
                            onSuccess("Error: Respuesta vacía")
                            isLoading(false)
                        }
                    } else {
                        onSuccess("Error: ${response}")
                        isLoading(false)
                    }
                }

                override fun onFailure(call: Call<ReconocimientoResponse>, t: Throwable) {
                    onSuccess("Error: ${t.message}")
                    isLoading(false)
                }
            })
    }
}
