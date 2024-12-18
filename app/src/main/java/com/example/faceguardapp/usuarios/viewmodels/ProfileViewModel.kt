package com.example.faceguardapp.usuarios.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.faceguardapp.roles.models.Rol
import com.example.faceguardapp.roles.models.RoleAssignRequest
import com.example.faceguardapp.roles.repository.RolRepository
import com.example.faceguardapp.usuarios.models.Profile
import com.example.faceguardapp.usuarios.repository.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {
    private val repository = ProfileRepository()
    private val repositoryRol = RolRepository()

    private val _usuarios = MutableLiveData<List<Profile>>()
    val usuarios: LiveData<List<Profile>> get() = _usuarios

    private val _mensajeEstado = MutableLiveData<String>()
    val mensajeEstado: LiveData<String> get() = _mensajeEstado

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                val response = repository.obtenerUsuarios()
                if (response.isSuccessful) {
                    _usuarios.postValue(response.body())
                } else {
                    _mensajeEstado.postValue("Error al cargar usuarios")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun actualizarUsuario(id: Int, profile: Profile) {
        viewModelScope.launch {
            try {
                val response = repository.actualizarUsuario(id, profile)
                if (response.isSuccessful) {
                    _mensajeEstado.postValue("Usuario actualizado correctamente")
                } else {
                    _mensajeEstado.postValue("Error al actualizar usuario: ${response.errorBody()?.string()}")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    private val _rolesNoAsignados = MutableLiveData<List<Rol>>()
    val rolesNoAsignados: LiveData<List<Rol>> get() = _rolesNoAsignados

    fun obtenerRolesNoAsignados(profileId: Int) {
        viewModelScope.launch {
            try {
                val response = repositoryRol.obtenerRolesNoAsignados(profileId)
                if (response.isSuccessful) {
                    _rolesNoAsignados.postValue(response.body())
                } else {
                    _mensajeEstado.postValue("Error al cargar roles no asignados")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }

    fun asignarRol(profileId: Int, roleId: Int, fechaVencimiento: String?) {
        viewModelScope.launch {
            try {
                val response = repositoryRol.asignarRol(
                    profileId,
                    RoleAssignRequest(roleId, fechaVencimiento)
                )
                if (response.isSuccessful) {
                    _mensajeEstado.postValue("Rol asignado correctamente")
                } else {
                    _mensajeEstado.postValue("Error al asignar rol")
                }
            } catch (e: Exception) {
                _mensajeEstado.postValue("Error de red: ${e.message}")
            }
        }
    }
}