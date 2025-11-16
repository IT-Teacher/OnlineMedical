package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uz.itteacher.onlinemedical.R
import uz.itteacher.onlinemedical.model.AppointmentData

@Composable
fun MessagingAppointmentScreen(navController: NavHostController) {
    // SAFEST WAY: Get data from savedStateHandle + fallback
    val savedAppointment by navController.currentBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<AppointmentData?>("appointment", null)
        ?.collectAsState() ?: remember { mutableStateOf<AppointmentData?>(null) }

    val previousAppointment by navController.previousBackStackEntry
        ?.savedStateHandle
        ?.getStateFlow<AppointmentData?>("appointment", null)
        ?.collectAsState() ?: remember { mutableStateOf<AppointmentData?>(null) }

    val data = savedAppointment ?: previousAppointment ?: AppointmentData(
        doctorName = "Dr. Loading...",
        patientName = "Loading...",
        patientGender = "Male",
        patientAge = 0,
        problem = "Please wait..."
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .verticalScroll(rememberScrollState())
    ) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = Color.Black)
            }
            Text(
                text = "My Appointment",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
            IconButton(onClick = { }) {
                Icon(painterResource(id = R.drawable.more), contentDescription = "More", tint = Color.Black)
            }
        }

        // Doctor Card
        Card(
            modifier = Modifier.padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    AsyncImage(
                        model = data.doctorPhotoUrl,
                        contentDescription = "Doctor",
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(80.dp).clip(CircleShape)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(data.doctorName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                        Text(data.doctorJob, color = Color.Gray, fontSize = 14.sp)
                        Text(data.doctorLocation, color = Color.Gray, fontSize = 13.sp)
                    }
                }
            }
        }

        // Scheduled Appointment
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Scheduled Appointment", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))
            Text(data.date, color = Color.Gray, fontSize = 14.sp)
            Text("${data.time} (${data.duration})", color = Color(0xFF007BFF), fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Patient Information — NOW SHOWS REAL DATA
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text("Patient Information", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
            Spacer(modifier = Modifier.height(12.dp))

            PatientInfoRow("Full Name", data.patientName)
            PatientInfoRow("Gender", data.patientGender)
            PatientInfoRow("Age", data.patientAge.toString())
            PatientInfoRow(
                label = "Problem",
                value = data.problem.ifEmpty { "No problem description provided." },
                isLongText = true
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Package
        Text("Your Package", fontWeight = FontWeight.SemiBold, fontSize = 16.sp, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
                .border(1.dp, Color(0xFF007BFF).copy(alpha = 0.3f), RoundedCornerShape(12.dp))
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(modifier = Modifier.size(50.dp).background(Color(0xFF007BFF).copy(0.1f), CircleShape), contentAlignment = Alignment.Center) {
                Icon(painterResource(R.drawable.message), null, tint = Color(0xFF007BFF), modifier = Modifier.size(24.dp))
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(data.packageType, fontWeight = FontWeight.Medium)
                Text("Chat messages with doctor", color = Color.Gray, fontSize = 12.sp)
            }
            Text(data.packagePrice, fontWeight = FontWeight.Bold, color = Color(0xFF007BFF))
            Spacer(modifier = Modifier.width(8.dp))
            Text("(paid)", color = Color.Gray, fontSize = 12.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Message Button
        Button(
            onClick = {
                navController.currentBackStackEntry?.savedStateHandle?.set("appointment", data)
                navController.navigate("chat")
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp).height(56.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF))
        ) {
            Icon(painterResource(R.drawable.message), null, tint = Color.White, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Message (Start at ${data.time.split(" – ")[0]})", color = Color.White, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun PatientInfoRow(label: String, value: String, isLongText: Boolean = false) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(label, color = Color.Gray, fontSize = 13.sp)
        Text(
            text = value,
            fontWeight = if (isLongText) FontWeight.Normal else FontWeight.Medium,
            fontSize = 14.sp,
            color = Color.Black,
            modifier = if (isLongText) Modifier else Modifier.padding(top = 4.dp)
        )
        if (isLongText && value.length > 100) {
            Text("view more", color = Color(0xFF007BFF), fontSize = 12.sp, modifier = Modifier.padding(top = 4.dp))
        }
    }
}