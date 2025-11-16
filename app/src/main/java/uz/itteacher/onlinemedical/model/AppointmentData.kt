// model/AppointmentData.kt
package uz.itteacher.onlinemedical.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class AppointmentData(
    val id: String = "",
    val doctorName: String = "",
    val doctorJob: String = "",
    val doctorLocation: String = "",
    val doctorPhotoUrl: String = "",
    val patientName: String = "",
    val patientGender: String = "",
    val patientAge: Int = 0,
    val problem: String = "",
    val date: String = "",
    val time: String = "",
    val duration: String = "30 minutes",
    val packageType: String = "Messaging",
    val packagePrice: String = "$20",
    val status: String = "UPCOMING"
) : Parcelable