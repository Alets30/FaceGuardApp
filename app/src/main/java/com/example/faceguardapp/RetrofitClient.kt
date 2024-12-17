package com.example.faceguardapp

import AuthApiService
import ReconocimientoApiService
import com.example.faceguardapp.notificaciones.data.NotificacionApiService
import com.example.faceguardapp.roles.data.RolApiService
import com.example.faceguardapp.usuarios.models.Profile
import com.example.faceguardapp.zonas.data.ZonaApiService
import com.example.faceguardapp.usuarios.data.ProfileApiService
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

    val apiRoles: RolApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(RolApiService::class.java)
    }

    val apiZonas: ZonaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ZonaApiService::class.java)
    }

    val apiUsuarios: ProfileApiService by lazy {
        Retrofit.Builder()
            .baseUrl(Constantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
            .create(ProfileApiService::class.java)
    }

    fun setToken(newToken: String) {
        token = newToken
    }
}