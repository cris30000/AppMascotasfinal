package com.example.rescatando_mascotas_forever.presentation.home

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.NotificationsNone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.rescatando_mascotas_forever.R
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import com.example.rescatando_mascotas_forever.presentation.common.components.AppDrawer
import com.example.rescatando_mascotas_forever.presentation.common.components.MainTopBar
import com.example.rescatando_mascotas_forever.presentation.common.components.AppBottomBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavHostController,
    viewModel: HomeViewModel = viewModel()
) {
    val mascotas by viewModel.mascotas.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    AppDrawer(
        navController = navController,
        drawerState = drawerState,
        scope = scope
    ) {
        Scaffold(
            topBar = {
                MainTopBar(drawerState = drawerState, scope = scope)
            },
            bottomBar = {
                AppBottomBar(navController)
            },
            containerColor = Color(0xFFFCF9F6) // Warm off-white
        ) { padding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                // Header con degradado suave y bienvenida cálida
                item { 
                    HomeHeader() 
                }

                // Banner Promocional con estilo "Tierno"
                item {
                    TenderPromoBanner()
                }

                // Selector de Categorías Interactivo
                item {
                    SectionHeader("Busca un nuevo amigo", "Ver todas")
                    CategoryList(selectedCategory) { category ->
                        viewModel.filterByCategory(category)
                    }
                }

                // Lista de Mascotas con Animación de Entrada
                item {
                    val sectionTitle = when(selectedCategory) {
                        "Todos" -> "Mascotas esperando por ti"
                        "Perros" -> "Perritos buscando hogar"
                        "Gatos" -> "Gatitos esperando amor"
                        else -> "Resultados de $selectedCategory"
                    }
                    
                    SectionHeader(title = sectionTitle, action = "Ver más")
                    
                    if (isLoading) {
                        Box(modifier = Modifier.fillMaxWidth().height(200.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = Color(0xFF673AB7), strokeWidth = 3.dp)
                        }
                    } else {
                        AnimatedVisibility(
                            visible = !isLoading,
                            enter = fadeIn() + expandVertically()
                        ) {
                            if (mascotas.isEmpty()) {
                                EmptyStateMessage()
                            } else {
                                LazyRow(
                                    contentPadding = PaddingValues(horizontal = 20.dp),
                                    horizontalArrangement = Arrangement.spacedBy(18.dp),
                                    modifier = Modifier.padding(bottom = 12.dp)
                                ) {
                                    items(mascotas) { mascota ->
                                        PremiumPetCard(mascota)
                                    }
                                }
                            }
                        }
                    }
                }

                // Acciones Rápidas (Proceso y Emergencia)
                item {
                    Row(
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        ActionCard(
                            title = "Mi Proceso",
                            subtitle = "Ver estado",
                            icon = Icons.Default.Assignment,
                            color = Color(0xFFE3F2FD),
                            iconColor = Color(0xFF1976D2),
                            modifier = Modifier.weight(1f)
                        ) {
                            navController.navigate("proceso_adopcion")
                        }
                        ActionCard(
                            title = "Reportar",
                            subtitle = "Emergencia",
                            icon = Icons.Default.Warning,
                            color = Color(0xFFFFEBEE),
                            iconColor = Color(0xFFD32F2F),
                            modifier = Modifier.weight(1f)
                        ) {
                            navController.navigate("formulario_rescate")
                        }
                    }
                }

                // Eventos Sutiles
                item {
                    SectionHeader("Eventos de la comunidad", "Calendario")
                    TenderEventCard()
                }

                item { Spacer(modifier = Modifier.height(40.dp)) }
            }
        }
    }
}

@Composable
fun HomeHeader() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 10.dp)
    ) {
        // Decoración de fondo suave
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(80.dp)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color(0xFFEDE7F6), Color.Transparent)
                    )
                )
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    "¡Bienvenido, Yeison! 👋",
                    fontSize = 26.sp, 
                    fontWeight = FontWeight.ExtraBold, 
                    color = Color(0xFF311B92),
                    letterSpacing = (-0.5).sp
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocationOn, null, tint = Color(0xFF673AB7), modifier = Modifier.size(14.dp))
                    Text(
                        " Popayán, Cauca", 
                        fontSize = 14.sp, 
                        color = Color.Gray,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            
            // Perfil/Logo flotante
            Surface(
                modifier = Modifier.size(56.dp),
                shape = CircleShape,
                color = Color.White,
                shadowElevation = 10.dp,
                border = androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF673AB7))
            ) {
                Image(
                    painter = painterResource(id = R.mipmap.logo_foreground),
                    contentDescription = "Logo",
                    modifier = Modifier.padding(6.dp),
                    contentScale = ContentScale.Fit
                )
            }
        }
    }
}

@Composable
fun TenderPromoBanner() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .height(160.dp),
        shape = RoundedCornerShape(32.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(Color(0xFF7E57C2), Color(0xFFB39DDB))
                    )
                )
        ) {
            // Huellas de fondo decorativas
            Icon(
                Icons.Default.Pets, 
                null, 
                modifier = Modifier.size(120.dp).align(Alignment.CenterEnd).offset(x = 30.dp).rotate(20f),
                tint = Color.White.copy(alpha = 0.1f)
            )

            Row(
                modifier = Modifier.fillMaxSize().padding(24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1.2f)) {
                    Surface(
                        color = Color.White.copy(alpha = 0.2f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            "CAMPAÑA 2025",
                            color = Color.White,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Un hogar lleno de ronroneos y ladridos",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        lineHeight = 24.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                        shape = RoundedCornerShape(14.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp)
                    ) {
                        Text("Ver más", color = Color(0xFF673AB7), fontSize = 13.sp, fontWeight = FontWeight.ExtraBold)
                    }
                }
                Box(modifier = Modifier.weight(0.8f), contentAlignment = Alignment.Center) {
                    // Aquí iría una imagen de un perrito tierno en el futuro
                    Icon(Icons.Default.Favorite, null, modifier = Modifier.size(80.dp), tint = Color.White.copy(alpha = 0.3f))
                }
            }
        }
    }
}

@Composable
fun CategoryList(selectedCategory: String, onCategorySelected: (String) -> Unit) {
    val cats = listOf(
        Triple("Todos", Icons.Default.GridView, Color(0xFF673AB7)),
        Triple("Perros", Icons.Default.Pets, Color(0xFFFF7043)),
        Triple("Gatos", Icons.Default.Face, Color(0xFF42A5F5)),
        Triple("Otros", Icons.Default.AutoAwesome, Color(0xFF66BB6A)),
        Triple("Suministros", Icons.Default.Storefront, Color(0xFFEC407A))
    )
    
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        items(cats) { cat ->
            val isSelected = selectedCategory == cat.first
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onCategorySelected(cat.first) }
            ) {
                Surface(
                    modifier = Modifier.size(70.dp),
                    shape = RoundedCornerShape(22.dp),
                    color = if (isSelected) cat.third else Color.White,
                    shadowElevation = if (isSelected) 12.dp else 4.dp,
                    border = if (isSelected) null else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
                ) {
                    Icon(
                        cat.second, 
                        null, 
                        modifier = Modifier.padding(20.dp), 
                        tint = if (isSelected) Color.White else cat.third
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    cat.first, 
                    fontSize = 12.sp, 
                    color = if (isSelected) cat.third else Color(0xFF757575), 
                    fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun PremiumPetCard(mascota: Mascota) {
    Card(
        modifier = Modifier
            .width(200.dp)
            .shadow(12.dp, RoundedCornerShape(28.dp), ambientColor = Color(0xFF673AB7).copy(alpha = 0.2f)),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column {
            Box {
                Image(
                    painter = rememberAsyncImagePainter(mascota.fotoPrincipal),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentScale = ContentScale.Crop
                )
                
                // Badge de Género "Tierno"
                Surface(
                    modifier = Modifier.padding(14.dp).align(Alignment.TopEnd),
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.9f)
                ) {
                    Icon(
                        if (mascota.genero == "Macho") Icons.Default.Male else Icons.Default.Female,
                        contentDescription = null,
                        modifier = Modifier.padding(6.dp).size(18.dp),
                        tint = if (mascota.genero == "Macho") Color(0xFF2196F3) else Color(0xFFF06292)
                    )
                }

                // Badge de "Nuevo" o similar
                Surface(
                    modifier = Modifier.padding(14.dp).align(Alignment.BottomStart),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFF4CAF50)
                ) {
                    Text(
                        "DISPONIBLE",
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                        color = Color.White,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
            
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        mascota.nombre, 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 20.sp, 
                        color = Color(0xFF1A237E)
                    )
                    Icon(
                        Icons.Outlined.FavoriteBorder, 
                        null, 
                        tint = Color.LightGray, 
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Rounded.LocationOn, null, tint = Color(0xFF9575CD), modifier = Modifier.size(14.dp))
                    Text(
                        mascota.ubicacion, 
                        fontSize = 12.sp, 
                        color = Color.Gray, 
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(start = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ActionCard(title: String, subtitle: String, icon: ImageVector, color: Color, iconColor: Color, modifier: Modifier, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        modifier = modifier.height(80.dp),
        shape = RoundedCornerShape(24.dp),
        color = color
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(12.dp),
                color = Color.White.copy(alpha = 0.5f)
            ) {
                Icon(icon, null, modifier = Modifier.padding(8.dp), tint = iconColor)
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 14.sp, color = iconColor)
                Text(subtitle, fontSize = 11.sp, color = iconColor.copy(alpha = 0.7f), fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun TenderEventCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color(0xFFF3E5F5)),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("15", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = Color(0xFF673AB7))
                    Text("MAR", fontSize = 10.sp, color = Color(0xFF673AB7), fontWeight = FontWeight.Bold)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text("Jornada de Adopción", fontWeight = FontWeight.ExtraBold, fontSize = 15.sp, color = Color.Black)
                Text("Parque Simón Bolívar • 09:00", fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
            }
            IconButton(onClick = {}) {
                Icon(Icons.Rounded.NotificationsNone, null, tint = Color(0xFF673AB7))
            }
        }
    }
}

@Composable
fun SectionHeader(title: String, action: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            title, 
            fontSize = 20.sp, 
            fontWeight = FontWeight.ExtraBold, 
            color = Color(0xFF1A1A1A),
            letterSpacing = (-0.5).sp
        )
        Text(
            action, 
            fontSize = 13.sp, 
            color = Color(0xFF673AB7), 
            fontWeight = FontWeight.ExtraBold
        )
    }
}

@Composable
fun EmptyStateMessage() {
    Column(
        modifier = Modifier.fillMaxWidth().padding(40.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(Icons.Default.Pets, null, modifier = Modifier.size(60.dp), tint = Color.LightGray)
        Spacer(modifier = Modifier.height(12.dp))
        Text("No encontramos peluditos aquí", color = Color.Gray, fontWeight = FontWeight.Bold)
        Text("Prueba con otra categoría", color = Color.LightGray, fontSize = 12.sp)
    }
}
