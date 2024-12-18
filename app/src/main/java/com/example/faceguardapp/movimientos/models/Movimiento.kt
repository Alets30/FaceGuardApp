package com.example.faceguardapp.movimientos.models

data class Movimiento(
    val tipo: String,
    val hora: String,
    val fecha: String,
    val usuario: Usuario?,
    val puerta: Puerta?
)

data class Usuario(
    val id: Int,
    val nombre: String
)

data class Puerta(
    val id: Int,
    val nombre: String
)