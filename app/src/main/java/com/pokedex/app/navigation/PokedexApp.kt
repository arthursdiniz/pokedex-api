package com.pokedex.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.NavType
import com.pokedex.app.data.repository.PokemonRepository
import com.pokedex.app.ui.details.DetailsRoute
import com.pokedex.app.ui.details.DetailsViewModelFactory
import com.pokedex.app.ui.details.PokemonDetailsViewModel
import com.pokedex.app.ui.home.HomeRoute
import com.pokedex.app.ui.home.HomeViewModel
import com.pokedex.app.ui.home.HomeViewModelFactory

private const val HOME = "home"
private const val DETAILS = "details"

@Composable fun PokedexApp() {
    val navController = rememberNavController()
    val repository = remember { PokemonRepository.create() }
    NavHost(navController, startDestination = HOME) {
        composable(HOME) {
            val viewModel: HomeViewModel = viewModel(factory = HomeViewModelFactory(repository))
            HomeRoute(viewModel) { id -> navController.navigate("$DETAILS/$id") }
        }
        composable("$DETAILS/{id}", arguments = listOf(navArgument("id") { type = NavType.IntType })) { entry ->
            val id = entry.arguments?.getInt("id") ?: return@composable
            val viewModel: PokemonDetailsViewModel = viewModel(factory = DetailsViewModelFactory(id, repository))
            DetailsRoute(viewModel) { navController.popBackStack() }
        }
    }
}
