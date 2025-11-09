package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itteacher.onlinemedical.screens.appointments.components.BackAndTitle
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

private val blueColor = Color(0xFF3366FF)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun DateHourScreen(
    onBack: () -> Unit,
    onNavigateToAppointments: () -> Unit
) {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var currentMonth by remember { mutableStateOf(selectedDate.withDayOfMonth(1)) }
    var selectedHour by remember { mutableStateOf("03.00 PM") }
    var showSuccessDialog by remember { mutableStateOf(false) }

    val monthYear = currentMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.ENGLISH))
    val firstDayOfMonth = currentMonth.dayOfWeek.value % 7
    val daysInMonth = currentMonth.lengthOfMonth()

    val calendarDays = buildList {
        repeat(firstDayOfMonth) { add(null) }
        for (day in 1..daysInMonth) {
            add(LocalDate.of(currentMonth.year, currentMonth.month, day))
        }
        while (size % 7 != 0) { add(null) }
    }

    val hours = listOf(
        "09.00 AM", "09.30 AM", "10.00 AM",
        "10.30 AM", "11.00 AM", "11.30 AM",
        "03.00 PM", "03.30 PM", "04.00 PM",
        "04.30 PM", "05.00 PM", "05.30 PM"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        BackAndTitle(title = "Reschedule Appointment", onBack = onBack)
        Spacer(modifier = Modifier.height(16.dp))

        Text("Select Date", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = {
                        currentMonth = currentMonth.minusMonths(1)
                    }) {
                        Icon(Icons.Default.KeyboardArrowLeft, contentDescription = "Prev")
                    }
                    Text(
                        text = monthYear,
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                    IconButton(onClick = {
                        currentMonth = currentMonth.plusMonths(1)
                    }) {
                        Icon(Icons.Default.KeyboardArrowRight, contentDescription = "Next")
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat").forEach {
                        Text(
                            text = it,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.Center
                        )
                    }
                }

                calendarDays.chunked(7).forEach { week ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        week.forEach { date ->
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(4.dp)
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (date == selectedDate) blueColor else Color.Transparent
                                    )
                                    .clickable(enabled = date != null) {
                                        date?.let { selectedDate = it }
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                date?.let {
                                    Text(
                                        text = it.dayOfMonth.toString(),
                                        color = if (date == selectedDate) Color.White else Color.Black,
                                        fontWeight = if (date == selectedDate) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        Text("Select Hour", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(Modifier.height(12.dp))

        Column {
            hours.chunked(3).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    row.forEach { hour ->
                        val isSelected = hour == selectedHour
                        Box(
                            modifier = Modifier
                                .padding(end = 8.dp, bottom = 8.dp)
                                .border(1.dp, blueColor, RoundedCornerShape(8.dp))
                                .background(if (isSelected) blueColor else Color.Transparent)
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                                .clickable { selectedHour = hour },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = hour,
                                color = if (isSelected) Color.White else blueColor,
                                fontWeight = FontWeight.Medium,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { showSuccessDialog = true },
            modifier = Modifier.fillMaxWidth().height(50.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = blueColor)
        ) {
            Text("Submit", fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }

    if (showSuccessDialog) {
        SuccessDialog(
            onDismiss = {
                showSuccessDialog = false
                onNavigateToAppointments()
            },
            onViewAppointment = {
                showSuccessDialog = false
                onNavigateToAppointments()
            }
        )
    }
}