package com.example.faceguardapp.login.body

import AuthTokenStore
import LoginRequest
import LoginResponse
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun LoginButton(
    loginEnable: Boolean,
    username: String,
    password: String,
    errorMessage: (String) -> Unit,
    navigationController: NavController
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val tokenStore = AuthTokenStore(context)
    val usernameStore = UsernameStore(context)
    Button(
        onClick = {
            performLogin(username, password, { error ->
                errorMessage(error)
            }, scope, tokenStore, usernameStore, navigationController)
        }, enabled = loginEnable, modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text = "Log In")
    }
}

fun performLogin(
    username: String,
    password: String,
    onError: (String) -> Unit,
    scope: CoroutineScope,
    tokenStore: AuthTokenStore,
    usernameStore: UsernameStore,
    navigationController: NavController
) {
    val request = LoginRequest(username, password)

    CoroutineScope(Dispatchers.IO).launch {
        RetrofitClient.api.login(request).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        scope.launch {
                            tokenStore.saveAuthToken(it.token)
                            usernameStore.saveUsername(it.username)
                        }
                        navigationController.navigate(MainRoutes.Home.route)
                        navigationController.popBackStack()
                    } ?: onError("Error: Respuesta vacía")
                } else {
                    onError("Error: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError("Error: ${t.message}")
            }
        })
    }
}