package com.example.onlinemedical

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itteacher.onlinemedical.R

@Composable
fun MainScreen(
    onNavigateToSeeAll: (String) -> Unit,
    onArticleClick: (String) -> Unit,
    onOpenFavourites: () -> Unit
) {
    val selectedCategory = remember { mutableStateOf("Newest") }
    var isSearchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    // ❤️ Favourites list saqlanadi (hatto qaytganingda ham)
    val favouriteList = rememberSaveable { mutableStateListOf<String>() }

    val allArticles = getArticleList()
    val filteredArticles = allArticles.filter { article ->
        (selectedCategory.value == "Newest" || article.category == selectedCategory.value) &&
                (searchQuery.isEmpty() || article.title.contains(searchQuery, ignoreCase = true))
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color(0xFFF8FAFF)
    ) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp)
        ) {
            // 🔍 Yuqoridagi Top Bar (Search + Favourites)
            ArticlesTopBar(
                onSearchClicked = { isSearchActive = !isSearchActive },
                onOpenFavourites = onOpenFavourites
            )

            Spacer(modifier = Modifier.height(14.dp))

            if (isSearchActive) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search articles...") },
                    modifier = Modifier.fillMaxWidth(),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") }
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            TrendingSection(
                onSeeAllClick = { onNavigateToSeeAll("Trending") },
                onArticleClick = onArticleClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            CategoryFilterRow(
                selectedCategory = selectedCategory.value,
                onCategorySelected = { selectedCategory.value = it },
                onSeeAllClick = { onNavigateToSeeAll("Articles") }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 🔹 Asosiy maqolalar ro‘yxati
            filteredArticles.forEach { article ->
                val isFav = favouriteList.contains(article.title)
                ArticleListCard(
                    article = article,
                    onClick = { onArticleClick(article.title) },
                    isFavourite = isFav,
                    onToggleFavourite = {
                        if (isFav) favouriteList.remove(article.title)
                        else favouriteList.add(article.title)
                    }
                )
                Spacer(modifier = Modifier.height(14.dp))
            }

            // ❤️ Favourites soni pastda
            if (favouriteList.isNotEmpty()) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "❤️ ${favouriteList.size} articles in favourites",
                    color = Color(0xFF007BFF),
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun ArticlesTopBar(
    onSearchClicked: () -> Unit,
    onOpenFavourites: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 28.dp, bottom = 10.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = painterResource(id = R.drawable.app_icon),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(30.dp)
//                    .background(Color.LightGray, shape = CircleShape)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Articles",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search",
                tint = Color(0xFF1E1E1E),
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onSearchClicked() }
            )
            Spacer(modifier = Modifier.width(18.dp))
            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Open favourites",
                tint = Color(0xFF1E1E1E),
                modifier = Modifier
                    .size(26.dp)
                    .clickable { onOpenFavourites() }
            )
        }
    }
}

@Composable
fun TrendingSection(
    onSeeAllClick: () -> Unit,
    onArticleClick: (String) -> Unit
) {
    val trendingArticles = getArticleList().take(4)
    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Trending", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
            Text(
                text = "See All",
                fontSize = 16.sp,
                color = Color(0xFF007BFF),
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.clickable { onSeeAllClick() }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            items(trendingArticles) { article ->
                TrendingCard(
                    article = article,
                    onClick = { onArticleClick(article.title) }
                )
            }
        }
    }
}

@Composable
fun TrendingCard(article: ArticleItem, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(200.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(bottom = 8.dp)
    ) {
        Image(
            painter = painterResource(id = article.imageRes),
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(130.dp)
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = article.title,
            fontSize = 15.sp,
            color = Color.Black,
            fontWeight = FontWeight.Medium,
            maxLines = 2,
            modifier = Modifier.padding(horizontal = 10.dp)
        )
    }
}

@Composable
fun CategoryFilterRow(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    onSeeAllClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("Articles", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(
            text = "See All",
            fontSize = 16.sp,
            color = Color(0xFF007BFF),
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable { onSeeAllClick() }
        )
    }

    Spacer(modifier = Modifier.height(12.dp))

    val categories = listOf("Newest", "Health", "Covid-19", "Lifestyle", "Nutrition", "Fitness")

    LazyRow(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
        items(categories) { category ->
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
                    .clickable { onCategorySelected(category) }
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
}

fun getArticleList(): List<ArticleItem> = listOf(
    ArticleItem(R.drawable.covid, "COVID-19 Was a Top Cause of Death in 2020 and 2021", "Dec 22, 2022", "Covid-19"),
    ArticleItem(R.drawable.hungry, "Study Finds Being 'Hangry' is Real", "Dec 22, 2022", "Health"),
    ArticleItem(R.drawable.child, "Why Childhood Obesity Rates Are Rising", "Dec 21, 2022", "Lifestyle"),
    ArticleItem(R.drawable.salt, "Salt Intake Linked to Blood Pressure", "Dec 20, 2022", "Nutrition")
)

data class ArticleItem(val imageRes: Int, val title: String, val date: String, val category: String)

@Composable
fun ArticleListCard(
    article: ArticleItem,
    onClick: () -> Unit,
    isFavourite: Boolean,
    onToggleFavourite: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White)
            .clickable { onClick() }
            .padding(10.dp),
        verticalAlignment = Alignment.Top
    ) {
        Image(
            painter = painterResource(id = article.imageRes),
            contentDescription = article.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(85.dp)
                .clip(RoundedCornerShape(10.dp))
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(text = article.date, fontSize = 13.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = article.title,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.Black,
                maxLines = 2
            )
            Spacer(modifier = Modifier.height(6.dp))
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFE7F0FF))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = article.category,
                    fontSize = 13.sp,
                    color = Color(0xFF007BFF),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
