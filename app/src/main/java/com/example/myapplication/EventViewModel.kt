package com.example.myapplication
import androidx.lifecycle.ViewModel

class Categoria(val nombre: String)
class Evento(val titulo: String, val descripcion: String, val categoria: String)

class EventViewModel : ViewModel() {
    var categorias = mutableListOf<Categoria>()
    var eventos = mutableListOf<Evento>()
}