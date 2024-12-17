package com.example.faceguardapp.usuarios.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.usuarios.models.Profile
import retrofit2.Response

class ProfileRepository {
    private val api = RetrofitClient.apiUsuarios

    suspend fun obtenerUsuarios(): Response<List<Profile>> {
        return api.obtenerUsuarios()
    }

    suspend fun actualizarUsuario(id: Int, profile: Profile): Response<Profile> {
        return api.actualizarUsuario(id, profile)
    }
}