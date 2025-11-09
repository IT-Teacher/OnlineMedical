package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import uz.itteacher.onlinemedical.R
import uz.itteacher.onlinemedical.model.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentsScreen() {
    var selectedTab by remember { mutableStateOf(0) }
    val pagerState = rememberPagerState(initialPage = 0) { 3 }
    val scope = rememberCoroutineScope()
    var showCancelDialog by remember { mutableStateOf(false) }
    var showCancelScreen by remember { mutableStateOf(false) }
    var showReschedule by remember { mutableStateOf(false) }
    var selectedAppointment by remember { mutableStateOf<Appointment?>(null) }


    val allAppointments = listOf(
        Appointment(
            "Drake Boeson", "Messaging", "Today", "1:00 PM", 30,
            AppointmentStatus.UPCOMING, R.drawable.ic_launcher_foreground, "+998990444804", "1"
        ),
        Appointment(
            "Jenny Watson", "Voice Call", "Today", "6:00 AM", 30,
            AppointmentStatus.UPCOMING, R.drawable.ic_launcher_foreground, "+998990444804", "1"
        ),
        Appointment(
            "Aidan Allende", "Video Call", "Dec 14, 2022", "15:00 PM", 30,
            AppointmentStatus.COMPLETED, R.drawable.ic_launcher_foreground, "+998990444804", "2"
        ),
        Appointment(
            "Raul Zirkind", "Voice Call", "Dec 12, 2022", "16:00 PM", 30,
            AppointmentStatus.CANCELLED, R.drawable.ic_launcher_foreground, "", "3"
        )
    )

    LaunchedEffect(selectedTab) {
        if (pagerState.currentPage != selectedTab) {
            pagerState.animateScrollToPage(selectedTab)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .collect { page ->
                if (selectedTab != page) {
                    selectedTab = page
                }
            }
    }

    if (showCancelScreen) {
        AnimatedVisibility(
            visible = showCancelScreen,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 500),
                initialOffsetY = { it }
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300)
            ) { it } + fadeOut(tween(300))
        ) {
            CancelAppointmentScreen(
                onBack = { showCancelScreen = false },
                onCancelSuccess = { showCancelScreen = false }
            )
        }
    } else if (showReschedule) {
        AnimatedVisibility(
            visible = showReschedule,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 500),
                initialOffsetY = { it }
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300)
            ) { it } + fadeOut(tween(300))
        ) {
            AppointmentRescheduler(onClose = { showReschedule = false },
                onNavigateToAppointments = { showReschedule = false })
        }
    } else if (selectedAppointment != null) {
        AnimatedVisibility(
            visible = true,
            enter = slideInVertically(
                animationSpec = tween(durationMillis = 500),
                initialOffsetY = { it }
            ) + fadeIn(tween(300)),
            exit = slideOutVertically(
                animationSpec = tween(durationMillis = 300)
            ) { it } + fadeOut(tween(300))
        ) {
            ConsultationFlowScreen(
                appointment = selectedAppointment!!,
                onBackToHome = { selectedAppointment = null },
                onNavigateToAppointments = { selectedAppointment = null }
            )
        }
    } else {
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
                                0 -> AppointmentCard(
                                    appointment = appointment,
                                    onCancelClick = { showCancelDialog = true },
                                    onRescheduleClick = { showReschedule = true },
                                    onDetailClick = { selectedAppointment = appointment }
                                )
                                1 -> CompletedAppointmentCard(appointment)
                                2 -> CanceledAppointmentCard(appointment)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCancelDialog) {
        ModalBottomSheet(
            onDismissRequest = { showCancelDialog = false },
            sheetState = rememberModalBottomSheetState(),
            containerColor = Color.White,
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Cancel Appointment",
                    color = Color.Red,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Are you sure you want to cancel your appointment?",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Only 50% of the funds will be returned to your account.",
                    textAlign = TextAlign.Center,
                    fontSize = 16.sp,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    Button(
                        onClick = { showCancelDialog = false },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE3F2FD)),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Back", color = Color.Black, fontSize = 16.sp)
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            showCancelDialog = false
                            showCancelScreen = true
                        },
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF)),
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                    ) {
                        Text("Yes, Cancel", color = Color.White, fontSize = 16.sp)
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
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