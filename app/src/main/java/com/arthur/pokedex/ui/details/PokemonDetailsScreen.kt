package com.arthur.pokedex.ui.details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.arthur.pokedex.domain.model.PokemonDetails
import com.arthur.pokedex.domain.model.displayName
import com.arthur.pokedex.domain.model.pokedexNumber
import com.arthur.pokedex.ui.components.PokemonImage
import com.arthur.pokedex.ui.components.TypeRow
import com.arthur.pokedex.ui.home.ErrorContent
import com.arthur.pokedex.ui.home.Loading

@Composable fun DetailsRoute(viewModel: PokemonDetailsViewModel, onBack: () -> Unit) {
    val state = viewModel.uiState.collectAsStateWithLifecycle().value
    when { state.isLoading -> Loading(Modifier.fillMaxSize()); state.error != null -> ErrorContent(state.error, viewModel::load, Modifier.fillMaxSize()); state.pokemon != null -> DetailsScreen(state.pokemon, onBack) }
}
@Composable private fun DetailsScreen(pokemon: PokemonDetails, onBack: () -> Unit) = Column(Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(20.dp)) {
    Button(onClick = onBack) { Text("Voltar") }
    PokemonImage(pokemon.imageUrl, pokemon.name, Modifier.fillMaxWidth().size(250.dp))
    Text(pokemon.id.pokedexNumber(), color = MaterialTheme.colorScheme.onSurfaceVariant)
    Text(pokemon.name.displayName(), style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.Bold)
    TypeRow(pokemon.types, Modifier.padding(top = 8.dp, bottom = 20.dp))
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) { Info("Altura", "${pokemon.heightDecimeters / 10.0} m"); Info("Peso", "${pokemon.weightHectograms / 10.0} kg") }
    Section("Habilidades"); Text(pokemon.abilities.joinToString(" • ") { it.displayName() })
    Section("Estatísticas")
    pokemon.stats.forEach { stat -> StatRow(stat.name, stat.value) }
}
@Composable private fun Info(label: String, value: String) = Column(horizontalAlignment = Alignment.CenterHorizontally) { Text(value, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold); Text(label, color = MaterialTheme.colorScheme.onSurfaceVariant) }
@Composable private fun Section(title: String) { Text(title, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, modifier = Modifier.padding(top = 28.dp, bottom = 10.dp)); HorizontalDivider() }
@Composable private fun StatRow(name: String, value: Int) = Column(Modifier.padding(vertical = 7.dp)) { Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) { Text(name.displayName()); Text(value.toString(), fontWeight = FontWeight.Bold) }; LinearProgressIndicator(progress = { (value / 150f).coerceIn(0f, 1f) }, modifier = Modifier.fillMaxWidth().padding(top = 4.dp)) }
