package com.example.myapplication
import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import kotlinx.serialization.Serializable

@Serializable
object Home

@Serializable
object FormCategoria

@Serializable
object FormEvento

@Serializable
data class DetalleEvento(val titulo: String, val descripcion: String, val categoria: String)

@SuppressLint("ViewModelConstructorInComposable")

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val viewModel = EventViewModel()

    NavHost(navController = navController, startDestination = Home) {
        composable<Home> {
        }
        composable<FormCategoria> {
        }
        composable<FormEvento> {
        }
        composable<DetalleEvento> { backStackEntry ->
            val args = backStackEntry.toRoute<DetalleEvento>()
        }
    }
}