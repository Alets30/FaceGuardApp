package com.example.faceguardapp.areas.data

import com.example.faceguardapp.areas.models.Area
import com.example.faceguardapp.areas.models.AreaRequest
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.*

interface AreaApiService {
    @GET("api/areas/list")
    suspend fun obtenerAreas(): Response<List<Area>>

    @POST("api/areas/create")
    suspend fun crearArea(@Body area: AreaRequest): Response<AreaRequest>

    @PUT("api/areas/update/{id}")
    suspend fun actualizarArea(@Path("id") id: Int, @Body area: AreaRequest): Response<AreaRequest>

    @DELETE("api/areas/delete/{id}")
    suspend fun eliminarArea(@Path("id") id: Int): Response<Void>
}