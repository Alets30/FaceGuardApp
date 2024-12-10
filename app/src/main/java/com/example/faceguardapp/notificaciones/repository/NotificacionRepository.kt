package com.example.faceguardapp.notificaciones.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.notificaciones.models.Notificacion
import retrofit2.Response

class NotificacionRepository {
    private val api = RetrofitClient.apinotificacion

    suspend fun obtenerNotificaciones(): Response<List<Notificacion>> {
        return api.obtenerNotificaciones()
    }

    suspend fun marcarNotificacionLeida(id: Int): Response<Map<String, String>> {
        return api.marcarNotificacionLeida(id)
    }
}