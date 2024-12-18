package com.example.faceguardapp.areas.models

import com.example.faceguardapp.zonas.model.Zona

data class Area (
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val activo: Boolean,
    val zonas: List<Zona>
)

data class AreaRequest (
    val nombre: String,
    val descripcion: String,
    val activo: Boolean,
    val zonas: List<Int>
)