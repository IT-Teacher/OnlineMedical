package uz.itteacher.onlinemedical.Favourite

object DoctorRepository {
    val doctors = listOf(
        Doctor(
            "1",
            "Dr. Jenny Watson",
            "Dentist",
            "Smile Dental Care",
            4.9,
            942,
            "https://example.com/doctors/jenny_watson.jpg"
        ),
        Doctor(
            "2",
            "Dr. Randy Whigham",
            "General Practitioner",
            "City Health Clinic",
            4.8,
            834,
            "https://example.com/doctors/randy_whigham.jpg"
        ),
        Doctor(
            "3",
            "Dr. Raul Zinkand",
            "Nutritionist",
            "Healthy Life Center",
            4.7,
            650,
            "https://example.com/doctors/raul_zinkand.jpg"
        )
    )

    val favorites = listOf("1", "3") // uid_sample.favorites
}