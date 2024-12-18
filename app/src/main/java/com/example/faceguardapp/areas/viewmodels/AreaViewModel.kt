package com.example.faceguardapp.areas.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faceguardapp.areas.models.Area
import com.example.faceguardapp.areas.models.AreaRequest
import com.example.faceguardapp.areas.repository.AreaRepository
import kotlinx.coroutines.launch

class AreaViewModel : ViewModel() {
    private val repository = AreaRepository()

    private val _areas = MutableLiveData<List<Area>>()
    val areas: LiveData<List<Area>> get() = _areas

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    fun cargarAreas() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerAreas()
                if (response.isSuccessful) {
                    _areas.postValue(response.body())
                } else {
                    _mensajeEstado.postValue("Error al cargar areas")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun crearArea(area: AreaRequest) {
        viewModelScope.launch {
            try {
                val response = repository.crearArea(area)
                if (response.isSuccessful) {
                    cargarAreas() // Recargar areas después de la creación
                } else {
                    _mensajeEstado.postValue("Error al crear el área")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun actualizarArea(id: Int, area: AreaRequest) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarArea(id, area)
                //println(response)
                if (response.isSuccessful) {
                    cargarAreas() // Recargar áreas después de la actualización
                } else {
                    _mensajeEstado.postValue("Error al actualizar el área")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun eliminarArea(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarArea(id)
                if (response.isSuccessful) {
                    cargarAreas() // Recargar areas después de la eliminación
                } else {
                    _mensajeEstado.postValue("Error al eliminar el área")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }
}