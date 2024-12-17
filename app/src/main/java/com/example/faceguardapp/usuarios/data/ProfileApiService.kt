package com.example.faceguardapp.usuarios.data

import com.example.faceguardapp.usuarios.models.Profile
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface ProfileApiService {
    @GET("api/profiles/list")
    suspend fun obtenerUsuarios(): Response<List<Profile>>

    @PUT("api/profiles/update/{id}/")
    suspend fun actualizarUsuario(
        @Path("id") id: Int,
        @Body profile: Profile
    ): Response<Profile>
}