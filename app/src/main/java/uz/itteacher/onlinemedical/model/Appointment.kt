package uz.itteacher.onlinemedical.model

enum class AppointmentStatus { UPCOMING, COMPLETED, CANCELLED }

data class Appointment(
    val doctorName: String,
    val type: String,
    val date: String,
    val time: String,
    val status: AppointmentStatus,
    val imageRes: Int
)
