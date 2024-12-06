package com.example.faceguardapp.login.body

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp

@Composable
fun LoginButton(loginEnable: Boolean) {
    Button(
        onClick = {}, enabled = loginEnable, modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        Text(text = "Log In")
    }
}