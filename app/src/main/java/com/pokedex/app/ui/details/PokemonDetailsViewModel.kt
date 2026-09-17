package com.pokedex.app.ui.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.data.repository.userMessage
import com.pokedex.app.domain.model.PokemonDetails
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsUiState(val pokemon: PokemonDetails? = null, val isLoading: Boolean = true, val error: String? = null)
class PokemonDetailsViewModel(private val pokemonId: Int, private val repository: PokemonRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState: StateFlow<DetailsUiState> = _uiState.asStateFlow()
    init { load() }
    fun load() = viewModelScope.launch {
        _uiState.update { it.copy(isLoading = true, error = null) }
        runCatching { repository.getPokemon(pokemonId.toString()) }
            .onSuccess { _uiState.value = DetailsUiState(pokemon = it, isLoading = false) }
            .onFailure { _uiState.value = DetailsUiState(isLoading = false, error = it.userMessage()) }
    }
}
class DetailsViewModelFactory(private val id: Int, private val repository: PokemonRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = PokemonDetailsViewModel(id, repository) as T
}
