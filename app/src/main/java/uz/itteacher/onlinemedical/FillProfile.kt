package uz.itteacher.onlinemedical

import android.app.DatePickerDialog
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FillProfileScreen(
    onBack: () -> Unit = {},
    emailFromSignup: String = "",
    onNavigateToHome: () -> Unit = {} // 👈 HomeScreen ga o‘tish uchun callback
) {
    var fullName by remember { mutableStateOf("") }
    var nickname by remember { mutableStateOf("") }
    var dateOfBirth by remember { mutableStateOf("") }
    var email by remember { mutableStateOf(emailFromSignup) }
    var gender by remember { mutableStateOf("") }

    var fullNameError by remember { mutableStateOf(false) }
    var nicknameError by remember { mutableStateOf(false) }
    var dateError by remember { mutableStateOf(false) }
    var emailError by remember { mutableStateOf(false) }
    var genderError by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val db = FirebaseDatabase.getInstance().getReference("users")
    val scope = rememberCoroutineScope()

    val calendar = Calendar.getInstance()
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    fun showDatePicker() {
        DatePickerDialog(context, { _, y, m, d ->
            dateOfBirth = "$d/${m + 1}/$y"
        }, year, month, day).show()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "Fill Your Profile",
                fontSize = 20.sp,
                color = Color.Black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color(0xFFF2F2F2))
                .clickable { /* image picker */ },
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.picon),
                contentDescription = "Profile Picture",
                modifier = Modifier.size(260.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it; fullNameError = false },
            label = { Text("Full Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = nickname,
            onValueChange = { nickname = it; nicknameError = false },
            label = { Text("Nickname") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))


        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { },
            label = { Text("Date of Birth") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                Icon(
                    Icons.Default.CalendarToday,
                    contentDescription = "Calendar",
                    modifier = Modifier.clickable { showDatePicker() }
                )
            },
            readOnly = true,
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { email = it; emailError = false },
            label = { Text("Email") },
            leadingIcon = { Icon(Icons.Default.Email, contentDescription = null) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            shape = RoundedCornerShape(16.dp)
        )

        Spacer(modifier = Modifier.height(12.dp))


        var expanded by remember { mutableStateOf(false) }

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = {},
                label = { Text("Gender") },
                modifier = Modifier
                    .menuAnchor()
                    .fillMaxWidth(),
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                shape = RoundedCornerShape(16.dp)
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text("Male") },
                    onClick = {
                        gender = "Male"
                        expanded = false
                    }
                )
                DropdownMenuItem(
                    text = { Text("Female") },
                    onClick = {
                        gender = "Female"
                        expanded = false
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        val allFilled = fullName.isNotEmpty() && nickname.isNotEmpty() &&
                dateOfBirth.isNotEmpty() && email.isNotEmpty() && gender.isNotEmpty()

        Button(
            onClick = {
                if (allFilled) {
                    val newProfile = mapOf(
                        "fullName" to fullName,
                        "nickname" to nickname,
                        "dateOfBirth" to dateOfBirth,
                        "email" to email,
                        "gender" to gender
                    )
                    db.push().child("profile").setValue(newProfile)

                    showDialog = true

                    // 3 sek kutib HomeScreen ga o‘tish
                    scope.launch {
                        delay(3000)
                        showDialog = false
                        onNavigateToHome()
                    }

                } else {
                    fullNameError = fullName.isEmpty()
                    nicknameError = nickname.isEmpty()
                    dateError = dateOfBirth.isEmpty()
                    emailError = email.isEmpty()
                    genderError = gender.isEmpty()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp),
            shape = RoundedCornerShape(30.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (allFilled) Color(0xFF007BFF) else Color.LightGray
            )
        ) {
            Text("Continue", fontSize = 18.sp, color = Color.White)
        }
    }

    if (showDialog) {
        Dialog(onDismissRequest = { }) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color.White)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Image(
                        painter = painterResource(id = R.drawable.picon),
                        contentDescription = null,
                        modifier = Modifier.size(80.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Congratulations!", color = Color(0xFF007BFF), fontSize = 20.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Your account is ready to use.\nYou will be redirected to the Home page in a few seconds...",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                    Image(
                        painter = painterResource(id = R.drawable.loading),
                        contentDescription = "Loading",
                        modifier = Modifier.size(50.dp)
                    )
                }
            }
        }
    }
}
