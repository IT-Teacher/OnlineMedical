package com.example.appointment

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class ConsultationPackage(
    val id: String,
    val name: String,
    val description: String,
    val basePrice: Int,
    val iconRes: Int
) {
    fun getPriceForDuration(duration: String): String {
        val minutes = duration.replace(" minutes", "").toIntOrNull() ?: 30
        val multiplier = minutes / 30.0
        val calculatedPrice = (basePrice * multiplier).toInt()
        return "$"+calculatedPrice
    }
}

@Composable
fun SelectPackageScreen(navController: NavController,viewModel: BookingViewModel) {
    val bookingData by viewModel.bookingData.collectAsState()
    var selectedDuration by remember { mutableStateOf(bookingData.duration.takeIf { it.isNotEmpty() } ?: "30 minutes") }
    var selectedPackage by remember { mutableStateOf(bookingData.packageType.takeIf { it.isNotEmpty() }?.let {
        when(it) {
            "Messaging" -> "messaging"
            "Voice Call" -> "voice"
            "Video Call" -> "video"
            else -> null
        }
    }) }
    var durationExpanded by remember { mutableStateOf(false) }


    val durations = listOf("30 minutes", "45 minutes", "60 minutes")

    val packages = listOf(
        ConsultationPackage(
            id = "messaging",
            name = "Messaging",
            description = "Chat messages with doctor",
            basePrice = 20,
            iconRes = R.drawable.reviews
        ),
        ConsultationPackage(
            id = "voice",
            name = "Voice Call",
            description = "Voice call with doctor",
            basePrice = 40,
            iconRes = R.drawable.voice_call
        ),
        ConsultationPackage(
            id = "video",
            name = "Video Call",
            description = "Video call with doctor",
            basePrice = 60,
            iconRes = R.drawable.video_call
        )
    )

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    if (selectedPackage != null) {
                        val pkg = packages.first { it.id == selectedPackage }
                        val minutes = selectedDuration.replace(" minutes", "").toIntOrNull() ?: 30
                        val multiplier = minutes / 30.0
                        val price = (pkg.basePrice * multiplier).toInt()

                        viewModel.updatePackage(pkg.name, selectedDuration, price)
                        navController.navigate("patient_details")
                    }
                },
                enabled = selectedPackage != null,
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
                Text(
                    "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
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
                    text = "Select Package",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp),
                    color = Color.Black
                )
            }


            Text(
                text = "Select Duration",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color.Black
            )


            Box {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { durationExpanded = !durationExpanded },
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFF9FAFB),
                    border = BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFEFF6FF),
                            modifier = Modifier.size(32.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Image(
                                    painter = painterResource(id = R.drawable.clock),
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Text(
                            text = selectedDuration,
                            fontSize = 16.sp,
                            color = Color.Black,
                            modifier = Modifier.weight(1f)
                        )

                        Icon(
                            imageVector = Icons.Default.KeyboardArrowDown,
                            contentDescription = "Dropdown",
                            tint = Color.Black,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }

                DropdownMenu(
                    expanded = durationExpanded,
                    onDismissRequest = { durationExpanded = false },
                    modifier = Modifier
                        .fillMaxWidth(0.92f)
                        .background(Color.White, RoundedCornerShape(12.dp))
                ) {
                    durations.forEach { duration ->
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = duration,
                                    color = Color.Black,
                                    fontSize = 16.sp
                                )
                            },
                            onClick = {
                                selectedDuration = duration
                                durationExpanded = false
                            },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Select Package",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(bottom = 12.dp),
                color = Color.Black
            )

            packages.forEach { pkg ->
                PackageCard(
                    package_ = pkg,
                    isSelected = selectedPackage == pkg.id,
                    onSelect = { selectedPackage = pkg.id },
                    selectedDuration = selectedDuration
                )
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

@Composable
fun PackageCard(
    package_: ConsultationPackage,
    isSelected: Boolean,
    onSelect: () -> Unit,
    selectedDuration: String
) {
    val displayPrice = package_.getPriceForDuration(selectedDuration)
    val displayDuration = selectedDuration.replace(" minutes", " mins")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFFF9FAFB)
        ),
        border = if (isSelected) BorderStroke(2.dp, Color(0xFF2563EB)) else null
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFFEFF6FF),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Image(
                        painter = painterResource(id = package_.iconRes),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = package_.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = package_.description,
                    fontSize = 13.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = displayPrice,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF2563EB)
                )
                Text(
                    text = displayDuration,
                    fontSize = 12.sp,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            RadioButton(
                selected = isSelected,
                onClick = { onSelect() },
                colors = RadioButtonDefaults.colors(
                    selectedColor = Color(0xFF2563EB),
                    unselectedColor = Color(0xFFD1D5DB)
                )
            )
        }
    }
}