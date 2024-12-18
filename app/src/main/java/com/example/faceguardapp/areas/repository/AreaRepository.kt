package com.example.faceguardapp.areas.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.areas.models.Area
import com.example.faceguardapp.areas.models.AreaRequest
import retrofit2.Response

class AreaRepository {
    private val api = RetrofitClient.apiAreas

    suspend fun obtenerAreas(): Response<List<Area>> {
        return api.obtenerAreas()
    }

    suspend fun crearArea(area: AreaRequest): Response<Area> {
        return api.crearArea(area)
    }

    suspend fun actualizarArea(id: Int, area: AreaRequest): Response<AreaRequest> {
        return api.actualizarArea(id, area)
    }

    suspend fun eliminarArea(id: Int): Response<Void> {
        return api.eliminarArea(id)
    }
}