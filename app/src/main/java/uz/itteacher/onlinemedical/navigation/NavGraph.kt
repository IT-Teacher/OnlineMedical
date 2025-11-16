// NavGraph.kt
package uz.itteacher.onlinemedical.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import uz.itteacher.onlinemedical.screens.appointments.AppointmentsScreen
import uz.itteacher.onlinemedical.screens.appointments.ChatScreen
import uz.itteacher.onlinemedical.screens.appointments.MessagingAppointmentScreen

object Screens {
    const val APPOINTMENTS = "appointments"
    const val MESSAGING_APPOINTMENT = "messaging_appointment"
    const val CHAT = "chat"
}

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Screens.APPOINTMENTS
    ) {
        composable(Screens.APPOINTMENTS) {
            AppointmentsScreen(navController = navController)
        }
        composable(Screens.MESSAGING_APPOINTMENT) {
            MessagingAppointmentScreen(navController = navController)
        }
        composable(Screens.CHAT) {
            ChatScreen(navController = navController)
        }
    }
}