//package uz.itteacher.onlinemedical.screens.appointments
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.pager.HorizontalPager
//import androidx.compose.foundation.pager.rememberPagerState
//import androidx.compose.material3.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import kotlinx.coroutines.launch
//import uz.itteacher.onlinemedical.R
//import uz.itteacher.onlinemedical.model.*
//
//@Composable
//fun AppointmentsScreen() {
//    var selectedTab by remember { mutableStateOf(0) }
//    val pagerState = rememberPagerState(initialPage = 0) { 3 }
//    val scope = rememberCoroutineScope()
//
//    val allAppointments = listOf(
//        Appointment("Raul Zirkind", "Voice Call", "Dec 12, 2022", "16:00 PM",
//            AppointmentStatus.CANCELLED, R.drawable.ic_launcher_foreground),
//        Appointment("Aidan Allende", "Video Call", "Dec 14, 2022", "15:00 PM",
//            AppointmentStatus.COMPLETED, R.drawable.ic_launcher_foreground),
//        Appointment("Drake Boeson", "Messaging", "Today", "16:00 PM",
//            AppointmentStatus.UPCOMING, R.drawable.ic_launcher_foreground),
//    )
//
//    LaunchedEffect(pagerState.currentPage) {
//        selectedTab = pagerState.currentPage
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        AppointmentTopBar(
//            onSearchClick = { /* TODO: handle search */ },
//            onMoreClick = { /* TODO: handle menu */ }
//        )
//
//        AppointmentTabs(onTabSelected = { index ->
//            selectedTab = index
//            scope.launch {
//                pagerState.animateScrollToPage(index)
//            }
//        })
//
//        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
//            val filteredAppointments = when (page) {
//                0 -> allAppointments.filter { it.status == AppointmentStatus.UPCOMING }
//                1 -> allAppointments.filter { it.status == AppointmentStatus.COMPLETED }
//                else -> allAppointments.filter { it.status == AppointmentStatus.CANCELLED }
//            }
//
//            if (filteredAppointments.isEmpty()) {
//                EmptyState()
//            } else {
//                LazyColumn(modifier = Modifier.fillMaxSize()) {
//                    items(filteredAppointments) { appointment ->
//                        when (page) {
//                            0 -> AppointmentCard(appointment)
//                            1 -> CompletedAppointmentCard(appointment)
//                            2 -> CanceledAppointmentCard(appointment)
//                        }
//                    }
//                }
//            }
//        }
//    }
//}
//
//@Composable
//fun EmptyState() {
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(32.dp),
//        contentAlignment = Alignment.Center
//    ) {
//        Column(horizontalAlignment = Alignment.CenterHorizontally) {
//            Icon(
//                painter = painterResource(id = R.drawable.ic_launcher_foreground),
//                contentDescription = null,
//                tint = MaterialTheme.colorScheme.primary,
//                modifier = Modifier.size(100.dp)
//            )
//            Text("You don’t have an appointment yet", fontWeight = FontWeight.Bold)
//            Text("You don’t have a doctor’s appointment scheduled at the moment.")
//        }
//    }
//}
package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import uz.itteacher.onlinemedical.R
import uz.itteacher.onlinemedical.model.*

@Composable
fun AppointmentsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0) { 3 }
    val scope = rememberCoroutineScope()

    val allAppointments = listOf(
        Appointment("Raul Zirkind", "Voice Call", "Dec 12, 2022", "16:00 PM",
            AppointmentStatus.CANCELLED, R.drawable.ic_launcher_foreground),
        Appointment("Aidan Allende", "Video Call", "Dec 14, 2022", "15:00 PM",
            AppointmentStatus.COMPLETED, R.drawable.ic_launcher_foreground),
        Appointment("Drake Boeson", "Messaging", "Today", "16:00 PM",
            AppointmentStatus.UPCOMING, R.drawable.ic_launcher_foreground),
    )

    LaunchedEffect(pagerState.currentPage) {
        selectedTab = pagerState.currentPage
    }

    Column(modifier = Modifier.fillMaxSize()) {
        AppointmentTopBar(
            onSearchClick = { /* TODO: handle search */ },
            onMoreClick = { /* TODO: handle menu */ }
        )

        AppointmentTabs(
            selectedTab = selectedTab,
            onTabSelected = { index ->
                selectedTab = index
                scope.launch {
                    pagerState.animateScrollToPage(index)
                }
            }
        )

        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxSize()) { page ->
            val filteredAppointments = when (page) {
                0 -> allAppointments.filter { it.status == AppointmentStatus.UPCOMING }
                1 -> allAppointments.filter { it.status == AppointmentStatus.COMPLETED }
                else -> allAppointments.filter { it.status == AppointmentStatus.CANCELLED }
            }

            if (filteredAppointments.isEmpty()) {
                EmptyState()
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(filteredAppointments) { appointment ->
                        when (page) {
                            0 -> AppointmentCard(appointment)
                            1 -> CompletedAppointmentCard(appointment)
                            2 -> CanceledAppointmentCard(appointment)
                        }
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