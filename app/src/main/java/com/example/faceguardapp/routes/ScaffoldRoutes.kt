package com.example.faceguardapp.routes

sealed class ScaffoldRoutes(val route: String) {
    object Notificaciones: ScaffoldRoutes("Notificaciones")
    object Reconocimiento: ScaffoldRoutes("Reconocimiento")
    object Roles: ScaffoldRoutes("Roles")
}