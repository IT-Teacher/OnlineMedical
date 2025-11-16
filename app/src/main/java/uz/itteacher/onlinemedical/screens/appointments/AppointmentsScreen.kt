// AppointmentsScreen.kt
package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import uz.itteacher.onlinemedical.R
import uz.itteacher.onlinemedical.model.AppointmentViewModel

@Composable
fun AppointmentsScreen(navController: NavHostController) {
    val viewModel: AppointmentViewModel = viewModel()
    val appointments = viewModel.appointments

    var selectedTab by remember { mutableStateOf(0) }

    val filteredAppointments = remember(appointments, selectedTab) {
        when (selectedTab) {
            0 -> appointments.filter { it.status == "UPCOMING" }
            1 -> appointments.filter { it.status == "COMPLETED" }
            else -> appointments.filter { it.status == "CANCELLED" }
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppointmentTopBar(
            onSearchClick = { /* TODO */ },
            onMoreClick = { /* TODO */ }
        )

        AppointmentTabs(onTabSelected = { selectedTab = it })

        if (filteredAppointments.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(filteredAppointments) { appointment ->
                    when (selectedTab) {
                        0 -> AppointmentCard(appointment, navController)
                        1 -> CompletedAppointmentCard(appointment, navController)
                        else -> CanceledAppointmentCard(appointment, navController)
                    }
                }
            }
        }
    }
}

@Composable
fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(100.dp)
            )
            Text("You don’t have an appointment yet", fontWeight = FontWeight.Bold)
            Text("You don’t have a doctor’s appointment scheduled at the moment.")
        }
    }
}