package com.example.faceguardapp.zonas.repository

import com.example.faceguardapp.RetrofitClient
import com.example.faceguardapp.zonas.model.Zona
import retrofit2.Response

class ZonaRepository {
    private val api = RetrofitClient.apiZonas

    suspend fun obtenerZonas(): Response<List<Zona>> {
        return api.obtenerZonas()
    }

    suspend fun crearZona(zona: Zona): Response<Zona> {
        return api.crearZona(zona)
    }

    suspend fun actualizarZona(id: Int, zona: Zona): Response<Zona> {
        return api.actualizarZona(id, zona)
    }

    suspend fun eliminarZona(id: Int): Response<Void> {
        return api.eliminarZona(id)
    }
}