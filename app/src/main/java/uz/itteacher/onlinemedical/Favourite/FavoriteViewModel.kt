package uz.itteacher.onlinemedical.Favourite

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteViewModel : ViewModel() {
    private val _favorites = MutableStateFlow(DoctorRepository.doctors.filter {
        DoctorRepository.favorites.contains(it.id)
    })
    val favorites: StateFlow<List<Doctor>> = _favorites

    private val _showDialog = MutableStateFlow<Doctor?>(null)
    val showDialog: StateFlow<Doctor?> = _showDialog

    fun onRemoveClick(doctor: Doctor) {
        _showDialog.value = doctor
    }

    fun confirmRemove(doctor: Doctor) {
        _favorites.value = _favorites.value.filterNot { it.id == doctor.id }
        _showDialog.value = null
    }

    fun cancelDialog() {
        _showDialog.value = null
    }
}
