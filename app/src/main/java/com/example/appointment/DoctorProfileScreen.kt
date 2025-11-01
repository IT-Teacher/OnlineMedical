package com.example.appointment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class DoctorProfile(
    val name: String,
    val specialty: String,
    val hospital: String,
    val patients: String,
    val experience: String,
    val rating: String,
    val reviewsCount: String,
    val about: String,
    val workingTime: String,
    val reviews: List<Review>
)


@Composable
fun StatItem(iconRes: Int, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = Color(0xFF0D6EFD).copy(alpha = 0.1f),
            modifier = Modifier.size(48.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = label,
                    modifier = Modifier.size(24.dp),
                    colorFilter = null
                )
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0D6EFD))
        Text(label, fontSize = 12.sp, color = Color.Black)
    }
}
@Composable
fun DoctorProfileScreen(navController: NavController, profile: DoctorProfile) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = { navController.navigate("book_appointment") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0D6EFD))
            ) {
                Text("Book Appointment", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {  }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
                }
                Text(
                    text = profile.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f),
                    color=Color(0xFF000000)
                )
                IconButton(onClick = {  }) {
                    Icon(Icons.Default.FavoriteBorder, contentDescription = "Favorite", tint = Color.Black)
                }
                IconButton(onClick = {  }) {
                    Icon(Icons.Default.MoreVert, contentDescription = "More", tint = Color.Black)
                }
            }


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.Top
            ) {
                Image(
                    painter = painterResource(id = R.drawable.doc_profile),
                    contentDescription = "Profile",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(RoundedCornerShape(16.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Spacer(modifier = Modifier.height(10.dp))
                    Text(profile.name, fontSize = 18.sp, fontWeight = FontWeight.Bold,color = Color.Black)
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(profile.specialty, fontSize = 14.sp, color = Color.Black)
                    Text(profile.hospital, fontSize = 14.sp, color = Color.Black)
                }
            }

            Spacer(modifier = Modifier.height(50.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {

                StatItem(iconRes = R.drawable.patients, value = profile.patients, label = "Patients")
                StatItem(iconRes = R.drawable.growth, value = profile.experience, label = "Years exper.")
                StatItem(iconRes = R.drawable.rating, value = profile.rating, label = "Rating")
                StatItem(iconRes = R.drawable.reviews, value = profile.reviewsCount, label = "Reviews")
            }

            Spacer(modifier = Modifier.height(24.dp))


            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("About me", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = profile.about,
                    fontSize = 14.sp,
                    color = Color.Black,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "View more",
                    fontSize = 14.sp,
                    color = Color(0xFF0D6EFD),
                    modifier = Modifier.clickable {  }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))


            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text("Working Time", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(profile.workingTime, fontSize = 14.sp, color = Color.Black)
            }

            Spacer(modifier = Modifier.height(24.dp))


            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Reviews", fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f),color = Color.Black)
                Text(
                    text = "See All",
                    fontSize = 14.sp,
                    color = Color(0xFF0D6EFD),
                    modifier = Modifier.clickable {
                        navController.navigate("reviews")
                    }
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            if (profile.reviews.isNotEmpty()) {
                ReviewItem(review = profile.reviews.first(), modifier = Modifier.padding(horizontal = 16.dp))
            }
        }
    }
}

@Composable
fun StatItem(icon: Painter, value: String, label: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(icon, contentDescription = label, tint = Color(0xFF0D6EFD), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.height(25.dp))
        Text(value, fontSize = 14.sp, fontWeight = FontWeight.Bold,color = Color(0xFF0D6EFD))
        Text(label, fontSize = 12.sp, color = Color.Black)
    }
}

@Composable
fun ReviewItem(review: Review, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = review.avatarRes ?: R.drawable.charolette),
                contentDescription = "Avatar",
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
            )
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = review.name,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
            Spacer(modifier = Modifier.weight(1f))
            RatingBubbleNumeric(rating = review.rating)
            Spacer(modifier = Modifier.width(8.dp))
            IconButton(onClick = { }) {
                Icon(Icons.Default.MoreVert, contentDescription = "More")
            }
        }
        Spacer(modifier = Modifier.height(2.dp))
        Row(modifier = Modifier.padding(start = 8.dp)) {
            Text(
                text = review.text,
                fontSize = 14.sp,
                color = Color.Black,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis)
        }
    }
}