package com.example.faceguardapp.zonas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.repository.RolRepository
import com.example.faceguardapp.zonas.model.Zona
import com.example.faceguardapp.zonas.repository.ZonaRepository
import kotlinx.coroutines.launch

class ZonaViewModel : ViewModel() {
    private val repository = ZonaRepository()

    private val _zonas = MutableLiveData<List<Zona>>()
    val zonas: LiveData<List<Zona>> get() = _zonas

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    fun cargarZonas() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerZonas()
                if (response.isSuccessful) {
                    _zonas.postValue(response.body())
                } else {
                    _mensajeEstado.postValue("Error al cargar zonas")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun crearZona(zona: Zona) {
        viewModelScope.launch {
            try {
                val response = repository.crearZona(zona)
                if (response.isSuccessful) {
                    cargarZonas() // Recargar zonas después de la creación
                } else {
                    _mensajeEstado.postValue("Error al crear la zona")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun actualizarZona(id: Int, zona: Zona) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarZona(id, zona)
                if (response.isSuccessful) {
                    cargarZonas() // Recargar zonas después de la actualización
                } else {
                    _mensajeEstado.postValue("Error al actualizar la zona")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun eliminarZona(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarZona(id)
                if (response.isSuccessful) {
                    cargarZonas() // Recargar zonas después de la eliminación
                } else {
                    _mensajeEstado.postValue("Error al eliminar la zona")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }
}