package com.example.faceguardapp.roles.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.roles.models.ApiResponse
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
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

    suspend fun obtenerRolesNoAsignados(profileId: Int): Response<List<Rol>> {
        return api.obtenerRolesNoAsignados(profileId)
    }

    suspend fun asignarRol(profileId: Int, roleAssignRequest: RoleAssignRequest): Response<ApiResponse> {
        return api.asignarRol(profileId, roleAssignRequest)
    }
}