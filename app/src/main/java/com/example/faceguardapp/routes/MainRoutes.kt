package com.example.faceguardapp.routes

sealed class MainRoutes(val route: String) {
    object Home: MainRoutes("Home")
    object Login: MainRoutes("Login")
    object Notificaciones: MainRoutes("Notificaciones")
    object Reconocimiento: MainRoutes("Reconocimiento")
}