package com.example.faceguardapp.movimientos.viewmodels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.faceguardapp.movimientos.models.Movimiento
import com.example.faceguardapp.movimientos.repository.MovimientoRepository

class MovimientoViewModel : ViewModel() {

    private val repository = MovimientoRepository()

    private val _movimientos = MutableLiveData<List<Movimiento>>()
    val movimientos: LiveData<List<Movimiento>> get() = _movimientos

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    fun fetchMovimientos() {
        repository.fetchMovimientos().observeForever { result ->
            result.onSuccess { movimientos ->
                _movimientos.value = movimientos
            }.onFailure { throwable ->
                _error.value = "Error: ${throwable.localizedMessage}"
            }
        }
    }
}