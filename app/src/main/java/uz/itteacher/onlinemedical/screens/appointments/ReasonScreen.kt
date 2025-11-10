package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itteacher.onlinemedical.screens.appointments.components.BackAndTitle

private val blueColor = Color(0xFF3366FF)
private val whiteColor = Color(0xFFFFFFFF)

@Composable
fun ReasonScreen(
    onNext: () -> Unit,
    onBack: () -> Unit,
    onNavigateToAppointments: () -> Unit
) {
    val reasons = listOf(
        "I'm having a schedule clash",
        "I'm not available on schedule",
        "I have a activity that can't be left behind",
        "I don't want to tell",
        "Others"
    )
    var selectedReason by remember { mutableStateOf(reasons.last()) }
    var customReason by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 20.dp)
    ) {
        BackAndTitle(
            title = "Reschedule Appointment",
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text("Reason for Schedule Change", fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(12.dp))

        reasons.forEach { reason ->
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .clickable { selectedReason = reason }
            ) {
                RadioButton(
                    selected = (reason == selectedReason),
                    onClick = { selectedReason = reason },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = blueColor,
                        unselectedColor = blueColor
                    )
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(reason)
            }
        }

        if (selectedReason == "Others") {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9))
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    TextField(
                        value = customReason,
                        onValueChange = { customReason = it },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = { Text("Write your reason here", color = Color.Gray) },
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            focusedIndicatorColor = whiteColor,
                            unfocusedIndicatorColor = whiteColor
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onNext,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(containerColor = blueColor)
        ) {
            Text("Next", fontWeight = FontWeight.SemiBold, color = Color.White)
        }
    }
}