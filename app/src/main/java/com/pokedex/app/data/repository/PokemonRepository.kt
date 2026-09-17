package com.pokedex.app.data.repository

import com.pokedex.app.data.remote.api.PokeApiService
import com.pokedex.app.data.remote.dto.PokemonDetailsDto
import com.pokedex.app.data.remote.dto.PokemonListItemDto
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.PokemonDetails
import com.pokedex.app.domain.model.PokemonStat
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.HttpException
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.io.IOException
import java.util.concurrent.TimeUnit
import okhttp3.MediaType.Companion.toMediaType

class PokemonRepository(private val api: PokeApiService) {
    suspend fun getPokemonPage(limit: Int, offset: Int): List<Pokemon> = coroutineScope {
        api.getPokemonList(limit, offset).results.map { item -> async { api.getPokemon(item.name).toPokemon() } }.awaitAll()
    }
    suspend fun getPokemon(nameOrId: String): PokemonDetails = api.getPokemon(nameOrId).toDetails()

    companion object {
        fun create(): PokemonRepository {
            val json = Json { ignoreUnknownKeys = true }
            val client = OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS).readTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(HttpLoggingInterceptor().apply { level = HttpLoggingInterceptor.Level.BASIC }).build()
            val api = Retrofit.Builder().baseUrl("https://pokeapi.co/api/v2/").client(client)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType())).build().create(PokeApiService::class.java)
            return PokemonRepository(api)
        }
    }
}

fun PokemonDetailsDto.toPokemon() = Pokemon(id, name, artworkUrl(), types.sortedBy { it.slot }.map { it.type.name })
fun PokemonDetailsDto.toDetails() = PokemonDetails(id, name, artworkUrl(), types.sortedBy { it.slot }.map { it.type.name }, height, weight, abilities.map { it.ability.name }, stats.map { PokemonStat(it.stat.name, it.baseStat) })
private fun PokemonDetailsDto.artworkUrl() = sprites.other?.officialArtwork?.frontDefault ?: sprites.frontDefault

fun Throwable.userMessage(): String = when (this) {
    is IOException -> "Verifique sua conexão com a internet e tente novamente."
    is HttpException -> if (code() == 404) "Pokémon não encontrado." else "O servidor não respondeu. Tente novamente em instantes."
    else -> "Ocorreu um erro inesperado. Tente novamente."
}
