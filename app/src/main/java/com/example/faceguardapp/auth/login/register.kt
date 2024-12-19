package com.example.faceguardapp.auth.login

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.faceguardapp.auth.login.body.Body
import com.example.faceguardapp.auth.login.header.Header

@Composable
fun LoginScreen(modifier: Modifier, navigationController: NavController) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xff02416d))
            .padding(top = 16.dp)
    ) {
        Header(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(3f)
        )
        Body(
            modifier = Modifier
                .weight(7f),
            navigationController
        )
    }
}