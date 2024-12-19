package com.example.faceguardapp.usuarios.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.roles.models.ApiResponse
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
import com.example.faceguardapp.usuarios.models.Puerta
import com.example.faceguardapp.usuarios.models.PuertaRequest
import com.example.faceguardapp.usuarios.models.VerificarRostroRequest
import com.example.faceguardapp.usuarios.models.VerificarRostroResponse
import retrofit2.Response

class PuertaRepository {
    private val api = RetrofitClient.apiPuertas

    suspend fun obtenerPuertas(): Response<List<Puerta>> {
        return api.obtenerPuertas()
    }

    suspend fun crearPuerta(puerta: PuertaRequest): Response<PuertaRequest> {
        return api.crearPuerta(puerta)
    }

    suspend fun actualizarPuerta(id: Int, puerta: PuertaRequest): Response<PuertaRequest> {
        return api.actualizarPuerta(id, puerta)
    }

    suspend fun eliminarPuerta(id: Int): Response<Void> {
        return api.eliminarPuerta(id)
    }

    suspend fun verificarRostroAcceso(puertaId: Int, username: String, photoBase64: String): Response<VerificarRostroResponse> {
        val request = VerificarRostroRequest(username, photoBase64)
        return api.verificarRostroAcceso(puertaId, request)
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