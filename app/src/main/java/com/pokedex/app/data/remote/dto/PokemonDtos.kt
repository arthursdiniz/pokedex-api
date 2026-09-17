package com.pokedex.app.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable data class PokemonListResponseDto(val results: List<PokemonListItemDto>)
@Serializable data class PokemonListItemDto(val name: String, val url: String)
@Serializable data class PokemonDetailsDto(
    val id: Int, val name: String, val height: Int, val weight: Int,
    val abilities: List<AbilitySlotDto>, val stats: List<StatSlotDto>, val types: List<TypeSlotDto>,
    val sprites: SpritesDto
)
@Serializable data class AbilitySlotDto(val ability: NamedResourceDto)
@Serializable data class StatSlotDto(@SerialName("base_stat") val baseStat: Int, val stat: NamedResourceDto)
@Serializable data class TypeSlotDto(val slot: Int, val type: NamedResourceDto)
@Serializable data class NamedResourceDto(val name: String)
@Serializable data class SpritesDto(@SerialName("other") val other: OtherSpritesDto? = null, @SerialName("front_default") val frontDefault: String? = null)
@Serializable data class OtherSpritesDto(@SerialName("official-artwork") val officialArtwork: ArtworkDto? = null)
@Serializable data class ArtworkDto(@SerialName("front_default") val frontDefault: String? = null)
