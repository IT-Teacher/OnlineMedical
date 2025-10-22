package uz.itteacher.onlinemedical

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.tasks.await
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class DoctorRepository {

    private val dbRef = FirebaseDatabase.getInstance().getReference("doctors")

    // suspend qilib, Firebase snapshotni kutadigan oddiy funksiya
    suspend fun getDoctors(): List<Doctor> = suspendCoroutine { cont ->
        dbRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                try {
                    val list = mutableListOf<Doctor>()
                    for (child in snapshot.children) {
                        val doctor = child.getValue(Doctor::class.java)
                        if (doctor != null) list.add(doctor)
                    }
                    cont.resume(list)
                } catch (e: Exception) {
                    cont.resumeWithException(e)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                cont.resumeWithException(Exception("Firebase cancelled: ${error.message}"))
            }
        })
    }
}
