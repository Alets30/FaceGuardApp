package com.example.faceguardapp.routes

sealed class ScaffoldRoutes(val route: String) {
    object Notificaciones: ScaffoldRoutes("Notificaciones")
    object Reconocimiento: ScaffoldRoutes("Reconocimiento")
    object Roles: ScaffoldRoutes("Roles")
    object Zonas: ScaffoldRoutes("Zonas")
    object Usuarios: ScaffoldRoutes("Usuarios")
    object EditProfile : ScaffoldRoutes("EditProfile/{id}") {
        fun createRoute(id: Int) = "EditProfile/$id"
    }
    object Movimientos: ScaffoldRoutes("Movimientos")
}