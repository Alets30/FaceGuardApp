package com.example.faceguardapp.zonas.data

import com.example.faceguardapp.zonas.model.Zona
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface ZonaApiService {
    @GET("api/zonas/list")
    suspend fun obtenerZonas(): Response<List<Zona>>

    @POST("api/zonas/create")
    suspend fun crearZona(@Body rol: Zona): Response<Zona>

    @PUT("api/zonas/update/{id}")
    suspend fun actualizarZona(@Path("id") id: Int, @Body rol: Zona): Response<Zona>

    @DELETE("api/zonas/delete/{id}")
    suspend fun eliminarZona(@Path("id") id: Int): Response<Void>
}