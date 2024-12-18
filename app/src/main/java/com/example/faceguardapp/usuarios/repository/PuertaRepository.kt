package com.example.faceguardapp.usuarios.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.roles.models.ApiResponse
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
import com.example.faceguardapp.usuarios.models.Puerta
import retrofit2.Response

class PuertaRepository {
    private val api = RetrofitClient.apiPuertas

    suspend fun obtenerPuertas(): Response<List<Puerta>> {
        return api.obtenerPuertas()
    }

    suspend fun crearPuerta(puerta: Puerta): Response<Puerta> {
        return api.crearPuerta(puerta)
    }

    suspend fun actualizarPuerta(id: Int, puerta: Puerta): Response<Puerta> {
        return api.actualizarPuerta(id, puerta)
    }

    suspend fun eliminarPuerta(id: Int): Response<Void> {
        return api.eliminarPuerta(id)
    }

    /*
    suspend fun obtenerRolesNoAsignados(profileId: Int): Response<List<Rol>> {
        return api.obtenerRolesNoAsignados(profileId)
    }

    suspend fun asignarRol(profileId: Int, roleAssignRequest: RoleAssignRequest): Response<ApiResponse> {
        return api.asignarRol(profileId, roleAssignRequest)
    }
    */
}