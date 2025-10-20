package uz.itteacher.onlinemedical

import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.tasks.await

class DoctorRepository {

    private val db = FirebaseDatabase.getInstance().getReference("doctors")

    suspend fun getDoctors(): List<Doctor> {
        val snapshot = db.get().await()
        val doctors = mutableListOf<Doctor>()
        for (child in snapshot.children) {
            val doctor = child.getValue(Doctor::class.java)
            doctor?.let { doctors.add(it) }
        }
        return doctors
    }
}
