// AppointmentViewModel.kt
package uz.itteacher.onlinemedical.model

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import uz.itteacher.onlinemedical.model.AppointmentData
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class AppointmentViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val currentUid = auth.currentUser?.uid ?: "uid_sample"
    private val dbAppointments: DatabaseReference = FirebaseDatabase.getInstance().getReference("appointments/$currentUid")
    private val dbDoctors: DatabaseReference = FirebaseDatabase.getInstance().getReference("doctors")
    private val rawAppointments = mutableListOf<Map<String, Any>>()
    private val doctors = mutableListOf<Doctor>()
    val appointments = mutableStateListOf<AppointmentData>()

    init {
        listenToAppointments()
        listenToDoctors()
    }

    private fun listenToAppointments() {
        dbAppointments.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rawAppointments.clear()
                for (child in snapshot.children) {
                    val aptMap = child.value as? Map<String, Any>
                    if (aptMap != null) {
                        rawAppointments.add(aptMap)
                    }
                }
                enrichAppointments()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun listenToDoctors() {
        dbDoctors.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                doctors.clear()
                for (child in snapshot.children) {
                    val docMap = child.value as? Map<String, Any>
                    if (docMap != null) {
                        val doc = Doctor(
                            id = child.key ?: "",
                            name = docMap["name"] as? String ?: "",
                            speciality = docMap["speciality"] as? String ?: "",
                            location = docMap["location"] as? String ?: "",
                            hospital = docMap["hospital"] as? String ?: "",
                            imageUrl = docMap["imageUrl"] as? String ?: ""
                        )
                        doctors.add(doc)
                    }
                }
                enrichAppointments()
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    private fun enrichAppointments() {
        appointments.clear()
        for (raw in rawAppointments) {
            val patient = raw["patientDetails"] as? Map<String, Any> ?: continue
            val doctorId = raw["doctorId"] as? String ?: continue
            val doc = doctors.find { it.id == doctorId }

            val startTimeStr = raw["time"] as? String ?: continue
            val formatter = DateTimeFormatter.ofPattern("hh:mm a")
            val startTime = LocalTime.parse(startTimeStr, formatter)
            val endTime = startTime.plusMinutes(30)
            val timeRange = "${formatter.format(startTime)} – ${formatter.format(endTime)}"

            val dateStr = raw["date"] as? String ?: continue
            val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
            val dateParsed = LocalDate.parse(dateStr, dateFormatter)
            val today = LocalDate.now()
            val dateDisplay = when {
                dateParsed == today -> "Today, ${dateParsed.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))}"
                dateParsed == today.plusDays(1) -> "Tomorrow, ${dateParsed.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))}"
                else -> dateParsed.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy"))
            }

            val statusRaw = raw["status"] as? String ?: "UPCOMING"
            val status = when (statusRaw) {
                "Confirmed" -> "UPCOMING"
                "Completed" -> "COMPLETED"
                "Cancelled" -> "CANCELLED"
                else -> statusRaw
            }

            val apt = AppointmentData(
                id = "", // No id in DB, perhaps generate or use index
                doctorName = raw["doctorName"] as? String ?: "",
                doctorJob = doc?.speciality ?: "",
                doctorLocation = if (doc != null) "${doc.hospital} in ${doc.location}" else "",
                doctorPhotoUrl = doc?.imageUrl ?: "",
                patientName = patient["fullName"] as? String ?: "",
                patientGender = patient["gender"] as? String ?: "",
                patientAge = (patient["age"] as? Long)?.toInt() ?: 0,
                problem = patient["problem"] as? String ?: "",
                date = dateDisplay,
                time = timeRange,
                duration = "30 minutes",
                packageType = raw["package"] as? String ?: "Messaging",
                packagePrice = "$" + (raw["price"] as? Long ?: 20),
                status = status
            )
            appointments.add(apt)
        }
    }
}

data class Doctor(
    val id: String,
    val name: String,
    val speciality: String,
    val location: String,
    val hospital: String,
    val imageUrl: String
)