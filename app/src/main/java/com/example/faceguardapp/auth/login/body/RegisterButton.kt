package com.example.faceguardapp.auth.login.body

import AuthTokenStore
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
import com.example.faceguardapp.routes.MainRoutes

@Composable
fun RegisterButton(
    navigationController: NavController
) {
    Button(
        onClick = {
            navigationController.navigate(MainRoutes.Register.route)
        }, modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text = "Register")
    }
}