package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.material3.*
import androidx.compose.runtime.*

@Composable
fun AppointmentTabs(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    val tabs = listOf("Upcoming", "Completed", "Cancelled")

    TabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = { onTabSelected(index) },
                text = { Text(title) }
            )
        }
    }
}