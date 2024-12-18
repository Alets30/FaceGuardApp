package com.example.faceguardapp.usuarios.data

import com.example.faceguardapp.roles.models.ApiResponse
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
import com.example.faceguardapp.usuarios.models.Puerta
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface PuertasApiService {
    @GET("api/puertas/list")
    suspend fun obtenerPuertas(): Response<List<Puerta>>

    @POST("api/puertas/create")
    suspend fun crearPuerta(@Body puerta: Puerta): Response<Puerta>

    @PUT("api/puertas/update/{id}")
    suspend fun actualizarPuerta(@Path("id") id: Int, @Body puerta: Puerta): Response<Puerta>

    @DELETE("api/puertas/delete/{id}")
    suspend fun eliminarPuerta(@Path("id") id: Int): Response<Void>
}