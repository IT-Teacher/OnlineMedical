package com.example.appointment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

data class Review(
    val name: String,
    val avatarRes: Int? = null,
    val rating: Int,
    val text: String,
    val likes: Int,
    val daysAgo: String
)

@Composable
fun ReviewsScreen(reviews: List<Review>, navController: NavController) {
    var selected by remember { mutableStateOf("All") }

    val filtered = if (selected == "All") {
        reviews
    } else {
        val want = selected.toIntOrNull()
        if (want == null) reviews else reviews.filter { it.rating == want }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        TopBar(
            title = "4.8 (4,942 reviews)",
            onBack = { navController.navigate("profile") },
            onMenu = {}
        )

        RatingFilters(selected = selected, onSelect = { selected = it })

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            items(filtered) { review ->
                ReviewItem(review = review)
                Divider(
                    color = Color(0xFFE0E0E0),
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )
            }
        }
    }
}

@Composable
fun TopBar(title: String, onBack: () -> Unit, onMenu: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 36.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBack) {
            Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
        }

        Text(
            text = title,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.weight(1f),
            color = Color.Black,
        )

        IconButton(onClick = onMenu) {
            Icon(Icons.Default.MoreVert, contentDescription = "Menu", tint = Color.Black)
        }
    }
}

@Composable
fun RatingFilters(selected: String, onSelect: (String) -> Unit) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            val items = listOf("All", "5", "4", "3", "2")
            items.forEachIndexed { index, item ->
                FilterChip(
                    text = item,
                    selected = (selected == item),
                    onClick = { onSelect(item) }
                )
                if (index < items.lastIndex) Spacer(modifier = Modifier.width(8.dp))
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
fun FilterChip(text: String, selected: Boolean, onClick: () -> Unit) {
    val blue = Color(0xFF0D6EFD)
    Surface(
        modifier = Modifier
            .height(36.dp)
            .wrapContentWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        color = if (selected) Color(0xFF0D6EFD) else Color(0xFFF0F0F0),
        border = BorderStroke(2.dp, blue)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (text != "All") {
                Icon(
                    Icons.Default.Star,
                    contentDescription = null,
                    tint = if (selected) Color.White else Color(0xFF0D6EFD),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
            }
            Text(
                text = text,
                color = if (selected) Color.White else Color.Black,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
fun ReviewItem(review: Review) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = review.avatarRes ?: R.drawable.doctor_avatar),
                contentDescription = null,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = review.name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    RatingBubbleNumeric(review.rating)

                    Spacer(modifier = Modifier.width(8.dp))

                    IconButton(
                        onClick = {  },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More",
                            tint = Color.DarkGray
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = review.text,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Favorite,
                contentDescription = "likes",
                tint = Color(0xFF0D6EFD),
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = review.likes.toString(), color = Color(0xFF0D6EFD), fontSize = 13.sp)
            Spacer(modifier = Modifier.width(19.dp))
            Text(
                text = review.daysAgo,
                color = Color.Gray,
                fontSize = 13.sp
            )
        }
    }
}

@Composable
fun RatingBubbleNumeric(rating: Int) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(2.dp, Color(0xFF0D6EFD)),
        color = Color.White,
        modifier = Modifier
            .height(30.dp)
            .wrapContentWidth()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 10.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFF0D6EFD),
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(text = rating.toString(), color = Color(0xFF0D6EFD), fontWeight = FontWeight.SemiBold)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ReviewsPreview() {
    val sample = listOf(
        Review(
            name = "Charolette Hanlin",
            avatarRes = R.drawable.charolette,
            rating = 5,
            text = "Dr. Jenny is very professional in her work and responsive. I have consulted and my problem is solved. 😍😍",
            likes = 938,
            daysAgo = "6 days ago"
        ),
        Review(
            name = "Darron Kulikowski",
            avatarRes = R.drawable.darron,
            rating = 4,
            text = "The doctor is very beautiful and the service is excellent!! I like it and want to consult again 😂😂",
            likes = 863,
            daysAgo = "6 days ago"
        )
    )
    ReviewsScreen(reviews = sample, navController = rememberNavController())
}