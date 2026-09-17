package com.arthur.pokedex.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.arthur.pokedex.data.repository.PokemonRepository
import com.arthur.pokedex.data.repository.userMessage
import com.arthur.pokedex.domain.model.Pokemon
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiState(
    val pokemon: List<Pokemon> = emptyList(), val query: String = "", val isInitialLoading: Boolean = true,
    val isLoadingMore: Boolean = false, val isSearching: Boolean = false, val error: String? = null,
    val hasMore: Boolean = true
)

@OptIn(FlowPreview::class)
class HomeViewModel(private val repository: PokemonRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    private var offset = 0
    private var isRequesting = false
    private var loadedPokemon: List<Pokemon> = emptyList()

    init {
        loadNextPage()
        viewModelScope.launch {
            _uiState.debounce(400).distinctUntilChanged { old, new -> old.query == new.query }
                .filter { it.query.isNotBlank() }.collect { search(it.query) }
        }
    }

    fun onQueryChanged(value: String) {
        val normalized = value.trim().lowercase()
        _uiState.update {
            if (normalized.isBlank()) it.copy(query = "", pokemon = loadedPokemon, error = null, isSearching = false)
            else it.copy(query = normalized, error = null, isSearching = true)
        }
    }

    fun loadNextPage() {
        val state = _uiState.value
        if (isRequesting || !state.hasMore || state.query.isNotBlank()) return
        isRequesting = true
        _uiState.update { it.copy(isInitialLoading = it.pokemon.isEmpty(), isLoadingMore = it.pokemon.isNotEmpty(), error = null) }
        viewModelScope.launch {
            runCatching { repository.getPokemonPage(PAGE_SIZE, offset) }
                .onSuccess { page ->
                    offset += page.size
                    loadedPokemon = loadedPokemon + page
                    _uiState.update { it.copy(pokemon = loadedPokemon, isInitialLoading = false, isLoadingMore = false, hasMore = page.size == PAGE_SIZE) }
                }.onFailure { throwable -> _uiState.update { it.copy(isInitialLoading = false, isLoadingMore = false, error = throwable.userMessage()) } }
            isRequesting = false
        }
    }

    private fun search(query: String) {
        viewModelScope.launch {
            runCatching { repository.getPokemon(query) }
                .onSuccess { result -> if (_uiState.value.query == query) _uiState.update { it.copy(pokemon = listOf(result.toPokemon()), isSearching = false, error = null) } }
                .onFailure { throwable -> if (_uiState.value.query == query) _uiState.update { it.copy(pokemon = emptyList(), isSearching = false, error = throwable.userMessage()) } }
        }
    }

    fun retry() { if (_uiState.value.query.isBlank()) loadNextPage() else search(_uiState.value.query) }
    companion object { const val PAGE_SIZE = 20 }
}

class HomeViewModelFactory(private val repository: PokemonRepository) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST") override fun <T : ViewModel> create(modelClass: Class<T>): T = HomeViewModel(repository) as T
}

private fun com.arthur.pokedex.domain.model.PokemonDetails.toPokemon() = Pokemon(id, name, imageUrl, types)
