package com.example.myapplication

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

// Definición de rutas
@Serializable
object Home

@Serializable
object FormCategoria

@Serializable
object FormEvento

@Serializable
data class EventosCategoria(val nombreCategoria: String)

@Serializable
data class DetalleEvento(val titulo: String, val descripcion: String, val categoria: String)

@SuppressLint("ViewModelConstructorInComposable")
@Composable
fun Navigation() {
    val navController = rememberNavController()
    // Es recomendable obtener el ViewModel de forma que sobreviva a la recreación
    val viewModel = EventViewModel()

    NavHost(
        navController = navController, 
        startDestination = Home
    ) {
        composable<Home> {
            HomeScreen(viewModel = viewModel, navController = navController)
        }

        composable<FormCategoria> {
            FormCategoriaScreen(viewModel = viewModel, navController = navController)
        }

        composable<FormEvento> {
            FormEventoScreen(viewModel = viewModel, navController = navController)
        }

        composable<EventosCategoria> { backStackEntry ->
            val args = backStackEntry.toRoute<EventosCategoria>()
            EventosPorCategoriaScreen(
                categoria = args.nombreCategoria,
                viewModel = viewModel,
                navController = navController
            )
        }

        composable<DetalleEvento> { backStackEntry ->
            val args = backStackEntry.toRoute<DetalleEvento>()
            DetalleScreen(
                titulo = args.titulo,
                descripcion = args.descripcion,
                categoria = args.categoria,
                navController = navController
            )
        }
    }
}
