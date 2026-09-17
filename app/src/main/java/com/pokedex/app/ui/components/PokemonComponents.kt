package com.pokedex.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.pokedex.app.domain.model.displayName

fun typeColor(type: String) = when (type.lowercase()) {
    "fire" -> Color(0xFFEF5350); "water" -> Color(0xFF42A5F5); "grass", "bug" -> Color(0xFF66BB6A)
    "electric" -> Color(0xFFFDD835); "poison", "ghost" -> Color(0xFFAB47BC); "psychic", "fairy" -> Color(0xFFEC407A)
    "ice" -> Color(0xFF4DD0E1); "fighting", "dragon" -> Color(0xFFEF6C00); "rock", "ground" -> Color(0xFF8D6E63)
    "dark" -> Color(0xFF5D4037); "steel" -> Color(0xFF90A4AE); "flying" -> Color(0xFF7986CB); else -> Color(0xFF78909C)
}

@Composable fun TypeChip(type: String) {
    Text(type.displayName(), style = MaterialTheme.typography.labelSmall, color = Color.White, modifier = Modifier.clip(RoundedCornerShape(12.dp)).background(typeColor(type)).padding(horizontal = 8.dp, vertical = 4.dp))
}

@Composable fun PokemonImage(url: String?, name: String, modifier: Modifier = Modifier) {
    val painter = rememberAsyncImagePainter(url)
    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        when (painter.state) {
            is AsyncImagePainter.State.Loading -> CircularProgressIndicator(modifier = Modifier.size(26.dp), strokeWidth = 2.dp)
            is AsyncImagePainter.State.Error -> Text("?", style = MaterialTheme.typography.headlineLarge)
            else -> Unit
        }
        if (painter.state !is AsyncImagePainter.State.Error) AsyncImage(model = url, contentDescription = name, modifier = Modifier.fillMaxSize(), contentScale = ContentScale.Fit)
    }
}

@Composable fun TypeRow(types: List<String>, modifier: Modifier = Modifier) = Row(modifier, horizontalArrangement = Arrangement.spacedBy(4.dp)) { types.forEach { TypeChip(it) } }
