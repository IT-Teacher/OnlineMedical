package uz.itteacher.onlinemedical.favourite.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

import uz.itteacher.onlinemedical.Doctor  // <- **BU YERDA umumiy Doctor modeli import qilindi**

class FavoriteViewModel : ViewModel() {

    private val db = FirebaseDatabase.getInstance().getReference("favorites")
    private val doctorDb = FirebaseDatabase.getInstance().getReference("doctors")
    private val uid = FirebaseAuth.getInstance().currentUser?.uid ?: "uid_sample"

    private val _favorites = MutableStateFlow<List<Doctor>>(emptyList())
    val favorites: StateFlow<List<Doctor>> = _favorites.asStateFlow()

    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteIds: StateFlow<Set<String>> = _favoriteIds.asStateFlow()

    private val _showDialog = MutableStateFlow<Doctor?>(null)
    val showDialog: StateFlow<Doctor?> = _showDialog.asStateFlow()

    init {
        loadFavorites()
    }

    private fun loadFavorites() {
        db.child(uid).child("doctorIds").get().addOnSuccessListener { snapshot ->
            val ids = mutableSetOf<String>()
            snapshot.children.forEach {
                it.getValue(String::class.java)?.let(ids::add)
            }
            _favoriteIds.value = ids

            doctorDb.get().addOnSuccessListener { doctorSnap ->
                val doctors = mutableListOf<Doctor>()
                doctorSnap.children.forEach { doc ->
                    val doctor = doc.getValue(Doctor::class.java)
                    val key = doc.key ?: ""
                    if (doctor != null && ids.contains(key)) {
                        // doctor modelimizga id va isFavorite qo'shamiz
                        doctors.add(doctor.copy(id = key, isFavorite = true))
                    }
                }
                _favorites.value = doctors
            }
        }
    }

    fun addFavorite(doctor: Doctor) {
        val id = doctor.id
        if (id.isEmpty()) return

        viewModelScope.launch {
            val updated = _favoriteIds.value.toMutableSet().apply { add(id) }
            db.child(uid).child("doctorIds").setValue(updated.toList())
                .addOnSuccessListener { loadFavorites() }
        }
    }

    fun removeFavorite(doctor: Doctor) {
        val id = doctor.id
        if (id.isEmpty()) return

        viewModelScope.launch {
            val updated = _favoriteIds.value.toMutableSet().apply { remove(id) }
            db.child(uid).child("doctorIds").setValue(updated.toList())
                .addOnSuccessListener { loadFavorites() }
        }
    }

    fun toggleFavorite(doctor: Doctor) {
        if (_favoriteIds.value.contains(doctor.id)) removeFavorite(doctor) else addFavorite(doctor)
    }

    fun isFavorite(id: String): Boolean = _favoriteIds.value.contains(id)

    fun onRemoveClick(doctor: Doctor) {
        _showDialog.value = doctor
    }

    fun confirmRemove(doctor: Doctor) {
        removeFavorite(doctor)
        _showDialog.value = null
    }

    fun cancelDialog() {
        _showDialog.value = null
    }
}

