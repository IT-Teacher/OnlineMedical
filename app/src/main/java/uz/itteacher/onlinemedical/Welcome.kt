package uz.itteacher.onlinemedical

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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
    var showHighlight by remember { mutableStateOf(false) }
    var showOnboarding by remember { mutableStateOf(false) }
    var showRegister by remember { mutableStateOf(false) }

    var highlightData by remember { mutableStateOf(WelcomeData()) }
    var onboardingList by remember { mutableStateOf<List<WelcomeData>>(emptyList()) }

    LaunchedEffect(Unit) {
        delay(3000)
        showSplash = false
        val db = FirebaseDatabase.getInstance()
        val welcomeRef = db.getReference("welcome")

        welcomeRef.get().addOnSuccessListener { snap ->
            val img4 = snap.child("4").child("imageUrl").value?.toString() ?: ""
            val t4 = snap.child("4").child("title").value?.toString() ?: ""
            highlightData = WelcomeData(img4, t4)

            val temp = mutableListOf<WelcomeData>()
            for (i in 1..3) {
                val img = snap.child(i.toString()).child("imageUrl").value?.toString() ?: ""
                val tt = snap.child(i.toString()).child("title").value?.toString() ?: ""
                if (img.isNotEmpty() || tt.isNotEmpty()) temp.add(WelcomeData(img, tt))
            }
            onboardingList = temp
            showHighlight = true
        }.addOnFailureListener {
            showHighlight = true
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        when {
            showRegister -> {
                RegisterScreen(onBack = { showRegister = false })
            }

            showSplash -> SplashScreen()

            showHighlight -> HighlightScreen(data = highlightData, onShown = {
                showHighlight = false
                showOnboarding = true
            })

            showOnboarding -> {
                if (onboardingList.isNotEmpty()) {
                    OnboardingSteps(onboardingList = onboardingList, onFinish = {
                        showOnboarding = false
                        showRegister = true
                    })
                } else {
                    Text("No onboarding data", color = Color.Gray)
                }
            }
        }
    }
}

@Composable
fun SplashScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
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
        Image(
            painter = painterResource(id = R.drawable.loading),
            contentDescription = "Loading",
            modifier = Modifier.size(60.dp)
        )
    }
}

data class WelcomeData(
    val imageUrl: String = "",
    val title: String = ""
)

@Composable
fun HighlightScreen(data: WelcomeData, onShown: () -> Unit) {
    LaunchedEffect(data) {
        delay(3000)
        onShown()
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (data.imageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = data.imageUrl),
                contentDescription = "Highlight Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
        }
        Spacer(modifier = Modifier.height(20.dp))
        Text(
            text = "Welcome to Medica! 👋",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007BFF),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = if (data.title.isNotEmpty()) data.title else "",
            fontSize = 16.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun OnboardingSteps(onboardingList: List<WelcomeData>, onFinish: () -> Unit) {
    var index by remember { mutableStateOf(0) }
    val item = onboardingList.getOrNull(index)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 20.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        if (item != null && item.imageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = item.imageUrl),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = item?.title ?: "",
            fontSize = 18.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(40.dp))
        Button(
            onClick = {
                if (index < onboardingList.lastIndex) index++
                else onFinish()
            },
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
        ) {
            Text(
                text = if (index < onboardingList.lastIndex) "Next" else "Get Started",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}




