package com.arthur.pokedex

import com.arthur.pokedex.data.remote.dto.AbilitySlotDto
import com.arthur.pokedex.data.remote.dto.ArtworkDto
import com.arthur.pokedex.data.remote.dto.NamedResourceDto
import com.arthur.pokedex.data.remote.dto.OtherSpritesDto
import com.arthur.pokedex.data.remote.dto.PokemonDetailsDto
import com.arthur.pokedex.data.remote.dto.SpritesDto
import com.arthur.pokedex.data.remote.dto.StatSlotDto
import com.arthur.pokedex.data.remote.dto.TypeSlotDto
import com.arthur.pokedex.data.repository.toDetails
import com.arthur.pokedex.domain.model.pokedexNumber
import org.junit.Assert.assertEquals
import org.junit.Test

class PokemonModelTest {
    @Test fun `formats number with leading zeroes`() { assertEquals("#001", 1.pokedexNumber()); assertEquals("#025", 25.pokedexNumber()) }
    @Test fun `maps only app detail fields from dto`() {
        val dto = PokemonDetailsDto(25, "pikachu", 4, 60, listOf(AbilitySlotDto(NamedResourceDto("static"))), listOf(StatSlotDto(35, NamedResourceDto("hp"))), listOf(TypeSlotDto(1, NamedResourceDto("electric"))), SpritesDto(OtherSpritesDto(ArtworkDto("image"))))
        val pokemon = dto.toDetails()
        assertEquals("image", pokemon.imageUrl); assertEquals(listOf("electric"), pokemon.types); assertEquals(35, pokemon.stats.single().value)
    }
}
