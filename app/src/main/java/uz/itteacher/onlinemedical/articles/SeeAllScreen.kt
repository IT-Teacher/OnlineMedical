package com.example.onlinemedical

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.Alignment
import androidx.compose.ui.draw.clip

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeeAllScreen(
    title: String,
    onBackClick: () -> Unit,
    onArticleClick: (String) -> Unit // 🔹 Bosilganda detail screen ochiladi
) {
    var selectedCategory by remember { mutableStateOf("Newest") }
    var searchQuery by remember { mutableStateOf("") }
    var isSearching by remember { mutableStateOf(false) }

    val categories = listOf("Newest", "Health", "Covid-19", "Lifestyle", "Medical")
    val allArticles = getArticleList() // 🔹 ArticleItem ro‘yxatini olamiz

    val filteredArticles = allArticles.filter {
        (selectedCategory == "Newest" || it.category == selectedCategory) &&
                it.title.contains(searchQuery, ignoreCase = true)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFF)
    ) {
        Column {
            // 🔹 Top App Bar
            TopAppBar(
                title = {
                    if (isSearching) {
                        TextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search articles...") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                cursorColor = Color(0xFF3366FF),
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                disabledIndicatorColor = Color.Transparent
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    } else {
                        Text(
                            text = title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            color = Color.Black
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (isSearching) {
                            isSearching = false
                            searchQuery = ""
                        } else onBackClick()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.Black
                        )
                    }
                },
                actions = {
                    if (isSearching) {
                        IconButton(onClick = {
                            searchQuery = ""
                            isSearching = false
                        }) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Close search",
                                tint = Color.Black
                            )
                        }
                    } else {
                        IconButton(onClick = { isSearching = true }) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Color.Black
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )

            // 🔹 Category filter tugmalari
            Row(
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                categories.forEach { category ->
                    val isSelected = category == selectedCategory
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(if (isSelected) Color(0xFF007BFF) else Color.White)
                            .border(
                                1.5.dp,
                                if (isSelected) Color(0xFF007BFF) else Color(0xFFB0C4DE),
                                CircleShape
                            )
                            .clickable { selectedCategory = category }
                            .padding(horizontal = 18.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = category,
                            color = if (isSelected) Color.White else Color(0xFF007BFF),
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    }
                }
            }

            // 🔹 Filtrlab chiqqan maqolalarni chiqarish
            LazyColumn(
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                items(filteredArticles) { article ->
                    ArticleListCard(
                        article = article,
                        isFavourite = false,
                        onToggleFavourite = {},
                        onClick = { onArticleClick(article.title) }
                    )



                }
            }
        }
    }
}
