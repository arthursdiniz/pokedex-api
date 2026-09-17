package com.arthur.pokedex.domain.model

data class Pokemon(val id: Int, val name: String, val imageUrl: String?, val types: List<String>)
data class PokemonDetails(
    val id: Int, val name: String, val imageUrl: String?, val types: List<String>,
    val heightDecimeters: Int, val weightHectograms: Int, val abilities: List<String>, val stats: List<PokemonStat>
)
data class PokemonStat(val name: String, val value: Int)

fun Int.pokedexNumber() = "#${toString().padStart(3, '0')}"
fun String.displayName() = replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }.replace('-', ' ')
