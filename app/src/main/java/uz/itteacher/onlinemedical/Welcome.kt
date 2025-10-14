package uz.itteacher.onlinemedical

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay

@SuppressLint("SuspiciousIndentation")
@Composable
fun WelcomeScreen() {

    var showSplash by remember { mutableStateOf(true) }

    // Firebase’dan o‘qiladigan ma’lumotlar
    var imageUrl by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }

    // 3 sek. Splash, keyin Firebase’dan yuklash
    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false

        // Firebase’dan welcome/4 ni olish
        val ref = FirebaseDatabase.getInstance().getReference("welcome/4")
        ref.get().addOnSuccessListener { snapshot ->
            imageUrl = snapshot.child("imageUrl").value.toString()
            title = snapshot.child("title").value.toString()
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        if (showSplash) {
            SplashContent()
        } else {
            FirebaseWelcomeScreen(imageUrl, title)
        }
    }
}

@Composable
fun SplashContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // O‘rtadagi logo
        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Medica Logo",
            modifier = Modifier.size(100.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = "Medica",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(60.dp))
        // Loading rasmi
        Image(
            painter = painterResource(id = R.drawable.loading),
            contentDescription = "Loading",
            modifier = Modifier.size(60.dp)
        )
    }
}

@Composable
fun FirebaseWelcomeScreen(imageUrl: String, title: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (imageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(imageUrl),
                contentDescription = "Welcome Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = "Welcome to Medica! 👋",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007BFF),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = if (title.isNotEmpty()) title else "Loading...",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
    }
}
