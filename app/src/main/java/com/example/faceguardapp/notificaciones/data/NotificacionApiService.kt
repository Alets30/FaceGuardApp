package com.example.faceguardapp.notificaciones.data
import com.example.faceguardapp.notificaciones.models.Notificacion
import retrofit2.Response
import retrofit2.http.*

interface NotificacionApiService {
    // Endpoint para obtener la lista de notificaciones
    @GET("api/notificaciones/list")
    suspend fun obtenerNotificaciones(): Response<List<Notificacion>>

    // Endpoint para marcar una notificación como leída
    @PATCH("api/notificaciones/marcar-leida/{id}")
    suspend fun marcarNotificacionLeida(@Path("id") id: Int): Response<Map<String, String>>
}