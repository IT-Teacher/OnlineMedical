package uz.itteacher.onlinemedical

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.database.FirebaseDatabase

@Composable
fun LoginScreen(onBack: () -> Unit = {}, onLoginSuccess: () -> Unit = {}) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailValid by remember { mutableStateOf<Boolean?>(null) }
    var passwordValid by remember { mutableStateOf<Boolean?>(null) }

    var passwordVisible by remember { mutableStateOf(false) }

    val db = FirebaseDatabase.getInstance()
    val usersRef = db.getReference("users")

    // 📧 Email tekshirish
    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            usersRef.get().addOnSuccessListener { snap ->
                val users = snap.children
                var match = false
                for (user in users) {
                    val userEmail = user.child("email").value?.toString() ?: ""
                    if (userEmail.equals(email, ignoreCase = true)) {
                        match = true
                        break
                    }
                }
                emailValid = match
            }
        } else emailValid = null
    }

    fun checkPassword() {
        if (emailValid == true && password.isNotEmpty()) {
            usersRef.get().addOnSuccessListener { snap ->
                for (user in snap.children) {
                    val userEmail = user.child("email").value?.toString() ?: ""
                    val userPassword = user.child("password").value?.toString() ?: ""
                    if (userEmail.equals(email, ignoreCase = true)) {
                        passwordValid = (password == userPassword)
                        break
                    }
                }
            }
        } else passwordValid = null
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { onBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        }

        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Logo",
            modifier = Modifier
                .size(120.dp)
                .padding(top = 10.dp)
        )

        Text(
            text = "Login to your account",
            fontSize = 24.sp,
            color = Color.Black
        )

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Email,
                    contentDescription = "Email",
                    tint = if (email.isNotEmpty()) Color.Black else Color.Gray
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = when (emailValid) {
                    true -> Color.Green
                    false -> Color.Red
                    else -> Color.Gray
                },
                unfocusedBorderColor = when (emailValid) {
                    true -> Color.Green
                    false -> Color.Red
                    else -> Color.Gray
                }
            )
        )

        OutlinedTextField(
            value = password,
            onValueChange = {
                password = it
                checkPassword()
            },
            label = { Text("Password") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Lock,
                    contentDescription = "Password Icon",
                    tint = Color.Gray
                )
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility
                        else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = when (passwordValid) {
                    true -> Color.Green
                    false -> Color.Red
                    else -> Color.Gray
                },
                unfocusedBorderColor = when (passwordValid) {
                    true -> Color.Green
                    false -> Color.Red
                    else -> Color.Gray
                }
            )
        )

        Button(
            onClick = {
                if (emailValid == true && passwordValid == true) {
                    onLoginSuccess()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
        ) {
            Text(text = "Sign in", color = Color.White, fontSize = 18.sp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Don't have an account? ",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Sign up",
                color = Color(0xFF007BFF),
                fontSize = 14.sp,
                modifier = Modifier.clickable { /* TODO */ }
            )
        }
    }
}
