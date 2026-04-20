package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.R // <-- Esta línea soluciona los errores de los textos

@Composable
fun FormCategoriaScreen(viewModel: EventViewModel, navController: NavHostController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = viewModel.nombreCategoria,
                onValueChange = { viewModel.nombreCategoria = it },
                label = { Text(stringResource(R.string.nombre_categoria)) }
            )

            if (viewModel.errorCategoria.isNotEmpty()) {
                Text(
                    text = viewModel.errorCategoria,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.size(22.dp))

            Button(onClick = {
                if (viewModel.addCategoria()) {
                    navController.popBackStack()
                }
            }) {
                Text(stringResource(R.string.btn_guardar))
            }
        }
    }
}

@Composable
fun FormEventoScreen(viewModel: EventViewModel, navController: NavHostController) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            TextField(
                value = viewModel.tituloEvento,
                onValueChange = { viewModel.tituloEvento = it },
                label = { Text(stringResource(R.string.titulo_evento)) }
            )

            Spacer(modifier = Modifier.size(16.dp))

            TextField(
                value = viewModel.descEvento,
                onValueChange = { viewModel.descEvento = it },
                label = { Text(stringResource(R.string.desc_evento)) }
            )

            if (viewModel.errorEvento.isNotEmpty()) {
                Text(
                    text = viewModel.errorEvento,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.size(22.dp))

            Button(onClick = {
                val categoriaAsignada = if (viewModel.categorias.isNotEmpty()) viewModel.categorias[0].nombre else "General"

                if (viewModel.addEvento(categoriaAsignada)) {
                    navController.popBackStack()
                }
            }) {
                Text(stringResource(R.string.btn_guardar))
            }
        }
    }
}
