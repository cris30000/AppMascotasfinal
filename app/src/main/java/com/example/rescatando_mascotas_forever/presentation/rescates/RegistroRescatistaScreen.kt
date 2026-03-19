package com.example.rescatando_mascotas_forever.presentation.rescates

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rescatando_mascotas_forever.presentation.common.components.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegistroRescatistaScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(1) }
    val totalPages = 4

    // --- ESTADOS DEL FORMULARIO ---
    var nombre by remember { mutableStateOf("") }
    var documento by remember { mutableStateOf("") }
    var fechaNacimiento by remember { mutableStateOf("") }
    var whatsapp by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var capacidadAnimales by remember { mutableStateOf("") }
    var motivacion by remember { mutableStateOf("") }
    var tieneEspacio by remember { mutableStateOf(false) }
    var tieneTransporte by remember { mutableStateOf(false) }
    var declaraVerdad by remember { mutableStateOf(false) }
    var aceptaTerminos by remember { mutableStateOf(false) }

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
            ) {
                // Header Elegante
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(Color(0xFF673AB7), Color(0xFF512DA8))
                            )
                        )
                        .padding(24.dp)
                ) {
                    Column {
                        Text(
                            "Registro Rescatista",
                            color = Color.White,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Text(
                            "Únete a nuestra red de héroes",
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 14.sp
                        )
                        
                        Spacer(modifier = Modifier.height(20.dp))
                        
                        // Progress Indicator
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            repeat(totalPages) { index ->
                                val step = index + 1
                                Box(
                                    modifier = Modifier
                                        .height(4.dp)
                                        .weight(1f)
                                        .clip(CircleShape)
                                        .background(
                                            if (step <= currentPage) Color.White 
                                            else Color.White.copy(alpha = 0.3f)
                                        )
                                )
                            }
                        }
                    }
                }

                // Form Card
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 20.dp)
                        .offset(y = (-30).dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
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
                                fadeIn() + slideInHorizontally { if (targetState > initialState) it else -it } togetherWith
                                fadeOut() + slideOutHorizontally { if (targetState > initialState) -it else it }
                            }, label = ""
                        ) { page ->
                            when (page) {
                                1 -> RescatistaStep1(nombre, { nombre = it }, documento, { documento = it })
                                2 -> RescatistaStep2(fechaNacimiento, { fechaNacimiento = it }, whatsapp, { whatsapp = it }, email, { email = it })
                                3 -> RescatistaStep3(capacidadAnimales, { capacidadAnimales = it }, tieneEspacio, { tieneEspacio = it }, tieneTransporte, { tieneTransporte = it })
                                4 -> RescatistaStep4(motivacion, { motivacion = it }, declaraVerdad, { declaraVerdad = it }, aceptaTerminos, { aceptaTerminos = it })
                            }
                        }
                    }
                }

                // Navigation Buttons
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .offset(y = (-15).dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentPage > 1) {
                        OutlinedButton(
                            onClick = { currentPage-- },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xFF673AB7))
                        ) {
                            Text("ATRÁS", color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            if (currentPage < totalPages) currentPage++
                            else navController.popBackStack()
                        },
                        modifier = Modifier.weight(2f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7)),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
                    ) {
                        Text(
                            if (currentPage == totalPages) "FINALIZAR REGISTRO" else "CONTINUAR",
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = 1.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
            }
        }
    }
}

@Composable
fun RescatistaStep1(nombre: String, onNombre: (String) -> Unit, dni: String, onDni: (String) -> Unit) {
    Column {
        RescatistaHeader(Icons.Default.Badge, "Identificación", "Comencemos con tus datos básicos.")
        RescatistaTextField(nombre, onNombre, "Nombre Completo", Icons.Default.Person)
        RescatistaTextField(dni, onDni, "Número de Documento", Icons.Default.Fingerprint)
    }
}

@Composable
fun RescatistaStep2(fecha: String, onFecha: (String) -> Unit, wa: String, onWa: (String) -> Unit, email: String, onEmail: (String) -> Unit) {
    Column {
        RescatistaHeader(Icons.Default.ContactMail, "Contacto", "¿Cómo podemos comunicarnos contigo?")
        RescatistaTextField(fecha, onFecha, "Fecha de Nacimiento", Icons.Default.CalendarToday)
        RescatistaTextField(wa, onWa, "WhatsApp / Teléfono", Icons.Default.Phone)
        RescatistaTextField(email, onEmail, "Correo Electrónico", Icons.Default.Email)
    }
}

@Composable
fun RescatistaStep3(capacidad: String, onCap: (String) -> Unit, espacio: Boolean, onEsp: (Boolean) -> Unit, trans: Boolean, onTrans: (Boolean) -> Unit) {
    Column {
        RescatistaHeader(Icons.Default.Pets, "Capacidad", "Cuéntanos sobre tus recursos para ayudar.")
        RescatistaTextField(capacidad, onCap, "Capacidad de animales (ej. 3 perros)", Icons.Default.FormatListNumbered)
        
        Spacer(modifier = Modifier.height(16.dp))
        
        RescatistaSwitch("¿Cuentas con espacio físico?", espacio, onEsp)
        RescatistaSwitch("¿Tienes transporte propio?", trans, onTrans)
    }
}

@Composable
fun RescatistaStep4(motivacion: String, onMot: (String) -> Unit, verdad: Boolean, onVerdad: (Boolean) -> Unit, term: Boolean, onTerm: (Boolean) -> Unit) {
    Column {
        RescatistaHeader(Icons.Default.VolunteerActivism, "Compromiso", "Tu motivación es el motor del cambio.")
        
        OutlinedTextField(
            value = motivacion,
            onValueChange = onMot,
            label = { Text("¿Por qué quieres ser rescatista?") },
            modifier = Modifier.fillMaxWidth().height(150.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = Color(0xFF673AB7))
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = verdad, onCheckedChange = onVerdad, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF673AB7)))
            Text("Declaro que la información es verídica", fontSize = 12.sp, color = Color.Gray)
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = term, onCheckedChange = onTerm, colors = CheckboxDefaults.colors(checkedColor = Color(0xFF673AB7)))
            Text("Acepto términos y condiciones de la app", fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun RescatistaHeader(icon: ImageVector, title: String, subtitle: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 24.dp)) {
        Surface(
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFF673AB7).copy(alpha = 0.1f)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, null, tint = Color(0xFF673AB7))
            }
        }
        Spacer(Modifier.width(16.dp))
        Column {
            Text(title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF333333))
            Text(subtitle, fontSize = 12.sp, color = Color.Gray)
        }
    }
}

@Composable
fun RescatistaTextField(value: String, onValue: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValue,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFF673AB7).copy(alpha = 0.6f)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 16.dp),
        shape = RoundedCornerShape(16.dp),
        singleLine = true,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF673AB7),
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun RescatistaSwitch(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp),
        shape = RoundedCornerShape(12.dp),
        color = Color(0xFFF8F9FA),
        border = BorderStroke(1.dp, Color.LightGray.copy(alpha = 0.3f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(label, fontSize = 14.sp, fontWeight = FontWeight.Medium)
            Switch(
                checked = checked, 
                onCheckedChange = onChecked,
                colors = SwitchDefaults.colors(checkedThumbColor = Color.White, checkedTrackColor = Color(0xFF673AB7))
            )
        }
    }
}
