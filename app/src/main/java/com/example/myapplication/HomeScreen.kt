package com.example.myapplication

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForwardIos
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FolderOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.myapplication.ui.theme.AzulClaroFondo
import com.example.myapplication.ui.theme.AzulOscuroBotones
import com.example.myapplication.ui.theme.AzulIconoFondo
import com.example.myapplication.ui.theme.FondoApp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: EventViewModel, navController: NavHostController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Mis Eventos",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3243)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoApp)
            )
        },
        containerColor = FondoApp
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
            contentPadding = PaddingValues(20.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = AzulClaroFondo)
                ) {
                    Column(modifier = Modifier.padding(24.dp)) {
                        Text(
                            "Acciones rápidas", 
                            style = MaterialTheme.typography.bodyLarge,
                            color = Color(0xFF4A5982).copy(alpha = 0.8f)
                        )
                        Spacer(Modifier.height(16.dp))
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            AccionBotonEstilo(
                                texto = "Nueva categoría",
                                icono = Icons.Default.FolderOpen,
                                modifier = Modifier.weight(1f),
                                onClick = { navController.navigate(FormCategoria) }
                            )
                            AccionBotonEstilo(
                                texto = "Nuevo evento",
                                icono = Icons.Default.CalendarMonth,
                                modifier = Modifier.weight(1f),
                                enabled = viewModel.categorias.isNotEmpty(),
                                onClick = { navController.navigate(FormEvento) }
                            )
                        }

                        if (viewModel.categorias.isEmpty()) {
                            Spacer(Modifier.height(12.dp))
                            Text(
                                "Crea una categoría para habilitar nuevos eventos",
                                style = MaterialTheme.typography.bodySmall,
                                color = Color(0xFFF44336),
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Categorías", 
                        style = MaterialTheme.typography.titleLarge, 
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2D3243)
                    )
                    Surface(
                        shape = RoundedCornerShape(50),
                        color = Color(0xFFE0E6F9)
                    ) {
                        Text(
                            "${viewModel.categorias.size}",
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = AzulOscuroBotones
                        )
                    }
                }
            }

            if (viewModel.categorias.isEmpty()) {
                item { EstadoVacioEstilo() }
            } else {
                items(viewModel.categorias) { categoria ->
                    CategoriaCardEstilo(
                        nombre = categoria.nombre,
                        onClick = { navController.navigate(EventosCategoria(categoria.nombre)) }
                    )
                }
            }
        }
    }
}

@Composable
fun AccionBotonEstilo(
    texto: String, 
    icono: ImageVector, 
    modifier: Modifier = Modifier, 
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = AzulOscuroBotones,
            disabledContainerColor = AzulOscuroBotones.copy(alpha = 0.5f)
        )
    ) {
        Icon(icono, contentDescription = null, modifier = Modifier.size(20.dp), tint = Color.White)
        Spacer(Modifier.width(8.dp))
        Text(texto, style = MaterialTheme.typography.labelLarge, color = Color.White)
    }
}

@Composable
fun CategoriaCardEstilo(nombre: String, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable { onClick() },
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(20.dp).fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AzulIconoFondo,
                    modifier = Modifier.size(44.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(Icons.Default.FolderOpen, contentDescription = null, tint = AzulOscuroBotones)
                    }
                }
                Spacer(Modifier.width(16.dp))
                Text(nombre, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            }
            Icon(Icons.AutoMirrored.Filled.ArrowForwardIos, contentDescription = null, modifier = Modifier.size(16.dp), tint = AzulOscuroBotones)
        }
    }
}

@Composable
fun EstadoVacioEstilo() {
    Card(
        modifier = Modifier.fillMaxWidth().height(280.dp),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = AzulClaroFondo.copy(alpha = 0.4f))
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp),
                color = AzulIconoFondo,
                modifier = Modifier.size(80.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(Icons.Default.FolderOpen, contentDescription = null, modifier = Modifier.size(40.dp), tint = AzulOscuroBotones)
                }
            }
            Spacer(Modifier.height(24.dp))
            Text("Sin categorías aún", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(8.dp))
            Text(
                "Crea tu primera categoría para organizar tus eventos",
                textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                modifier = Modifier.padding(horizontal = 40.dp),
                color = Color.Gray,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventosPorCategoriaScreen(categoria: String, viewModel: EventViewModel, navController: NavHostController) {
    val eventosFiltrados = viewModel.getEventosPorCategoria(categoria)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(categoria, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoApp)
            )
        },
        containerColor = FondoApp
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding).fillMaxSize(), contentPadding = PaddingValues(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(eventosFiltrados) { evento ->
                EventCard(evento = evento, onClick = {
                    navController.navigate(DetalleEvento(evento.titulo, evento.descripcion, evento.categoria))
                })
            }
        }
    }
}
