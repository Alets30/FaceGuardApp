package com.example.faceguardapp.roles.data

import com.example.faceguardapp.roles.models.Rol
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface RolApiService {
    @GET("api/roles/list")
    suspend fun obtenerRoles(): Response<List<Rol>>

    @POST("api/roles/create")
    suspend fun crearRol(@Body rol: Rol): Response<Rol>

    @PUT("api/roles/update/{id}")
    suspend fun actualizarRol(@Path("id") id: Int, @Body rol: Rol): Response<Rol>

    @DELETE("api/roles/delete/{id}")
    suspend fun eliminarRol(@Path("id") id: Int): Response<Void>
}