package uz.itteacher.onlinemedical.utils

import java.time.*
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale

object TimeUtils {
    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm a", Locale.ENGLISH) // 2:00 PM

    fun parseTime(timeStr: String): LocalTime? = try {
        LocalTime.parse(timeStr.uppercase(), timeFormatter)
    } catch (e: Exception) {
        null
    }

    fun isWithinCallWindow(
        appointmentTime: String,
        durationMinutes: Int = 30
    ): Boolean {
        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val startTime = parseTime(appointmentTime) ?: return false
        val start = today.atTime(startTime)
        val end = start.plusMinutes(durationMinutes.toLong())
        return now.isAfter(start.minusMinutes(1)) && now.isBefore(end.plusMinutes(1))
    }

    fun getTimeStatus(
        appointmentTime: String,
        durationMinutes: Int = 30
    ): String {
        val now = LocalDateTime.now()
        val today = now.toLocalDate()
        val startTime = parseTime(appointmentTime) ?: return "Invalid time"
        val start = today.atTime(startTime)
        val end = start.plusMinutes(durationMinutes.toLong())

        return when {
            now.isBefore(start.minusMinutes(1)) -> {
                val mins = java.time.Duration.between(now, start).toMinutes()
                "Call available in ${mins}min"
            }
            now.isAfter(end) -> "Call window closed"
            else -> "Call now"
        }
    }
}