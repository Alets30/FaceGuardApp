package com.example.faceguardapp.movimientos.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.faceguardapp.RetrofitClient.apiMovimientos
import com.example.faceguardapp.movimientos.models.Movimiento
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MovimientoRepository() {

    private val api = apiMovimientos
    fun fetchMovimientos(): LiveData<Result<List<Movimiento>>> {
        val result = MutableLiveData<Result<List<Movimiento>>>()

        api.getMovimientos().enqueue(object : Callback<List<Movimiento>> {
            override fun onResponse(
                call: Call<List<Movimiento>>,
                response: Response<List<Movimiento>>
            ) {
                if (response.isSuccessful) {
                    result.postValue(Result.success(response.body() ?: emptyList()))
                } else {
                    result.postValue(Result.failure(Throwable("Error: ${response.code()}")))
                }
            }

            override fun onFailure(call: Call<List<Movimiento>>, t: Throwable) {
                result.postValue(Result.failure(t))
            }
        })

        return result
    }
}