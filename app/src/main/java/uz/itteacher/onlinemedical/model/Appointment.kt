// model/Appointment.kt
package uz.itteacher.onlinemedical.model

import androidx.annotation.DrawableRes

enum class AppointmentStatus {
    UPCOMING, COMPLETED, CANCELLED
}

data class Appointment(
    val name: String,
    val type: String,
    val date: String,
    val time: String,
    val durationMinutes: Int = 30,
    val status: AppointmentStatus,
    @DrawableRes val imageRes: Int,
    val doctorPhone: String = "",
    val doctorId: String? = null  // ← ADD THIS
) {
    init {
        require(doctorPhone.isNotBlank() || status == AppointmentStatus.CANCELLED) {
            "doctorPhone must not be blank for UPCOMING or COMPLETED appointments."
        }
    }

    fun canCall(): Boolean = status == AppointmentStatus.UPCOMING && doctorPhone.isNotBlank()
}