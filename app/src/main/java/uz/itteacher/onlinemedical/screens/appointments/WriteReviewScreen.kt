package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import uz.itteacher.onlinemedical.R

@Composable
fun WriteReviewScreen(
    onBack: () -> Unit,
    doctorName: String,
    doctorId: String,
    userId: String = "uid_sample",
    onNavigateToAppointments: () -> Unit
) {
    var reviewText by remember { mutableStateOf("") }
    var recommend by remember { mutableStateOf<Boolean?>(null) }
    var rating by remember { mutableStateOf(0) }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val database = Firebase.database
    val reviewsRef = database.getReference("reviews")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            Text("Write a Review", fontWeight = FontWeight.Bold, fontSize = 20.sp)
        }

        Spacer(Modifier.height(24.dp))

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground),
                contentDescription = "Doctor",
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.height(16.dp))
            Text(
                text = "How was your experience\nwith $doctorName?",
                textAlign = TextAlign.Center,
                fontSize = 18.sp,
                color = Color.Black
            )
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(5) { index ->
                IconButton(
                    onClick = { rating = index + 1 },
                    modifier = Modifier.size(40.dp)
                ) {
                    Icon(
                        imageVector = if (index < rating) Icons.Filled.Star else Icons.Outlined.StarBorder,
                        contentDescription = null,
                        tint = Color(0xFF3366FF),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(32.dp))
        HorizontalDivider()
        Spacer(Modifier.height(24.dp))

        Text(
            "Write Your Review",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = reviewText,
            onValueChange = { reviewText = it },
            placeholder = { Text("Your review here...") },
            modifier = Modifier
                .fillMaxWidth()
                .height(120.dp)
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF5F5F5),
                unfocusedContainerColor = Color(0xFFF5F5F5),
                disabledContainerColor = Color(0xFFF5F5F5)
            )
        )

        Spacer(Modifier.height(32.dp))

        Text(
            "Would you recommend $doctorName\nto your friends?",
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            fontSize = 16.sp
        )
        Spacer(Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            RadioButton(selected = recommend == true, onClick = { recommend = true })
            Text("Yes", modifier = Modifier.padding(end = 32.dp, top = 12.dp))
            RadioButton(selected = recommend == false, onClick = { recommend = false })
            Text("No", modifier = Modifier.padding(top = 12.dp))
        }

        Spacer(Modifier.height(40.dp))

        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            OutlinedButton(
                onClick = onBack,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(50.dp)
            ) {
                Text("Cancel")
            }

            Button(
                onClick = {
                    val reviewId = reviewsRef.push().key ?: return@Button
                    val reviewData = mapOf(
                        "userId" to userId,
                        "doctorId" to doctorId,
                        "doctorName" to doctorName,
                        "rating" to rating,
                        "reviewText" to reviewText,
                        "recommend" to recommend,
                        "timestamp" to System.currentTimeMillis()
                    )
                    reviewsRef.child(reviewId).setValue(reviewData)
                        .addOnSuccessListener { showSuccessDialog = true }
                        .addOnFailureListener { /* Handle error */ }

                },
                enabled = reviewText.isNotBlank() && recommend != null && rating > 0,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3366FF))
            ) {
                Text("Submit", color = Color.White)
            }
        }

        Spacer(Modifier.height(32.dp))
    }

    if (showSuccessDialog) {
        ReviewSuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                onNavigateToAppointments()
            }
        )
    }
}