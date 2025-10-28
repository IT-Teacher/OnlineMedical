package uz.itteacher.onlinemedical

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import coil.compose.rememberAsyncImagePainter
import com.google.firebase.database.FirebaseDatabase

@Composable
fun RegisterScreen(onBack: () -> Unit = {}) {
    var currentScreen by remember { mutableStateOf("register") }

    when (currentScreen) {
        "register" -> RegisterContent(onSignUpClick = { currentScreen = "signup" })
        "signup" -> SignUpScreen(
            onBack = { currentScreen = "register" },
            onLoginClick = { currentScreen = "login" },
            onFillProfile = { currentScreen = "fillProfile" }
        )
        "login" -> LoginScreen(
            onBack = { currentScreen = "signup" },
            onSignUpClick = { currentScreen = "signup" }
        )
        "fillProfile" -> FillProfileScreen(
            onBack = { currentScreen = "signup" }
        )
    }
}

@Composable
fun RegisterContent(onSignUpClick: () -> Unit) {
    var imageUrl by remember { mutableStateOf("") }
    var title by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        val db = FirebaseDatabase.getInstance()
        db.getReference("welcome").child("5").get().addOnSuccessListener {
            imageUrl = it.child("imageUrl").value?.toString() ?: ""
            title = it.child("title").value?.toString() ?: ""
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

        }

        if (imageUrl.isNotEmpty()) {
            Image(
                painter = rememberAsyncImagePainter(model = imageUrl),
                contentDescription = "Welcome Illustration",
                modifier = Modifier.fillMaxWidth().height(300.dp)
            )
        }

        Text(
            text = if (title.isNotEmpty()) title else "Let's you in",
            color = Color.Black,
            fontSize = 32.sp
        )

        Button(
            onClick = onSignUpClick,
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
        ) {
            Text(text = "Sign up with password", color = Color.White, fontSize = 18.sp)
        }
    }
}

@Composable
fun SignUpScreen(
    onBack: () -> Unit = {},
    onLoginClick: () -> Unit = {},
    onFillProfile: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var emailValid by remember { mutableStateOf<Boolean?>(null) }
    var passwordValid by remember { mutableStateOf<Boolean?>(null) }
    var passwordVisible by remember { mutableStateOf(false) }

    val db = FirebaseDatabase.getInstance()
    val usersRef = db.getReference("users")

    LaunchedEffect(email) {
        if (email.isNotEmpty()) {
            usersRef.get().addOnSuccessListener { snap ->
                var exists = false
                for (user in snap.children) {
                    val userEmail = user.child("email").value?.toString() ?: ""
                    if (userEmail.equals(email, ignoreCase = true)) {
                        exists = true
                        break
                    }
                }
                emailValid = !exists
            }
        } else emailValid = null
    }

    LaunchedEffect(password) {
        passwordValid = password.isNotEmpty()
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp).padding(top = 10.dp)
        )

        Text(text = "Create New Account", fontSize = 24.sp, color = Color.Black)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth().height(60.dp),
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
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon", tint = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().height(60.dp),
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

        val buttonEnabled = (emailValid == true && passwordValid == true)

        Button(
            onClick = {
                if (buttonEnabled) {
                    val newUser = usersRef.push()
                    newUser.child("email").setValue(email)
                    newUser.child("password").setValue(password)
                        .addOnSuccessListener { onFillProfile() }
                }
            },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (buttonEnabled) Color(0xFF007BFF) else Color.LightGray
            ),
            enabled = buttonEnabled
        ) {
            Text(text = "Sign up", color = Color.White, fontSize = 18.sp)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Already have an account? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "Sign in",
                color = Color(0xFF007BFF),
                fontSize = 14.sp,
                modifier = Modifier.clickable { onLoginClick() }
            )
        }
    }
}

@Composable
fun LoginScreen(
    onBack: () -> Unit = {},
    onLoginSuccess: () -> Unit = {},
    onSignUpClick: () -> Unit = {}
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(top = 8.dp),
            verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBack) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
        }

        Image(
            painter = painterResource(id = R.drawable.images),
            contentDescription = "Logo",
            modifier = Modifier.size(120.dp).padding(top = 10.dp)
        )

        Text(text = "Login to your account", fontSize = 24.sp, color = Color.Black)

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = "Email", tint = Color.Gray) },
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Password Icon", tint = Color.Gray) },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                        contentDescription = "Toggle Password"
                    )
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth().height(60.dp),
            shape = RoundedCornerShape(20.dp),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
        )

        Button(
            onClick = { onLoginSuccess() },
            modifier = Modifier.fillMaxWidth().height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
        ) {
            Text(text = "Sign in", color = Color.White, fontSize = 18.sp)
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Text(text = "Don't have an account? ", color = Color.Gray, fontSize = 14.sp)
            Text(
                text = "Sign up",
                color = Color(0xFF007BFF),
                fontSize = 14.sp,
                modifier = Modifier.clickable { onSignUpClick() }
            )
        }
    }
}
