package com.example.faceguardapp.notificaciones.models

data class Notificacion(
    val id: Int,
    val usuario: String, // Según el backend, este campo podría ser solo el ID o un username
    val mensaje: String,
    val leida: Boolean,
    val fecha_creacion: String
)