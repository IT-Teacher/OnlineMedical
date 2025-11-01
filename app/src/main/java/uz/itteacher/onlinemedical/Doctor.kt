package uz.itteacher.onlinemedical

data class Doctor(
    val id: String = "",
    val name: String = "",
    val about: String = "",
    val hospital: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val speciality: String = "",
    val workingTime: String = "",
    val isFavorite: Boolean = false
)
