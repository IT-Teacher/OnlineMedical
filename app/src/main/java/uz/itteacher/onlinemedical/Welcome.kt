package uz.itteacher.onlinemedical

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
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
    // boshi: true = Splash (logo)
    var showSplash by remember { mutableStateOf(true) }

    // keyin: true = highlight (welcome/4) ekrani 3s turadi
    var showHighlight by remember { mutableStateOf(false) }

    // onboarding boshlanganmi
    var showOnboarding by remember { mutableStateOf(false) }

    // Firebase ma'lumotlari
    var highlightData by remember { mutableStateOf(WelcomeData()) } // welcome/4
    var onboardingList by remember { mutableStateOf<List<WelcomeData>>(emptyList()) } // 1..3

    // 1) Splash 3s -> yuklab olish va highlightga o'tish
    LaunchedEffect(Unit) {
        delay(3000) // splash 3 s
        showSplash = false

        // Firebase'dan welcome/4 ni o'qiymiz (highlight)
        val db = FirebaseDatabase.getInstance()
        val welcomeRef = db.getReference("welcome")

        welcomeRef.get().addOnSuccessListener { snap ->
            // highlight (4)
            val img4 = snap.child("4").child("imageUrl").value?.toString() ?: ""
            val t4 = snap.child("4").child("title").value?.toString() ?: ""
            highlightData = WelcomeData(img4, t4)

            // onboarding 1..3
            val temp = mutableListOf<WelcomeData>()
            for (i in 1..3) {
                val img = snap.child(i.toString()).child("imageUrl").value?.toString() ?: ""
                val tt = snap.child(i.toString()).child("title").value?.toString() ?: ""
                // agar bo'sh bo'lsa ham qo'shmaymiz, lekin davom etish uchun fallback qo'yish mumkin
                if (img.isNotEmpty() || tt.isNotEmpty()) {
                    temp.add(WelcomeData(img, tt))
                }
            }
            onboardingList = temp

            // highlightni ko'rsat
            showHighlight = true
        }.addOnFailureListener {
            // agar o'qib bo'lmasa — shunchaki onboardingni bo'sh qoldiramiz
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
            showSplash -> {
                // original SplashContent (logo + loading)
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

            showHighlight -> {
                // show highlightData for exactly 3 seconds, then go to onboarding
                HighlightScreen(
                    data = highlightData,
                    onShown = {
                        // bu callback 3s tugagach chaqiriladi
                        showHighlight = false
                        showOnboarding = true
                    }
                )
            }

            showOnboarding -> {
                // onboardingList chiqaradi (1..3). Agar bo'sh bo'lsa fallback matn ko'rsatadi.
                if (onboardingList.isNotEmpty()) {
                    OnboardingSteps(onboardingList = onboardingList, onFinish = {
                        // Get Started bosilganda shu joyga keladi
                        // Bu yerga HomeScreen chaqir yoki MainActivitydagi bosh ekranga o'tish lozim.
                        // Masalan: showOnboarding = false; va boshqa ekran flags
                        // Hozircha shunchaki biror ish bajarmaydi.
                    })
                } else {
                    // agar onboarding bo'sh bo'lsa, shunchaki xabar
                    Text(text = "No onboarding data", color = Color.Gray)
                }
            }
        }
    }
}

data class WelcomeData(
    val imageUrl: String = "",
    val title: String = ""
)

@Composable
private fun HighlightScreen(data: WelcomeData, onShown: () -> Unit) {
    // data ni ko'rsatib 3 soniya kutadi, keyin onShown() chaqiradi
    LaunchedEffect(data) {
        // faqat data o'zgarganda 3s kutilsin
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
        } else {
            // fallback local image agar url bo'lmasa
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = "Highlight fallback",
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
            color = Color(0xFF007BFF), // ko'k rang
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
private fun OnboardingSteps(onboardingList: List<WelcomeData>, onFinish: () -> Unit) {
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
                contentDescription = "Onboarding Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(320.dp)
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.images),
                contentDescription = "fallback image",
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
                if (index < onboardingList.lastIndex) {
                    index++
                } else {
                    onFinish()
                }
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
