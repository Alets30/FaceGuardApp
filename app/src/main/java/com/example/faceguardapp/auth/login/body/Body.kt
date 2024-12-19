package com.example.faceguardapp.auth.login.body

import AuthTokenStore
import ProfileResponse
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.routes.MainRoutes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Body(modifier: Modifier, navigationController: NavController) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    //var authToken = AuthTokenStore(LocalContext.current).getAuthToken.collectAsState(initial = "")
    //var username = UsernameStore(LocalContext.current).getUsername.collectAsState(initial = "")
    var errorMessage by rememberSaveable { mutableStateOf("") }
    var isLoginEnable by rememberSaveable { mutableStateOf(true) }
    var authToken =
        AuthTokenStore(LocalContext.current).getAuthToken.collectAsState(initial = "").value.toString()
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = authToken) {
        if (authToken.isNotEmpty()) {
            AuthUser(scope, authToken, navigationController)
        }
    }

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 32.dp))
            .background(Color(0xfff8f8ec))
            .padding(16.dp)
            .padding(top = 32.dp)
    )
    {
        LoginText(Modifier)
        Spacer(modifier = Modifier.size(48.dp))
        Email(email, { email = it }, Modifier.background(Color.White))
        Spacer(modifier = Modifier.size(32.dp))
        Password(password, { password = it }, Modifier)
        Spacer(modifier = Modifier.size(64.dp))
        LoginButton(
            isLoginEnable,
            email,
            password,
            { error -> errorMessage = error },
            navigationController
        )
        Spacer(modifier = Modifier.size(16.dp))
        RegisterButton(navigationController)
        Spacer(modifier = Modifier.size(16.dp))
        Text(errorMessage, color = Color.Red)
        //username.value?.let { Text(it, color = Color.Red) }
    }
}

fun AuthUser(
    scope: CoroutineScope,
    tokenStore: String,
    navigationController: NavController,
) {
    RetrofitClient.setToken(tokenStore)
    RetrofitClient.api.profile().enqueue(object : Callback<ProfileResponse> {
        override fun onResponse(call: Call<ProfileResponse>, response: Response<ProfileResponse>) {
            if (response.isSuccessful) {
                response.body()?.let { profileResponse ->
                    scope.launch {
                        navigationController.navigate(MainRoutes.Home.route)
                    }
                }
            }
        }

        override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {

        }
    })
}