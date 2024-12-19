package com.example.faceguardapp.usuarios.models

import com.example.faceguardapp.areas.models.Area

data class Puerta (
    val id: Int,
    val nombre: String,
    val descripcion: String,
    val activo: Boolean,
    val areas: List<Area>  // Aquí, áreas es una lista de objetos `Area`, similar a cómo `zonas` es una lista en `Area`
)

data class PuertaRequest (
    val nombre: String,
    val descripcion: String,
    val activo: Boolean,
    val areas: List<Int>  // Aquí, áreas es una lista de IDs de las áreas asociadas, como en `AreaRequest`
)