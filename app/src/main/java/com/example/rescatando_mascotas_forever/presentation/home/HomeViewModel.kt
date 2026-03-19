package com.example.rescatando_mascotas_forever.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rescatando_mascotas_forever.data.network.models.Mascota
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val allMascotas = listOf(
        Mascota(
            id = 1,
            nombre = "Luna",
            especie = "Perros",
            edadAprox = 2,
            genero = "Hembra",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Es muy juguetona y cariñosa.",
            fotoPrincipal = "https://images.dog.ceo/breeds/retriever-golden/n02099601_3004.jpg",
            aptoConNinos = true,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        ),
        Mascota(
            id = 2,
            nombre = "Simba",
            especie = "Gatos",
            edadAprox = 1,
            genero = "Macho",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Gatito rescatado, muy tranquilo.",
            fotoPrincipal = "https://images.ctfassets.net/denf86kkcx7r/4IPlg4Qazd4sFRuCUHIJ1T/f6c71da7eec727babcd554d843a528b8/gatocomuneuropeo-97",
            aptoConNinos = true,
            aptoConOtrosAnimales = false,
            fundacionId = 1
        ),
        Mascota(
            id = 3,
            nombre = "Rocky",
            especie = "Perros",
            edadAprox = 4,
            genero = "Macho",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Busca un hogar activo.",
            fotoPrincipal = "https://images.ctfassets.net/denf86kkcx7r/HJO06XFEAWjMW42CkMPQz/c3cb44ef5b0815101349affd2353033e/Beagle.webp?fm=webp&w=913",
            aptoConNinos = true,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        ),
        Mascota(
            id = 4,
            nombre = "Mora",
            especie = "Perros",
            edadAprox = 3,
            genero = "Hembra",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Busca una familia activa.",
            fotoPrincipal = "https://images.dog.ceo/breeds/labrador/n02099712_3503.jpg",
            aptoConNinos = false,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        ),
        Mascota(
            id = 5,
            nombre = "Boni",
            especie = "Otros",
            edadAprox = 1,
            genero = "Hembra",
            estado = "En adopcion",
            ubicacion = "Popayán, Cauca",
            descripcion = "Conejita rescatada.",
            fotoPrincipal = "https://images.unsplash.com/photo-1585110396000-c9ffd4e4b308",
            aptoConNinos = true,
            aptoConOtrosAnimales = true,
            fundacionId = 1
        )
    )

    private val _mascotas = MutableStateFlow<List<Mascota>>(allMascotas)
    val mascotas: StateFlow<List<Mascota>> = _mascotas

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _selectedCategory = MutableStateFlow("Todos")
    val selectedCategory: StateFlow<String> = _selectedCategory

    fun filterByCategory(category: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _selectedCategory.value = category
            delay(500)
            
            _mascotas.value = if (category == "Todos") {
                allMascotas
            } else if (category == "Suministros") {
                emptyList() // O manejar lógica de tienda
            } else {
                allMascotas.filter { it.especie == category }
            }
            _isLoading.value = false
        }
    }
}
