package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppointmentRescheduler(
    onClose: () -> Unit,
    onNavigateToAppointments: () -> Unit
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "reason"
    ) {
        composable(
            route = "reason",
            enterTransition = { slideInHorizontally(initialOffsetX = { it }) + fadeIn() },
            exitTransition = { slideOutHorizontally(targetOffsetX = { it }) + fadeOut() },
            popEnterTransition = { slideInHorizontally(initialOffsetX = { -it }) + fadeIn() },
            popExitTransition = { slideOutHorizontally(targetOffsetX = { -it }) + fadeOut() }
        ) {
            ReasonScreen(
                onNext = { navController.navigate("date") },
                onBack = onClose,
                onNavigateToAppointments = onNavigateToAppointments
            )
        }

        composable(
            route = "date",
            enterTransition = {
                slideInHorizontally(initialOffsetX = { it }) +
                        fadeIn(animationSpec = tween(300)) +
                        scaleIn(initialScale = 0.9f)
            },
            exitTransition = {
                slideOutHorizontally(targetOffsetX = { it }) +
                        fadeOut(animationSpec = tween(300)) +
                        scaleOut(targetScale = 0.9f)
            },
            popEnterTransition = {
                slideInHorizontally(initialOffsetX = { -it }) +
                        fadeIn(animationSpec = tween(300))
            },
            popExitTransition = {
                slideOutHorizontally(targetOffsetX = { -it }) +
                        fadeOut(animationSpec = tween(300))
            }
        ) {
            DateHourScreen(
                onBack = { navController.popBackStack() },
                onNavigateToAppointments = onNavigateToAppointments
            )
        }
    }
}