package com.pokedex.app.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.pokedex.app.domain.model.Pokemon
import com.pokedex.app.domain.model.displayName
import com.pokedex.app.domain.model.pokedexNumber
import com.pokedex.app.ui.components.PokemonImage
import com.pokedex.app.ui.components.TypeRow

@Composable fun HomeRoute(viewModel: HomeViewModel, onPokemonSelected: (Int) -> Unit) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    HomeScreen(state, viewModel::onQueryChanged, viewModel::loadNextPage, viewModel::retry, onPokemonSelected)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable fun HomeScreen(state: HomeUiState, onQueryChanged: (String) -> Unit, onLoadMore: () -> Unit, onRetry: () -> Unit, onPokemonSelected: (Int) -> Unit) {
    Column(Modifier.fillMaxSize().padding(horizontal = 20.dp)) {
        Text("Pokédex", style = MaterialTheme.typography.displaySmall, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 24.dp))
        Text("Explore todos os Pokémon", style = MaterialTheme.typography.bodyLarge, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(top = 4.dp, bottom = 16.dp))
        OutlinedTextField(value = state.query, onValueChange = onQueryChanged, singleLine = true, modifier = Modifier.fillMaxWidth(), label = { Text("Buscar Pokémon...") })
        when {
            state.isInitialLoading -> Loading(Modifier.weight(1f))
            state.error != null && state.pokemon.isEmpty() -> ErrorContent(state.error, onRetry, Modifier.weight(1f))
            else -> PokemonGrid(state, onLoadMore, onPokemonSelected)
        }
    }
}

@Composable private fun PokemonGrid(state: HomeUiState, onLoadMore: () -> Unit, onPokemonSelected: (Int) -> Unit) {
    if (state.pokemon.isEmpty() && !state.isSearching) {
        Text("Nenhum Pokémon encontrado.", modifier = Modifier.fillMaxSize().padding(top = 32.dp))
        return
    }
    LazyVerticalGrid(columns = GridCells.Fixed(2), contentPadding = PaddingValues(vertical = 16.dp), horizontalArrangement = Arrangement.spacedBy(12.dp), verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxSize()) {
        items(state.pokemon, key = { it.id }) { pokemon ->
            PokemonCard(pokemon, onPokemonSelected)
            if (pokemon == state.pokemon.getOrNull(state.pokemon.size - 5)) LaunchedEffect(pokemon.id) { onLoadMore() }
        }
        if (state.isLoadingMore || state.isSearching) item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) { Loading(Modifier.fillMaxWidth().padding(16.dp)) }
        if (state.error != null && state.pokemon.isNotEmpty()) item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) { ErrorContent(state.error, onLoadMore, Modifier.fillMaxWidth()) }
    }
}

@Composable private fun PokemonCard(pokemon: Pokemon, onSelect: (Int) -> Unit) = Card(Modifier.fillMaxWidth().clickable { onSelect(pokemon.id) }) {
    Column(Modifier.padding(12.dp)) {
        PokemonImage(pokemon.imageUrl, pokemon.name, Modifier.fillMaxWidth().size(128.dp))
        Text(pokemon.id.pokedexNumber(), style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
        Text(pokemon.name.displayName(), style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold, maxLines = 1)
        TypeRow(pokemon.types, Modifier.padding(top = 6.dp))
    }
}

@Composable fun Loading(modifier: Modifier = Modifier) = androidx.compose.foundation.layout.Box(modifier, contentAlignment = Alignment.Center) { CircularProgressIndicator() }
@Composable fun ErrorContent(message: String, onRetry: () -> Unit, modifier: Modifier = Modifier) = Column(modifier, horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) { Text("Não foi possível carregar os Pokémon.", style = MaterialTheme.typography.titleMedium); Text(message, modifier = Modifier.padding(vertical = 8.dp)); Button(onClick = onRetry) { Text("Tentar novamente") } }
