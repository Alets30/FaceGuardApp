package com.example.faceguardapp.movimientos.data

import com.example.faceguardapp.movimientos.models.Movimiento
import retrofit2.Call
import retrofit2.http.GET

interface MovimientoApiService {

    @GET("api/movimientos/")
    fun getMovimientos(): Call<List<Movimiento>>
}