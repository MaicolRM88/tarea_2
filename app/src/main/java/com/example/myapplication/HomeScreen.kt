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
import androidx.compose.material3.MaterialTheme
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

            val eventosAgrupados = viewModel.eventos.groupBy { it.categoria }

            LazyVerticalGrid(
                columns = GridCells.Fixed(count = 1),
                modifier = Modifier.padding(top = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Iteramos sobre cada grupo (Categoría -> Lista de eventos)
                eventosAgrupados.forEach { (categoria, listaDeEventos) ->
                    // Título de la Categoría
                    item {
                        Text(
                            text = categoria,
                            style = MaterialTheme.typography.headlineSmall,
                            modifier = Modifier.padding(vertical = 8.dp),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Eventos pertenecientes a esa categoría
                    items(listaDeEventos) { evento ->
                        EventCard(evento = evento, onClick = {
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
}