package com.example.faceguardapp

import AuthApiService
import ReconocimientoApiService
import com.example.faceguardapp.notificaciones.data.NotificacionApiService
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    private var token: String = ""

    val headerInterceptor = Interceptor { chain ->
        val request = chain.request().newBuilder()
            .addHeader("Authorization", "token $token")
            .addHeader("Content-Type", "application/json")
            .build()
        chain.proceed(request)
    }

    // Configurar OkHttpClient con el interceptor
    val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(headerInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(600, TimeUnit.SECONDS)
        .build()

    val api: AuthApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(AuthApiService::class.java)
    }

    val apiReconocimiento: ReconocimientoApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.NUBE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ReconocimientoApiService::class.java)
    }

    val apinotificacion: NotificacionApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(NotificacionApiService::class.java)
    }

    fun setToken(newToken: String) {
        token = newToken
    }
}