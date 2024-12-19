package com.example.faceguardapp.usuarios.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faceguardapp.usuarios.models.Puerta
import com.example.faceguardapp.usuarios.models.PuertaRequest
import com.example.faceguardapp.usuarios.repository.PuertaRepository
import kotlinx.coroutines.launch

class PuertaViewModel : ViewModel() {
    private val repository = PuertaRepository()

    private val _puertas = MutableLiveData<List<Puerta>>()
    val puertas: LiveData<List<Puerta>> get() = _puertas

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    private val _accesoVerificado = MutableLiveData<Boolean?>()
    val accesoVerificado: MutableLiveData<Boolean?> get() = _accesoVerificado

    fun cargarPuertas() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerPuertas()
                if (response.isSuccessful) {
                    _puertas.postValue(response.body())
                } else {
                    _mensajeEstado.postValue("Error al cargar puertas")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun crearPuerta(puerta: PuertaRequest) {
        viewModelScope.launch {
            try {
                val response = repository.crearPuerta(puerta)
                if (response.isSuccessful) {
                    cargarPuertas() // Recargar puertas después de la creación
                } else {
                    _mensajeEstado.postValue("Error al crear puerta")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun actualizarPuerta(id: Int, puerta: PuertaRequest) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarPuerta(id, puerta)
                if (response.isSuccessful) {
                    cargarPuertas() // Recargar puertas después de la actualización
                } else {
                    _mensajeEstado.postValue("Error al actualizar puerta")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun eliminarPuerta(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarPuerta(id)
                if (response.isSuccessful) {
                    cargarPuertas() // Recargar puertas después de la eliminación
                } else {
                    _mensajeEstado.postValue("Error al eliminar puerta")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun verificarRostroAcceso(puertaId: Int, username: String, photoBase64: String) {
        viewModelScope.launch {
            try {
                val response = repository.verificarRostroAcceso(puertaId, username, photoBase64)
                if (response.isSuccessful) {
                    response.body()?.let {
                        _accesoVerificado.postValue(it.acceso)
                        _mensajeEstado.postValue("Acceso ${if (it.acceso == true) "concedido" else "denegado"}")
                    }
                } else {
                    _mensajeEstado.postValue("Error al verificar rostro")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }
}