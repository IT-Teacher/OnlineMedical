package uz.itteacher.onlinemedical

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(
    onBack: () -> Unit = {} // 🔹 qo‘shildi — WelcomeScreen bilan ishlashi uchun
) {
    var imageUrl by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }
    var showLogin by remember { mutableStateOf(false) } // 🔹 LoginScreen ko‘rsatish uchun flag

    // 🔹 Firebase’dan welcome/5 ma’lumotni olish
    LaunchedEffect(Unit) {
        val db = FirebaseDatabase.getInstance()
        db.getReference("welcome").child("5").get().addOnSuccessListener {
            imageUrl = it.child("imageUrl").value?.toString() ?: ""
            title = it.child("title").value?.toString() ?: ""
        }
    }

    if (showLogin) {
        // 🔹 LoginScreen’ni ko‘rsatamiz
        LoginScreen(
            onBack = { showLogin = false }, // Back bosilsa RegisterScreen’ga qaytadi
            onLoginSuccess = {
                // bu joyda keyingi ekran chaqiriladi
            }
        )
    } else {
        // 🔹 RegisterScreen UI
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔙 Orqaga icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { onBack() }) { // ✅ endi WelcomeScreen’ga qaytadi
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                    )
                }
            }

            // 🖼 Rasm (Firebase’dan)
            if (imageUrl.isNotEmpty()) {
                Image(
                    painter = rememberAsyncImagePainter(model = imageUrl),
                    contentDescription = "Welcome Illustration",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(300.dp)
                )
            }

            Text(
                text = if (title.isNotEmpty()) title else "Let's you in",
                color = Color.Black,
                fontSize = 32.sp
            )

            // 🔵 LoginScreen ochish tugmasi
            Button(
                onClick = { showLogin = true }, // shu joyda LoginScreen ochiladi
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
            ) {
                Text(
                    text = "Sign in with password",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            // 🔹 Sign up text
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Text(text = "Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
                Text(
                    text = "Sign up",
                    color = Color(0xFF007BFF),
                    fontSize = 14.sp,
                    modifier = Modifier.clickable { /* boshqa ekran */ }
                )
            }
        }
    }
}
