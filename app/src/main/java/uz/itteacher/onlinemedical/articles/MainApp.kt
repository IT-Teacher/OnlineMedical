package com.example.onlinemedical

import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.listSaver
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun MainApp() {
    val navController = rememberNavController()

    var favouriteList by rememberSaveable(
        stateSaver = listSaver(
            save = { list -> list.map { it.title } },
            restore = { titles -> getArticleList().filter { it.title in titles } }
        )
    ) {
        mutableStateOf(emptyList<ArticleItem>())
    }

    NavHost(navController = navController, startDestination = "main") {

        // 🏠 Asosiy sahifa
        composable("main") {
            MainScreen(
                onNavigateToSeeAll = { title ->
                    navController.navigate("seeAll/$title")
                },
                onArticleClick = { articleTitle ->
                    navController.navigate("articleDetail/$articleTitle")
                },
                onOpenFavourites = { navController.navigate("favourites") }
            )
        }

        // 📖 See All sahifa
        composable(
            route = "seeAll/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            SeeAllScreen(
                title = title,
                onBackClick = { navController.popBackStack() },
                onArticleClick = { articleTitle ->
                    navController.navigate("articleDetail/$articleTitle")
                }
            )
        }

        // 📄 Maqola tafsiloti
        composable(
            route = "articleDetail/{title}",
            arguments = listOf(navArgument("title") { type = NavType.StringType })
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val article = getArticleList().find { it.title == title }
            article?.let {
                ArticleDetailScreen(
                    article = it,
                    onBackClick = { navController.popBackStack() },
                    onAddToFavourites = { fav ->
                        favouriteList = if (favouriteList.contains(fav)) {
                            favouriteList - fav
                        } else {
                            favouriteList + fav
                        }
                    },
                    isFavourite = favouriteList.contains(it)
                )
            }
        }

        // ❤️ Favourites sahifa
        composable("favourites") {
            FavouritesScreen(
                favourites = favouriteList,
                onBackClick = { navController.popBackStack() },
                onArticleClick = { articleTitle ->
                    navController.navigate("articleDetail/$articleTitle")
                },
                onRemoveFavourite = { fav ->
                    favouriteList = favouriteList - fav
                }
            )
        }
    }
}
