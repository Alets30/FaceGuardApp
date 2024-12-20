package com.example.faceguardapp.auth.register.body

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun RegisterText(modifier: Modifier){
    Text(text = "Register",
        modifier = modifier.fillMaxWidth(),
        color = Color(0xff02416d),
        fontWeight = FontWeight.W300,
        fontSize = 40.sp,
        letterSpacing = 1.sp,
        textAlign = TextAlign.Center
    )
}