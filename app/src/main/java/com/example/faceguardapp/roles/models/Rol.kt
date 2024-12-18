package com.example.faceguardapp.roles.models

data class Rol (
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val activo: Boolean
)

// RoleAssignRequest.kt
data class RoleAssignRequest(
    val role_id: Int,
    val fecha_vencimiento: String? = null
)

// ApiResponse.kt
data class ApiResponse(
    val message: String
)