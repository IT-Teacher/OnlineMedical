package uz.itteacher.onlinemedical.screens.appointments

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import uz.itteacher.onlinemedical.R
import uz.itteacher.onlinemedical.model.Appointment
import uz.itteacher.onlinemedical.utils.TimeUtils

@Composable
fun AppointmentDetailScreen(
    appointment: Appointment,
    onBack: () -> Unit,
    onCallEnded: () -> Unit
) {
    var problemExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    // Recompute every minute
    var tick by remember { mutableStateOf(0) }
    LaunchedEffect(Unit) {
        while (true) {
            delay(60_000)
            tick++
        }
    }

    val isCallAvailable = remember(appointment.time, tick) {
        TimeUtils.isWithinCallWindow(appointment.time, appointment.durationMinutes)
    }
    val timeStatus = remember(appointment.time, tick) {
        TimeUtils.getTimeStatus(appointment.time, appointment.durationMinutes)
    }

    val startCall: () -> Unit = {
        if (appointment.canCall() && isCallAvailable) {
            val intent = Intent(Intent.ACTION_CALL).apply {
                data = Uri.parse("tel:${appointment.doctorPhone}")
            }
            context.startActivity(intent)
            onCallEnded()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
            Text(
                text = "My Appointment",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
                modifier = Modifier.weight(1f),
                color = Color.Black
            )
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.more),
                    contentDescription = "More",
                    tint = Color.Black
                )
            }
        }

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = appointment.imageRes),
                    contentDescription = "Doctor",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(60.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = "Dr. ${appointment.name}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    Text("Immunologists", color = Color(0xFF666666), fontSize = 14.sp)
                    Text("Alka Hospital in Seoul, South Korea", color = Color(0xFF666666), fontSize = 14.sp)
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Scheduled Appointment", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))
        Text("Today, 2025", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))
        Text("6:30 - 6:00 PM (30 minutes)", fontSize = 16.sp, color = Color.Black, modifier = Modifier.padding(horizontal = 16.dp))

        Spacer(Modifier.height(24.dp))

        Text("Patient Information", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(12.dp))
        InfoRow("Full Name", "Andrew Ainsley")
        InfoRow("Gender", "Male")
        InfoRow("Age", "27")

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 6.dp)
        ) {
            Text("Problem :", fontSize = 15.sp, color = Color(0xFF666666), modifier = Modifier.width(100.dp))
            Column(modifier = Modifier.weight(1f)) {
                val problem = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor."
                if (problemExpanded) {
                    Text(problem, fontSize = 15.sp, color = Color.Black)
                } else {
                    Text(problem, fontSize = 15.sp, color = Color.Black, maxLines = 2, overflow = TextOverflow.Ellipsis)
                    Text("View more", fontSize = 15.sp, color = Color(0xFF3366FF), modifier = Modifier.clickable { problemExpanded = true })
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Your Package", fontWeight = FontWeight.Bold, fontSize = 18.sp, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(Color(0xFFE3F2FD), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.call),
                        contentDescription = null,
                        tint = Color(0xFF3366FF),
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text("Voice Call", fontWeight = FontWeight.Medium, fontSize = 16.sp)
                    Text("Voice call with doctor", fontSize = 13.sp, color = Color(0xFF666666))
                }
                Text("$40", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = Color(0xFF3366FF))
                Spacer(Modifier.width(4.dp))
                Text("(paid)", color = Color(0xFF666666), fontSize = 12.sp)
            }
        }

        Spacer(Modifier.height(32.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Button(
                onClick = startCall,
                enabled = appointment.canCall() && isCallAvailable,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isCallAvailable) Color(0xFF3366FF) else Color(0xFFCCCCCC),
                    disabledContainerColor = Color(0xFFEEEEEE)
                )
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.call),
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isCallAvailable) "Voice Call (Start at ${appointment.time})"
                    else "Call Not Available",
                    color = Color.White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            }

            if (!isCallAvailable && appointment.canCall()) {
                Spacer(Modifier.height(8.dp))
                Text(
                    text = timeStatus,
                    color = Color(0xFF666666),
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp)
    ) {
        Text("$label :", fontSize = 15.sp, color = Color(0xFF666666), modifier = Modifier.width(100.dp))
        Text(value, fontSize = 15.sp, color = Color.Black, modifier = Modifier.weight(1f))
    }
}