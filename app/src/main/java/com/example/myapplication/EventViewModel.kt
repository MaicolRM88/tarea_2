package com.example.myapplication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class Categoria(val nombre: String)
class Evento(val titulo: String, val descripcion: String, val categoria: String)

class EventViewModel : ViewModel() {
    var categorias = mutableListOf<Categoria>()
    var eventos = mutableListOf<Evento>()

    var nombreCategoria by mutableStateOf("")
    var errorCategoria by mutableStateOf("")

    var tituloEvento by mutableStateOf("")
    var descEvento by mutableStateOf("")
    var errorEvento by mutableStateOf("")

    fun addCategoria(): Boolean {
        if (nombreCategoria.isNotEmpty()) {
            categorias.add(Categoria(nombreCategoria))
            nombreCategoria = ""
            errorCategoria = ""
            return true
        } else {
            errorCategoria = "El nombre no puede estar vacío"
            return false
        }
    }

    fun addEvento(catSeleccionada: String): Boolean {
        if (tituloEvento.isNotEmpty() && descEvento.isNotEmpty()) {
            eventos.add(Evento(tituloEvento, descEvento, catSeleccionada))
            tituloEvento = ""
            descEvento = ""
            errorEvento = ""
            return true
        } else {
            errorEvento = "Todos los campos son obligatorios"
            return false
        }
    }
}