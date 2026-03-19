package com.example.rescatando_mascotas_forever.presentation.admin

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
fun AdminFormularioAdopcionScreen(navController: NavHostController) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var currentPage by remember { mutableStateOf(1) }
    val totalPages = 5

    // --- ESTADOS DEL FORMULARIO ---
    var nombre by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var ocupacion by remember { mutableStateOf("") }
    var tipoVivienda by remember { mutableStateOf("Casa") }
    var tienePatio by remember { mutableStateOf(false) }
    var tieneProtecciones by remember { mutableStateOf(false) }
    var integrantes by remember { mutableStateOf("") }
    var hayNinos by remember { mutableStateOf(false) }
    var estanDeAcuerdo by remember { mutableStateOf(true) }
    var tieneOtrasMascotas by remember { mutableStateOf(false) }
    var experienciaPrevia by remember { mutableStateOf("") }
    var tiempoDiario by remember { mutableStateOf("") }
    var presupuestoVeterinario by remember { mutableStateOf(true) }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            ModalDrawerSheet(
                drawerContainerColor = Color.White,
                modifier = Modifier.width(300.dp)
            ) {
                AdminDrawerContent(navController, drawerState, scope)
            }
        }
    ) {
        Scaffold(
            topBar = { MainTopBar(drawerState = drawerState, scope = scope) }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .background(Color(0xFFF0F2F5))
            ) {
                // Admin Header with Gradient
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(
                            Brush.horizontalGradient(
                                colors = listOf(Color(0xFF673AB7), Color(0xFF9C27B0))
                            )
                        )
                        .padding(20.dp)
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.AdminPanelSettings, null, tint = Color.White, modifier = Modifier.size(20.dp))
                            Spacer(Modifier.width(8.dp))
                            Text("PANEL ADMINISTRADOR", color = Color.White.copy(alpha = 0.8f), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text("Registro de Adopción", color = Color.White, fontSize = 28.sp, fontWeight = FontWeight.ExtraBold)
                        
                        Spacer(Modifier.height(12.dp))
                        
                        // Stepper
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            (1..totalPages).forEach { step ->
                                Box(
                                    modifier = Modifier
                                        .size(32.dp)
                                        .clip(CircleShape)
                                        .background(if (step <= currentPage) Color.White else Color.White.copy(alpha = 0.3f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = step.toString(),
                                        color = if (step <= currentPage) Color(0xFF673AB7) else Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                }
                                if (step < totalPages) {
                                    Box(
                                        modifier = Modifier
                                            .height(2.dp)
                                            .weight(1f)
                                            .background(if (step < currentPage) Color.White else Color.White.copy(alpha = 0.3f))
                                    )
                                }
                            }
                        }
                    }
                }

                // Form Content
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(horizontal = 16.dp)
                        .offset(y = (-20).dp),
                    shape = RoundedCornerShape(32.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
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
                                1 -> StepAdmin1(nombre, { nombre = it }, dni, { dni = it }, edad, { edad = it })
                                2 -> StepAdmin2(ocupacion, { ocupacion = it }, telefono, { telefono = it })
                                3 -> StepAdmin3(tipoVivienda, { tipoVivienda = it }, tienePatio, { tienePatio = it }, tieneProtecciones, { tieneProtecciones = it })
                                4 -> StepAdmin4(integrantes, { integrantes = it }, hayNinos, { hayNinos = it }, estanDeAcuerdo, { estanDeAcuerdo = it })
                                5 -> StepAdmin5(tieneOtrasMascotas, { tieneOtrasMascotas = it }, experienciaPrevia, { experienciaPrevia = it }, tiempoDiario, { tiempoDiario = it }, presupuestoVeterinario, { presupuestoVeterinario = it })
                            }
                        }
                    }
                }

                // Footer Navigation
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .offset(y = (-10).dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (currentPage > 1) {
                        OutlinedButton(
                            onClick = { currentPage-- },
                            modifier = Modifier.weight(1f).height(56.dp),
                            shape = RoundedCornerShape(16.dp),
                            border = BorderStroke(1.dp, Color(0xFF673AB7))
                        ) {
                            Text("ANTERIOR", color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                        }
                    }

                    Button(
                        onClick = {
                            if (currentPage < totalPages) currentPage++
                            else navController.popBackStack()
                        },
                        modifier = Modifier.weight(2f).height(56.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF673AB7))
                    ) {
                        Text(
                            if (currentPage == totalPages) "GUARDAR SOLICITUD" else "SIGUIENTE PASO",
                            fontWeight = FontWeight.Bold
                        )
                        if (currentPage < totalPages) {
                            Spacer(Modifier.width(8.dp))
                            Icon(Icons.Default.ArrowForward, null, modifier = Modifier.size(18.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StepAdmin1(nombre: String, onNombre: (String) -> Unit, dni: String, onDni: (String) -> Unit, edad: String, onEdad: (String) -> Unit) {
    Column {
        AdminSectionTitle(Icons.Default.Person, "Identidad del Solicitante")
        AdminTextField(nombre, onNombre, "Nombre Completo", Icons.Default.Badge)
        AdminTextField(dni, onDni, "DNI / Cédula de Identidad", Icons.Default.AssignmentInd)
        AdminTextField(edad, onEdad, "Edad del solicitante", Icons.Default.Cake)
    }
}

@Composable
fun StepAdmin2(ocupacion: String, onOcupacion: (String) -> Unit, telefono: String, onTelefono: (String) -> Unit) {
    Column {
        AdminSectionTitle(Icons.Default.ContactPhone, "Contacto y Profesión")
        AdminTextField(ocupacion, onOcupacion, "Ocupación actual", Icons.Default.Work)
        AdminTextField(telefono, onTelefono, "Teléfono de contacto", Icons.Default.Phone)
    }
}

@Composable
fun StepAdmin3(tipo: String, onTipo: (String) -> Unit, patio: Boolean, onPatio: (Boolean) -> Unit, prot: Boolean, onProt: (Boolean) -> Unit) {
    Column {
        AdminSectionTitle(Icons.Default.HomeWork, "Entorno de la Vivienda")
        Text("Tipo de vivienda", fontSize = 14.sp, color = Color.Gray, modifier = Modifier.padding(bottom = 8.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            listOf("Casa", "Apto", "Finca").forEach { t ->
                FilterChip(
                    selected = tipo == t,
                    onClick = { onTipo(t) },
                    label = { Text(t) },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        Spacer(Modifier.height(16.dp))
        AdminSwitchField("¿Tiene patio o balcón seguro?", patio, onPatio)
        AdminSwitchField("¿Tiene mallas o protecciones?", prot, onProt)
    }
}

@Composable
fun StepAdmin4(integrantes: String, onIntegrantes: (String) -> Unit, ninos: Boolean, onNinos: (Boolean) -> Unit, acuerdo: Boolean, onAcuerdo: (Boolean) -> Unit) {
    Column {
        AdminSectionTitle(Icons.Default.Groups, "Núcleo Familiar")
        AdminTextField(integrantes, onIntegrantes, "Número de habitantes", Icons.Default.FormatListNumbered)
        AdminSwitchField("¿Viven niños en el hogar?", ninos, onNinos)
        AdminSwitchField("¿Están todos de acuerdo?", acuerdo, onAcuerdo)
    }
}

@Composable
fun StepAdmin5(otras: Boolean, onOtras: (Boolean) -> Unit, exp: String, onExp: (String) -> Unit, tiempo: String, onTiempo: (String) -> Unit, presupuesto: Boolean, onPresupuesto: (Boolean) -> Unit) {
    Column {
        AdminSectionTitle(Icons.Default.Favorite, "Compromiso y Experiencia")
        AdminSwitchField("¿Tiene otras mascotas?", otras, onOtras)
        AdminTextField(tiempo, onTiempo, "Tiempo diario dedicado", Icons.Default.Timer)
        OutlinedTextField(
            value = exp,
            onValueChange = onExp,
            label = { Text("Experiencia previa") },
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            shape = RoundedCornerShape(12.dp),
            minLines = 3
        )
        AdminSwitchField("¿Cuenta con presupuesto veterinario?", presupuesto, onPresupuesto)
    }
}

@Composable
fun AdminSectionTitle(icon: ImageVector, title: String) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(bottom = 20.dp)) {
        Icon(icon, null, tint = Color(0xFF673AB7), modifier = Modifier.size(24.dp))
        Spacer(Modifier.width(12.dp))
        Text(title, fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = Color(0xFF333333))
    }
}

@Composable
fun AdminTextField(value: String, onValue: (String) -> Unit, label: String, icon: ImageVector) {
    OutlinedTextField(
        value = value,
        onValueChange = onValue,
        label = { Text(label) },
        leadingIcon = { Icon(icon, null, tint = Color(0xFF673AB7).copy(alpha = 0.7f)) },
        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = Color(0xFF673AB7),
            unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f)
        )
    )
}

@Composable
fun AdminSwitchField(label: String, checked: Boolean, onChecked: (Boolean) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFFF8F9FA))
            .padding(horizontal = 16.dp, vertical = 8.dp),
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
