package com.example.onlinemedical

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavouritesScreen(
    favourites: List<ArticleItem>,
    onBackClick: () -> Unit,
    onArticleClick: (String) -> Unit,
    onRemoveFavourite: (ArticleItem) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Favourites", color = Color.Black) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        if (favourites.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = androidx.compose.ui.Alignment.Center
            ) {
                Text("No favourites yet ❤️")
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.padding(padding)
            ) {
                items(favourites) { article ->
                    ArticleListCard(
                        article = article,
                        onClick = { onArticleClick(article.title) },
                        isFavourite = true,
                        onToggleFavourite = { onRemoveFavourite(article) }
                    )
                }
            }
        }
    }
}
