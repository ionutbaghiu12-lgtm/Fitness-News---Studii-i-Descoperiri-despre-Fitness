package com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.ui.NewsViewModel
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.ui.screens.ArticleDetailScreen
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.ui.screens.FavoritesScreen
import com.example.calcul_mobil_fitnessnews_studiiidescoperiridesprefitness.ui.screens.NewsListScreen

sealed class Screen(val route: String) {
    object Feed : Screen("feed")
    object Favorites : Screen("favorites")
    object Detail : Screen("detail/{articleId}") {
        fun createRoute(articleId: Int) = "detail/$articleId"
    }
}

@Composable
fun FitnessNavGraph(
    navController: NavHostController,
    viewModel: NewsViewModel,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Feed.route,
        modifier = modifier
    ) {
        composable(Screen.Feed.route) {
            NewsListScreen(
                viewModel = viewModel,
                onArticleClick = { articleId ->
                    navController.navigate(Screen.Detail.createRoute(articleId))
                }
            )
        }

        composable(Screen.Favorites.route) {
            FavoritesScreen(
                viewModel = viewModel,
                onArticleClick = { articleId ->
                    navController.navigate(Screen.Detail.createRoute(articleId))
                }
            )
        }

        composable(
            route = Screen.Detail.route,
            arguments = listOf(navArgument("articleId") { type = NavType.IntType })
        ) { backStackEntry ->
            val articleId = backStackEntry.arguments?.getInt("articleId") ?: 0
            ArticleDetailScreen(
                articleId = articleId,
                viewModel = viewModel,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
