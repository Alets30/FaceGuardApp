package com.example.faceguardapp.login.body

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import java.time.format.TextStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Email(
    email: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier
) {
    TextField(
        value = email,
        onValueChange = { onTextChanged(it) },
        modifier = modifier.fillMaxWidth().clip(RoundedCornerShape(12.dp)),
        singleLine = true,
        placeholder = { Text(text = "Email") },
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.White,
            focusedContainerColor = Color.White,
            unfocusedTextColor = Color.Black,
            focusedTextColor = Color.Black
        ),
    )
}