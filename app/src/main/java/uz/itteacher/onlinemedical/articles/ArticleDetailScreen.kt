package com.example.onlinemedical

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ArticleDetailScreen(
    article: ArticleItem,
    onBackClick: () -> Unit,
    onAddToFavourites: (ArticleItem) -> Unit,
    isFavourite: Boolean
) {
    var favState by remember { mutableStateOf(isFavourite) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // 🔙 Back va yurakcha
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 12.dp, top = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }

            Row {
                IconButton(onClick = {
                    favState = !favState
                    onAddToFavourites(article)
                }) {
                    Icon(
                        imageVector = if (favState) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = "Favourite",
                        tint = if (favState) Color.Red else Color.Black
                    )
                }
                IconButton(onClick = { /* Share */ }) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color.Black
                    )
                }
            }
        }

        Image(
            painter = painterResource(id = article.imageRes),
            contentDescription = article.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(16.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(text = article.title, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = article.date, fontSize = 13.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Healthy lifestyle is not only about eating vegetables or avoiding fast food — it's about balance, exercise, and mental peace.",
            fontSize = 15.sp,
            lineHeight = 22.sp,
            color = Color.DarkGray
        )
    }
}
