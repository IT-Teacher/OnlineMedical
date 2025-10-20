package uz.itteacher.onlinemedical


data class Doctor(
    val name: String = "",
    val about: String = "",
    val hospital: String = "",
    val imageUrl: String = "",
    val location: String = "",
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val speciality: String = "",
    val workingTime: String = ""
)
