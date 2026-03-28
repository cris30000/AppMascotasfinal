package com.example.rescatando_mascotas_forever.presentation.adopciones

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormularioAdopcionScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(1) }
    val totalPages = 3

    // State for form fields
    var nombreCompleto by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var tieneOtrasMascotas by remember { mutableStateOf(false) }
    var tipoVivienda by remember { mutableStateOf("") }

    AppDrawer(navController = navController, drawerState = drawerState, scope = scope) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) },
            bottomBar = { AppBottomBar(navController = navController) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF8F9FA))
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { if (currentPage > 1) currentPage-- else navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color(0xFF673AB7))
                    }
                    
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            "Paso $currentPage de $totalPages",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF673AB7)
                        )
                        LinearProgressIndicator(
                            progress = currentPage.toFloat() / totalPages.toFloat(),
                            modifier = Modifier
                                .width(100.dp)
                                .height(6.dp)
                                .clip(CircleShape),
                            color = Color(0xFF673AB7),
                            trackColor = Color(0xFF673AB7).copy(alpha = 0.2f)
                        )
                    }
                    
                    Box(modifier = Modifier.size(48.dp)) // Spacer for alignment
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Content with Animation
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(24.dp)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AnimatedContent(
                            targetState = currentPage,
                            transitionSpec = {
                                if (targetState > initialState) {
                                    slideInHorizontally { it } + fadeIn() togetherWith
                                            slideOutHorizontally { -it } + fadeOut()
                                } else {
                                    slideInHorizontally { -it } + fadeIn() togetherWith
                                            slideOutHorizontally { it } + fadeOut()
                                }
                            }, label = ""
                        ) { page ->
                            when (page) {
                                1 -> Step1(nombreCompleto, { nombreCompleto = it }, edad, { edad = it })
                                2 -> Step2(direccion, { direccion = it }, telefono, { telefono = it })
                                3 -> Step3(motivo, { motivo = it }, tieneOtrasMascotas, { tieneOtrasMascotas = it })
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Navigation Buttons
                Button(
                    onClick = {
                        if (currentPage < totalPages) {
                            currentPage++
                        } else {
                            // Logic to submit form
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp),
                    shape = RoundedCornerShape(20.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 8.dp)
                ) {
                    Text(
                        if (currentPage == totalPages) "FINALIZAR SOLICITUD" else "SIGUIENTE",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 16.sp,
                        letterSpacing = 1.sp
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun Step1(nombre: String, onNombreChange: (String) -> Unit, edad: String, onEdadChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {
        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF673AB7), modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Información Personal", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Cuéntanos un poco sobre ti para conocerte mejor.", color = Color.Gray, fontSize = 14.sp)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        ModernTextField(value = nombre, onValueChange = onNombreChange, label = "Nombre completo", icon = Icons.Default.Badge)
        ModernTextField(value = edad, onValueChange = onEdadChange, label = "Tu edad", icon = Icons.Default.Event)
    }
}

@Composable
fun Step2(direccion: String, onDirChange: (String) -> Unit, telefono: String, onTelChange: (String) -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {
        Icon(Icons.Default.ContactSupport, contentDescription = null, tint = Color(0xFF673AB7), modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Contacto y Ubicación", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("¿Dónde vivirá tu nuevo amigo y cómo te contactamos?", color = Color.Gray, fontSize = 14.sp)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        ModernTextField(value = direccion, onValueChange = onDirChange, label = "Dirección", icon = Icons.Default.LocationOn)
        ModernTextField(value = telefono, onValueChange = onTelChange, label = "Teléfono de contacto", icon = Icons.Default.Phone)
    }
}

@Composable
fun Step3(motivo: String, onMotivoChange: (String) -> Unit, tieneMascotas: Boolean, onTieneChange: (Boolean) -> Unit) {
    Column(horizontalAlignment = Alignment.Start) {
        Icon(Icons.Default.Favorite, contentDescription = null, tint = Color(0xFF673AB7), modifier = Modifier.size(48.dp))
        Spacer(modifier = Modifier.height(16.dp))
        Text("Sobre la adopción", fontSize = 22.sp, fontWeight = FontWeight.ExtraBold)
        Text("Tu experiencia y motivaciones son muy importantes.", color = Color.Gray, fontSize = 14.sp)
        
        Spacer(modifier = Modifier.height(32.dp))
        
        OutlinedTextField(
            value = motivo,
            onValueChange = onMotivoChange,
            label = { Text("¿Por qué quieres adoptar?") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            minLines = 4,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF673AB7),
                focusedLabelColor = Color(0xFF673AB7)
            )
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF0EDFF)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(16.dp)
            ) {
                Checkbox(
                    checked = tieneMascotas, 
                    onCheckedChange = onTieneChange,
                    colors = CheckboxDefaults.colors(checkedColor = Color(0xFF673AB7))
                )
                Text("¿Tienes otras mascotas actualmente?", fontSize = 14.sp, fontWeight = FontWeight.Medium)
            }
        }
    }
}

@Composable
fun ModernTextField(value: String, onValueChange: (String) -> Unit, label: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = { Icon(icon, contentDescription = null, tint = Color(0xFF673AB7)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF673AB7),
            focusedLabelColor = Color(0xFF673AB7)
        )
    )
}
