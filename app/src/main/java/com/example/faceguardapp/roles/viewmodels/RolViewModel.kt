package com.example.faceguardapp.roles.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
import com.example.faceguardapp.roles.repository.RolRepository
import kotlinx.coroutines.launch

class RolViewModel : ViewModel() {
    private val repository = RolRepository()

    private val _roles = MutableLiveData<List<Rol>>()
    val roles: LiveData<List<Rol>> get() = _roles

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    fun cargarRoles() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerRoles()
                if (response.isSuccessful) {
                    _roles.postValue(response.body())
                } else {
                    _mensajeEstado.postValue("Error al cargar roles")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun crearRol(rol: Rol) {
        viewModelScope.launch {
            try {
                val response = repository.crearRol(rol)
                if (response.isSuccessful) {
                    cargarRoles() // Recargar roles después de la creación
                } else {
                    _mensajeEstado.postValue("Error al crear rol")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun actualizarRol(id: Int, rol: Rol) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarRol(id, rol)
                if (response.isSuccessful) {
                    cargarRoles() // Recargar roles después de la actualización
                } else {
                    _mensajeEstado.postValue("Error al actualizar rol")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun eliminarRol(id: Int) {
        viewModelScope.launch {
            try {
                val response = repository.eliminarRol(id)
                if (response.isSuccessful) {
                    cargarRoles() // Recargar roles después de la eliminación
                } else {
                    _mensajeEstado.postValue("Error al eliminar rol")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }
}