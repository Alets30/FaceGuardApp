package com.example.faceguardapp.usuarios.models

data class Profile(
    val id: Int,
    val nombre: String,
    val apellido_p: String,
    val apellido_m: String,
    val fecha_nacimiento: String,
    val sexo: String,
    val nss: String,
    val telefono: String,
    val direccion: String,
    val estado: String,
    val ciudad: String,
    val user_id: Int,
    val usuario: Usuario
)

data class Usuario(
    val is_superuser: Boolean,
    val is_staff: Boolean
)