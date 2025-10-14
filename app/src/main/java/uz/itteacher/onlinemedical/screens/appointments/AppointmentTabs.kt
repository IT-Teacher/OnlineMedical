package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AppointmentTabs(onTabSelected: (Int) -> Unit) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Upcoming", "Completed", "Cancelled")

    TabRow(selectedTabIndex = selectedTab) {
        tabs.forEachIndexed { index, title ->
            Tab(
                selected = selectedTab == index,
                onClick = {
                    selectedTab = index
                    onTabSelected(index)
                },
                text = { Text(title) }
            )
        }
    }
}
