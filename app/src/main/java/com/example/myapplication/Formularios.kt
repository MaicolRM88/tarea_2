package com.example.myapplication

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.CalendarMonth
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
import com.example.myapplication.ui.theme.FondoApp

@Composable
fun debouncedClick(onClick: () -> Unit): () -> Unit {
    var lastClickTime by remember { mutableLongStateOf(0L) }
    return {
        val currentTime = System.currentTimeMillis()
        if (currentTime - lastClickTime > 1000L) {
            lastClickTime = currentTime
            onClick()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormScaffold(
    titulo: String,
    subtitulo: String,
    icono: ImageVector,
    iconoColor: Color,
    iconoColorContenido: Color,
    navController: NavHostController,
    content: @Composable ColumnScope.() -> Unit
) {
    val onBackSafe = debouncedClick { navController.popBackStack() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(titulo, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color(0xFF2D3243))
                        Text(subtitulo, style = MaterialTheme.typography.labelSmall, color = Color.Gray)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackSafe) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = AzulOscuroBotones)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = FondoApp)
            )
        },
        containerColor = FondoApp
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding).fillMaxSize().padding(24.dp)
        ) {
            Surface(
                shape = RoundedCornerShape(20.dp), 
                color = iconoColor, 
                modifier = Modifier.size(64.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icono, contentDescription = null, tint = iconoColorContenido, modifier = Modifier.size(32.dp))
                }
            }
            Spacer(Modifier.height(32.dp))
            content()
        }
    }
}

@Composable
fun CampoTexto(
    label: String,
    value: String,
    onChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true
) {
    Column {
        Text(label, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = AzulOscuroBotones)
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = { onChange(it) },
            modifier = modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            singleLine = singleLine,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = AzulOscuroBotones,
                unfocusedBorderColor = Color(0xFFE0E6F9),
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedLabelColor = AzulOscuroBotones
            )
        )
    }
}

@Composable
fun ColumnScope.BotonesFormulario(onGuardar: () -> Unit, onCancelar: () -> Unit, textoGuardar: String) {
    val onGuardarSafe = debouncedClick(onGuardar)
    val onCancelarSafe = debouncedClick(onCancelar)

    Spacer(Modifier.weight(1f))
    Button(
        onClick = onGuardarSafe, 
        modifier = Modifier.fillMaxWidth().height(56.dp), 
        shape = RoundedCornerShape(18.dp),
        colors = ButtonDefaults.buttonColors(containerColor = AzulOscuroBotones)
    ) {
        Text(textoGuardar, style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold)
    }
    Spacer(Modifier.height(12.dp))
    TextButton(
        onClick = onCancelarSafe, 
        modifier = Modifier.fillMaxWidth().height(56.dp)
    ) {
        Text("Cancelar", color = Color.Gray, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun FormCategoriaScreen(viewModel: EventViewModel, navController: NavHostController) {
    FormScaffold(
        titulo = "Nueva categoría",
        subtitulo = "Organiza tus eventos",
        icono = Icons.AutoMirrored.Filled.List,
        iconoColor = AzulClaroFondo,
        iconoColorContenido = AzulOscuroBotones,
        navController = navController
    ) {
        CampoTexto(
            label = "Nombre de la categoría",
            value = viewModel.nombreCategoria,
            onChange = { viewModel.nombreCategoria = it }
        )
        if (viewModel.errorCategoria.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(viewModel.errorCategoria, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
        BotonesFormulario(
            onGuardar = { if (viewModel.addCategoria()) navController.popBackStack() },
            onCancelar = { navController.popBackStack() },
            textoGuardar = "Guardar Categoría"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormEventoScreen(viewModel: EventViewModel, navController: NavHostController) {
    var expanded by remember { mutableStateOf(false) }
    var categoriaSeleccionada by remember {
        mutableStateOf(if (viewModel.categorias.isNotEmpty()) viewModel.categorias[0].nombre else "")
    }

    FormScaffold(
        titulo = "Nuevo evento",
        subtitulo = "Completa los detalles",
        icono = Icons.Default.CalendarMonth,
        iconoColor = AzulClaroFondo,
        iconoColorContenido = AzulOscuroBotones,
        navController = navController
    ) {
        CampoTexto(label = "Título del evento", value = viewModel.tituloEvento, onChange = { viewModel.tituloEvento = it })
        Spacer(Modifier.height(20.dp))
        CampoTexto(label = "Descripción", value = viewModel.descEvento, onChange = { viewModel.descEvento = it }, modifier = Modifier.height(120.dp), singleLine = false)
        Spacer(Modifier.height(20.dp))

        Text("Selecciona Categoría", style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = AzulOscuroBotones)
        Spacer(Modifier.height(8.dp))
        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }, modifier = Modifier.fillMaxWidth()) {
            OutlinedTextField(
                value = categoriaSeleccionada,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AzulOscuroBotones,
                    unfocusedBorderColor = Color(0xFFE0E6F9),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                )
            )
            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                viewModel.categorias.forEach { cat ->
                    DropdownMenuItem(text = { Text(cat.nombre) }, onClick = { categoriaSeleccionada = cat.nombre; expanded = false })
                }
            }
        }

        if (viewModel.errorEvento.isNotEmpty()) {
            Spacer(Modifier.height(8.dp))
            Text(viewModel.errorEvento, color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelSmall)
        }
        BotonesFormulario(
            onGuardar = { if (viewModel.addEvento(categoriaSeleccionada)) navController.popBackStack() },
            onCancelar = { navController.popBackStack() },
            textoGuardar = "Crear Evento"
        )
    }
}
