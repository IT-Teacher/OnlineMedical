package uz.itteacher.onlinemedical



import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DoctorViewModel(
    private val repo: DoctorRepository = DoctorRepository()
) : ViewModel() {

    private val _doctors = MutableStateFlow<List<Doctor>>(emptyList())
    val doctors: StateFlow<List<Doctor>> = _doctors

    private val _isLoading = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        fetchDoctors()
    }

    private fun fetchDoctors() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val list = repo.getDoctors()
                _doctors.value = list
                _error.value = null
            } catch (e: Exception) {
                _doctors.value = emptyList()
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    // agar qo'lda refresh kerak bo'lsa
    fun refresh() = fetchDoctors()
}
