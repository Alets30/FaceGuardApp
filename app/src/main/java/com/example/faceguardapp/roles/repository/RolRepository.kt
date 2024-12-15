package com.example.faceguardapp.roles.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.roles.models.Rol
import retrofit2.Response

class RolRepository {
    private val api = RetrofitClient.apiRoles

    suspend fun obtenerRoles(): Response<List<Rol>> {
        return api.obtenerRoles()
    }

    suspend fun crearRol(rol: Rol): Response<Rol> {
        return api.crearRol(rol)
    }

    suspend fun actualizarRol(id: Int, rol: Rol): Response<Rol> {
        return api.actualizarRol(id, rol)
    }

    suspend fun eliminarRol(id: Int): Response<Void> {
        return api.eliminarRol(id)
    }
}