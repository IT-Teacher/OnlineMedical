package uz.itteacher.onlinemedical.Favourite

data class Doctor(
    val id: String = "",
    val name: String = "",
    val speciality: String = "",
    val hospital: String = "",
    val rating: Double = 0.0,
    val reviews: Int = 0,
    val imageUrl: String = ""
)