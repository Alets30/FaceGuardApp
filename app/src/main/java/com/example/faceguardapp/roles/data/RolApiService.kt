package com.example.faceguardapp.roles.data

import com.example.faceguardapp.roles.models.ApiResponse
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
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

    @GET("api/profiles/{profile_id}/unassigned-roles/")
    suspend fun obtenerRolesNoAsignados(@Path("profile_id") profileId: Int): Response<List<Rol>>

    @POST("api/profiles/{profile_id}/assign-role/")
    suspend fun asignarRol(
        @Path("profile_id") profileId: Int,
        @Body roleAssignRequest: RoleAssignRequest
    ): Response<ApiResponse>
}