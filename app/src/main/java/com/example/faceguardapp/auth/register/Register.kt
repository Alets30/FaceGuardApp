package com.example.faceguardapp.auth.register

import android.graphics.Bitmap
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.faceguardapp.auth.register.body.Body
import com.example.faceguardapp.auth.register.header.Header

@Composable
fun RegisterScreen(modifier: Modifier, navigationController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xff02416d))
            .padding(top = 16.dp)
    ) {
        val imageBase64 = remember { mutableStateOf<String?>(null) }
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(3f),
            onPhotoSelected = {
                imageBase64.value = it
            }
        )
        Body(
            modifier = Modifier
                .weight(7f),
            navigationController,
            imageBase64.value.toString()
        )
    }
}