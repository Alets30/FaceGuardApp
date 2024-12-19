package com.example.faceguardapp.usuarios.data

import com.example.faceguardapp.roles.models.ApiResponse
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
import com.example.faceguardapp.usuarios.models.Puerta
import com.example.faceguardapp.usuarios.models.PuertaRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface PuertasApiService {
    @GET("api/puertas/list")
    suspend fun obtenerPuertas(): Response<List<Puerta>>  // Lista de puertas

    @POST("api/puertas/create")
    suspend fun crearPuerta(@Body puerta: PuertaRequest): Response<PuertaRequest>  // Crear puerta, el cuerpo es PuertaRequest

    @PUT("api/puertas/update/{id}")
    suspend fun actualizarPuerta(@Path("id") id: Int, @Body puerta: PuertaRequest): Response<PuertaRequest>  // Actualizar puerta, el cuerpo es PuertaRequest

    @DELETE("api/puertas/delete/{id}")
    suspend fun eliminarPuerta(@Path("id") id: Int): Response<Void>  // Eliminar puerta
}