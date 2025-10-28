package uz.itteacher.onlinemedical.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun FillProfileScreen() {
    var fullName by remember { mutableStateOf("") }
    var fullNameError by remember { mutableStateOf(false) }

    var age by remember { mutableStateOf("") }
    var ageError by remember { mutableStateOf(false) }

    var gender by remember { mutableStateOf("") }
    var genderError by remember { mutableStateOf(false) }

    var expanded by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Fill Your Profile",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF007BFF),
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Full name
        OutlinedTextField(
            value = fullName,
            onValueChange = { fullName = it; fullNameError = false },
            label = { Text("Full Name") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (fullNameError) Color.Red else Color.Gray,
                unfocusedBorderColor = if (fullNameError) Color.Red else Color.Gray
            )
        )

        // Age
        OutlinedTextField(
            value = age,
            onValueChange = { age = it; ageError = false },
            label = { Text("Age") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = if (ageError) Color.Red else Color.Gray,
                unfocusedBorderColor = if (ageError) Color.Red else Color.Gray
            )
        )

        // ✅ Gender (Dropdown menu to‘g‘rilangan)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
                .clickable { expanded = true } // <-- faqat shu joy qo‘shildi
        ) {
            OutlinedTextField(
                value = gender,
                onValueChange = { gender = it; genderError = false },
                label = { Text("Gender") },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                readOnly = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = if (genderError) Color.Red else Color.Gray,
                    unfocusedBorderColor = if (genderError) Color.Red else Color.Gray
                )
            )

            DropdownMenu(
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

        // Continue button
        Button(
            onClick = {
                fullNameError = fullName.isEmpty()
                ageError = age.isEmpty()
                genderError = gender.isEmpty()
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = "Continue",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Text(
            text = "We’ll use this information to personalize your experience",
            fontSize = 12.sp,
            color = Color.Gray,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
    }
}
