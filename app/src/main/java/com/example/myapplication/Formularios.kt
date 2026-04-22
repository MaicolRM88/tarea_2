package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

// ─── Componentes reutilizables ───────────────────────────

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun FormScaffold(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    iconoColor: Color,
    iconoColorContenido: Color,
    navController: NavHostController,
    content: @Composable ColumnScope.() -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(titulo, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
                        Text(subtitulo, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver", tint = MaterialTheme.colorScheme.primary)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.surface)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(24.dp)
        ) {
            Spacer(Modifier.height(12.dp))
            Surface(shape = RoundedCornerShape(16.dp), color = iconoColor, modifier = Modifier.size(56.dp)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icono, contentDescription = null, tint = iconoColorContenido, modifier = Modifier.size(28.dp))
                }
            }
            Spacer(Modifier.height(24.dp))
            content()
        }
    }
}

@Composable
private fun CampoTexto(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Text(label, style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
    Spacer(Modifier.height(8.dp))
    OutlinedTextField(
        value = value,
        onValueChange = onChange,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        singleLine = singleLine,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = MaterialTheme.colorScheme.primary,
            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
            focusedLabelColor = MaterialTheme.colorScheme.primary
        )
    )
}

@Composable
private fun ColumnScope.BotonesFormulario(onGuardar: () -> Unit, onCancelar: () -> Unit, textoGuardar: String) {
    Spacer(Modifier.weight(1f))
    Button(onClick = onGuardar, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
        Text(textoGuardar, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }
    Spacer(Modifier.height(8.dp))
    OutlinedButton(onClick = onCancelar, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(14.dp)) {
        Text("Cancelar", style = MaterialTheme.typography.titleSmall)
    }
}

// ─── Pantallas ────────────────────────────────────────────

@Composable
fun FormCategoriaScreen(viewModel: EventViewModel, navController: NavHostController) {
    FormScaffold(
        titulo = "Nueva categoría",
        subtitulo = "Organiza tus eventos",
        icono = Icons.Default.List,
        iconoColor = MaterialTheme.colorScheme.primaryContainer,
        iconoColorContenido = MaterialTheme.colorScheme.onPrimaryContainer,
        navController = navController
    ) {
        CampoTexto(
            label = stringResource(R.string.nombre_categoria),
            value = viewModel.nombreCategoria,
            onChange = { viewModel.nombreCategoria = it }
        )
        if (viewModel.errorCategoria.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(viewModel.errorCategoria, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
        BotonesFormulario(
            onGuardar = { if (viewModel.addCategoria()) navController.popBackStack() },
            onCancelar = { navController.popBackStack() },
            textoGuardar = stringResource(R.string.btn_guardar)
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEventoScreen(viewModel: EventViewModel, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember {
        mutableStateOf(if (viewModel.categorias.isNotEmpty()) viewModel.categorias[0].nombre else "General")
    }

    FormScaffold(
        titulo = "Nuevo evento",
        subtitulo = "Completa los detalles",
        icono = Icons.Default.CalendarMonth,
        iconoColor = MaterialTheme.colorScheme.tertiaryContainer,
        iconoColorContenido = MaterialTheme.colorScheme.onTertiaryContainer,
        navController = navController
    ) {
        CampoTexto(label = stringResource(R.string.titulo_evento), value = viewModel.tituloEvento, onChange = { viewModel.tituloEvento = it })
        Spacer(Modifier.height(16.dp))
        CampoTexto(label = stringResource(R.string.desc_evento), value = viewModel.descEvento, onChange = { viewModel.descEvento = it }, modifier = Modifier.height(110.dp), singleLine = false)
        Spacer(Modifier.height(16.dp))

        Text("Categoría", style = MaterialTheme.typography.labelLarge, fontWeight = FontWeight.SemiBold)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = categoriaSeleccionada,
                onValueChange = {},
                readOnly = true,
                label = { Text("Seleccionar categoría") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                val opciones = if (viewModel.categorias.isEmpty()) listOf("General") else viewModel.categorias.map { it.nombre }
                opciones.forEach { nombre ->
                    DropdownMenuItem(text = { Text(nombre) }, onClick = { categoriaSeleccionada = nombre; expanded = false })
                }
            }
        }

        if (viewModel.errorEvento.isNotEmpty()) {
            Spacer(Modifier.height(4.dp))
            Text(viewModel.errorEvento, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
        BotonesFormulario(
            onGuardar = { if (viewModel.addEvento(categoriaSeleccionada)) navController.popBackStack() },
            onCancelar = { navController.popBackStack() },
            textoGuardar = stringResource(R.string.btn_guardar)
        )
    }
}