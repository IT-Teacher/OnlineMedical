package com.example.appointment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.util.Calendar

@Composable
fun BookAppointmentScreen(
    navController: NavController,
    viewModel: BookingViewModel
) {
    val bookingData by viewModel.bookingData.collectAsState()

    var selectedDate by remember { mutableStateOf<Int?>(null) }
    var selectedMonth by remember { mutableStateOf(11) }
    var selectedYear by remember { mutableStateOf(2022) }
    var selectedTime by remember { mutableStateOf(bookingData.selectedTime.takeIf { it.isNotEmpty() }) }

    LaunchedEffect(bookingData.selectedDate) {
        if (bookingData.selectedDate.isNotEmpty()) {

            val parts = bookingData.selectedDate.split(" ")
            if (parts.size == 3) {
                val monthNames = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")
                val monthIndex = monthNames.indexOf(parts[0])
                if (monthIndex != -1) {
                    selectedMonth = monthIndex
                    selectedDate = parts[1].replace(",", "").toIntOrNull()
                    selectedYear = parts[2].toIntOrNull() ?: 2025
                }
            }
        }
    }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    if (selectedDate != null && selectedTime != null) {
                        val monthNames = listOf(
                            "Jan", "Feb", "Mar", "Apr", "May", "Jun",
                            "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
                        )
                        val dateString = "${monthNames[selectedMonth]} $selectedDate, $selectedYear"
                        viewModel.updateDateTime(dateString, selectedTime!!)
                        navController.navigate("select_package")
                    }
                },
                enabled = selectedDate != null && selectedTime != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB),
                    disabledContainerColor = Color(0xFF93C5FD)
                )
            ) {
                Text("Next", fontSize = 16.sp, fontWeight = FontWeight.Bold,color = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState())
        ) {

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 24.dp)
                    .clickable { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Text(
                    text = "Book Appointment",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp),
                    color = Color.Black

                )
            }

            Text(
                text = "Select Date",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color.Black
            )

            CalendarView(
                selectedDate = selectedDate,
                onDateSelected = { selectedDate = it }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select Hour",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color.Black
            )

            TimeSlotGrid(
                selectedTime = selectedTime,
                onTimeSelected = { selectedTime = it }
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun CalendarView(
    selectedDate: Int?,
    onDateSelected: (Int) -> Unit
) {
    var currentMonth by remember { mutableStateOf(11) }
    var currentYear by remember { mutableStateOf(2022) }

    val monthNames = listOf(
        "January", "February", "March", "April", "May", "June",
        "July", "August", "September", "October", "November", "December"
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFEFF6FF)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "${monthNames[currentMonth]} $currentYear",
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Row {
                    IconButton(onClick = {
                        if (currentMonth == 0) {
                            currentMonth = 11
                            currentYear--
                        } else {
                            currentMonth--
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_back),
                            contentDescription = "Previous",
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    IconButton(onClick = {
                        if (currentMonth == 11) {
                            currentMonth = 0
                            currentYear++
                        } else {
                            currentMonth++
                        }
                    }) {
                        Image(
                            painter = painterResource(id = R.drawable.arrow_fw),
                            contentDescription = "Next",
                            modifier = Modifier.size(27.dp)
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                listOf("Mo", "Tu", "We", "Th", "Fr", "Sa", "Su").forEach { day ->
                    Text(
                        text = day,
                        fontSize = 12.sp,
                        color = Color.Black,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val daysInMonth = when (currentMonth) {
                1 -> if (currentYear % 4 == 0 && (currentYear % 100 != 0 || currentYear % 400 == 0)) 29 else 28
                3, 5, 8, 10 -> 30
                else -> 31
            }

            val calendar = Calendar.getInstance()
            calendar.set(currentYear, currentMonth, 1)
            val firstDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val startDay = if (firstDayOfWeek == 1) 6 else firstDayOfWeek - 2

            Column {
                var dayCounter = 1
                var weekCount = 0

                while (dayCounter <= daysInMonth) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        for (dayOfWeek in 0..6) {
                            val shouldShowDay = if (weekCount == 0) {
                                dayOfWeek >= startDay && dayCounter <= daysInMonth
                            } else {
                                dayCounter <= daysInMonth
                            }

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .padding(2.dp)
                            ) {
                                if (shouldShowDay) {
                                    val currentDay = dayCounter
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(
                                                if (selectedDate == currentDay)
                                                    Color(0xFF2563EB)
                                                else
                                                    Color.Transparent
                                            )
                                            .clickable { onDateSelected(currentDay) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = currentDay.toString(),
                                            fontSize = 14.sp,
                                            color = if (selectedDate == currentDay)
                                                Color.White
                                            else
                                                Color.Black,
                                            fontWeight = if (selectedDate == currentDay)
                                                FontWeight.SemiBold
                                            else
                                                FontWeight.Normal
                                        )
                                    }
                                    dayCounter++
                                }
                            }
                        }
                    }
                    weekCount++
                }
            }
        }
    }
}

@Composable
fun TimeSlotGrid(
    selectedTime: String?,
    onTimeSelected: (String) -> Unit
) {
    val timeSlots = listOf(
        "09.00 AM", "09.30 AM", "10.00 AM",
        "10.30 PM", "11.00 PM", "11.30 PM",
        "15.00 PM", "15.30 PM", "16.00 PM",
        "16.30 PM", "17.00 PM", "17.30 PM"
    )

    Column {
        for (row in timeSlots.chunked(3)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                row.forEach { time ->
                    Button(
                        onClick = { onTimeSelected(time) },
                        modifier = Modifier
                            .weight(1f)
                            .height(40.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (selectedTime == time)
                                Color(0xFF2563EB)
                            else
                                Color.White,
                            contentColor = if (selectedTime == time)
                                Color.White
                            else
                                Color(0xFF2563EB)
                        ),
                        border = BorderStroke(2.dp, Color(0xFF2563EB)),
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = time,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}