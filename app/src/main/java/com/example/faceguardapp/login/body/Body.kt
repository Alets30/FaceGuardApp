package com.example.faceguardapp.login.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Body(modifier: Modifier) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var isLoginEnable by rememberSaveable { mutableStateOf(false) }
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
        LoginButton(isLoginEnable)
    }
}