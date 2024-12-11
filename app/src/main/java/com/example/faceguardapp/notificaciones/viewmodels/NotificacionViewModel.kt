package com.example.faceguardapp.notificaciones.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faceguardapp.notificaciones.models.Notificacion
import com.example.faceguardapp.notificaciones.repository.NotificacionRepository
import kotlinx.coroutines.launch

class NotificacionViewModel : ViewModel() {
    private val repositorio = NotificacionRepository()

    private val _notificaciones = MutableLiveData<List<Notificacion>>()
    val notificaciones: LiveData<List<Notificacion>> get() = _notificaciones

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado


    fun cargarNotificaciones() {
        viewModelScope.launch {
            try {
                val response = repositorio.obtenerNotificaciones()
                if (response.isSuccessful) {
                    _notificaciones.postValue(response.body())
                } else {
                    _mensajeEstado.postValue("Error al cargar notificaciones")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun marcarComoLeida(id: Int) {
        viewModelScope.launch {
            try {
                val response = repositorio.marcarNotificacionLeida(id)
                if (response.isSuccessful) {
                    cargarNotificaciones() // Recargar las notificaciones
                } else {
                    _mensajeEstado.postValue("Error al marcar como leída")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }
}
