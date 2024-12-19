package com.example.faceguardapp.auth.register.body

import AuthTokenStore
import LoginRequest
import LoginResponse
import RegisterRequest
import RegisterResponse
import UsernameStore
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.routes.MainRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RegisterButton(
    loginEnable: Boolean,
    username: String,
    password: String,
    photo: String,
    errorMessage: (String) -> Unit,
    navigationController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenStore = AuthTokenStore(context)
    val usernameStore = UsernameStore(context)
    Button(
        onClick = {
            errorMessage("")
            performRegister(username, password, photo, { error ->
                errorMessage(error)
            }, scope, tokenStore, usernameStore, navigationController)
        }, enabled = loginEnable, modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text = "Register")
    }
}

fun performRegister(
    username: String,
    password: String,
    photo: String,
    onError: (String) -> Unit,
    scope: CoroutineScope,
    tokenStore: AuthTokenStore,
    usernameStore: UsernameStore,
    navigationController: NavController
) {
    val request = RegisterRequest(username, password, "data:image/png;base64,$photo")

    scope.launch {
        try {
            // Realiza la solicitud en un hilo de IO
            withContext(Dispatchers.IO) {
                val response = RetrofitClient.api.register(request)
                    .execute() // Cambiar a `execute()` para manejarlo sin callbacks
                if (response.isSuccessful) {
                    response.body()?.let { body ->
                        // Guarda el token y el username en el almacenamiento
                        tokenStore.saveAuthToken(body.token)
                        usernameStore.saveUsername(body.username)
                    } ?: throw Exception("Respuesta vacía")
                } else {
                    throw Exception(response.errorBody()?.string() ?: "Error desconocido")
                }
            }

            // Navega a la pantalla principal en el hilo principal
            withContext(Dispatchers.Main) {
                navigationController.navigate(MainRoutes.Home.route) {
                    popUpTo(MainRoutes.Register.route) { inclusive = true }
                }
            }
        } catch (e: Exception) {
            onError("Error: ${e.message}")
        }
    }
}
