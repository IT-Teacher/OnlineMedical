package com.example.appointment

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientDetailsScreen(
    navController: NavController,
    viewModel: BookingViewModel
) {
    val bookingData by viewModel.bookingData.collectAsState()

    var fullName by remember { mutableStateOf(bookingData.patientName) }
    var selectedGender by remember { mutableStateOf<String?>(bookingData.patientGender.takeIf { it.isNotEmpty() }) }
    var selectedAge by remember { mutableStateOf<String?>(bookingData.patientAge.takeIf { it.isNotEmpty() }) }
    var problem by remember { mutableStateOf(bookingData.patientProblem) }

    var genderExpanded by remember { mutableStateOf(false) }
    var ageExpanded by remember { mutableStateOf(false) }

    val genderOptions = listOf("Male", "Female", "Other")
    val ageOptions = (18..100).map { "$it years" }

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    viewModel.updatePatientDetails(
                        fullName,
                        selectedGender ?: "",
                        selectedAge ?: "",
                        problem
                    )
                    navController.navigate("payment_options")
                },
                enabled = fullName.length >= 15 &&
                        problem.length >= 35 &&
                        selectedGender != null &&
                        selectedAge != null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF246BFD),
                    disabledContainerColor = Color(0xFF93C5FD)
                )
            ) {
                Text(
                    text = "Next",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
                    .clickable { navController.popBackStack() }
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
                Text(
                    text = "Patient Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp),
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Full Name",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text(
                            text = "Enter your full name",
                            color = Color.Gray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF7F8F9),
                        focusedContainerColor = Color(0xFFF7F8F9),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF246BFD),
                        unfocusedTextColor = Color(0xFF000000),
                        focusedTextColor = Color(0xFF000000),
                        cursorColor = Color(0xFF246BFD)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Gender",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = genderExpanded,
                    onExpandedChange = { genderExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedGender ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        placeholder = {
                            Text(
                                text = "Select your gender",
                                color = Color.Gray
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF7F8F9),
                            focusedContainerColor = Color(0xFFF7F8F9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF246BFD),
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = genderExpanded,
                        onDismissRequest = { genderExpanded = false },
                        modifier = Modifier.background(Color.White, RoundedCornerShape(12.dp))
                    ) {
                        genderOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option,
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    selectedGender = option
                                    genderExpanded = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = Color.Black
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Your Age",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                ExposedDropdownMenuBox(
                    expanded = ageExpanded,
                    onExpandedChange = { ageExpanded = it }
                ) {
                    OutlinedTextField(
                        value = selectedAge ?: "",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .menuAnchor(),
                        placeholder = {
                            Text(
                                text = "Select your age",
                                color = Color.Gray
                            )
                        },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.ArrowDropDown,
                                contentDescription = null,
                                tint = Color.Black
                            )
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedContainerColor = Color(0xFFF7F8F9),
                            focusedContainerColor = Color(0xFFF7F8F9),
                            unfocusedBorderColor = Color.Transparent,
                            focusedBorderColor = Color(0xFF246BFD),
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = ageExpanded,
                        onDismissRequest = { ageExpanded = false },
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(12.dp))
                            .heightIn(max = 300.dp)
                    ) {
                        ageOptions.forEach { option ->
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        option,
                                        color = Color.Black
                                    )
                                },
                                onClick = {
                                    selectedAge = option
                                    ageExpanded = false
                                },
                                colors = MenuDefaults.itemColors(
                                    textColor = Color.Black
                                )
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Write Your Problem",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = problem,
                    onValueChange = { problem = it },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    placeholder = {
                        Text(
                            text = "Describe your problem...",
                            color = Color.Gray
                        )
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF7F8F9),
                        focusedContainerColor = Color(0xFFF7F8F9),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF246BFD),
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black
                    ),
                    shape = RoundedCornerShape(12.dp),
                    maxLines = 10
                )

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}