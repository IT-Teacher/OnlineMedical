package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.runtime.*
import uz.itteacher.onlinemedical.model.Appointment

@Composable
fun ConsultationFlowScreen(
    appointment: Appointment,
    onBackToHome: () -> Unit,
    onNavigateToAppointments: () -> Unit
) {
    var currentStep by remember { mutableStateOf(Step.DETAIL) }

    when (currentStep) {
        Step.DETAIL -> AppointmentDetailScreen(
            appointment = appointment,
            onBack = onBackToHome,
            onCallEnded = { currentStep = Step.ENDED }
        )
        Step.ENDED -> ConsultationEndedScreen(
            onBackToHome = onBackToHome,
            onLeaveReview = { currentStep = Step.REVIEW }
        )
        Step.REVIEW -> WriteReviewScreen(
            onBack = { currentStep = Step.ENDED },
            doctorName = appointment.name,
            doctorId = appointment.doctorId ?: "1",
            userId = "uid_sample",
            onNavigateToAppointments = onNavigateToAppointments
        )
        Step.SUCCESS -> ReviewSuccessDialog(
            onDismiss = {
                currentStep = Step.DETAIL
                onBackToHome()
            }
        )
    }
}

private enum class Step { DETAIL, ENDED, REVIEW, SUCCESS }