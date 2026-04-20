package com.example.myapplication

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(viewModel: EventViewModel, navController: NavHostController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Fila superior con los botones de navegación
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { navController.navigate(FormCategoria) }) {
                    // Usamos los strings definidos en el primer paso
                    Text(stringResource(R.string.btn_crear_cat))
                }
                Button(onClick = { navController.navigate(FormEvento) }) {
                    Text(stringResource(R.string.btn_crear_evento))
                }
            }

            // Cuadrícula para mostrar los eventos
            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 1), // Usamos 1 columna para que las tarjetas ocupen el ancho completo
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Iteramos sobre la lista de eventos del ViewModel
                items(viewModel.eventos) { evento ->
                    EventCard(evento = evento, onClick = {
                        // Navegamos a la vista de detalle pasando los datos del evento
                        navController.navigate(
                            DetalleEvento(
                                titulo = evento.titulo,
                                descripcion = evento.descripcion,
                                categoria = evento.categoria
                            )
                        )
                    })
                }
            }
        }
    }
}