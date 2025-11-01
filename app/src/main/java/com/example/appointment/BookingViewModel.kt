package com.example.appointment

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class CreditCard(
    val id: String,
    val cardHolderName: String,
    val cardNumber: String,
    val expiryDate: String,
    val cvv: String,
    val cardType: String = "Mastercard"
)

data class BookingData(
    val doctorName: String = "Dr. Jenny Watson",
    val specialty: String = "Immunologists",
    val hospital: String = "Christ Hospital in London, UK",
    val doctorImage: Int = R.drawable.doc_profile,
    val selectedDate: String = "",
    val selectedTime: String = "",
    val packageType: String = "",
    val packagePrice: Int = 0,
    val duration: String = "",
    val patientName: String = "",
    val patientGender: String = "",
    val patientAge: String = "",
    val patientProblem: String = "",
    val paymentMethod: String = "",
    val savedCards: List<CreditCard> = emptyList(),
    val selectedCardId: String? = null
)

class BookingViewModel : ViewModel() {
    private val _bookingData = MutableStateFlow(BookingData())
    val bookingData: StateFlow<BookingData> = _bookingData.asStateFlow()

    fun updateDateTime(date: String, time: String) {
        _bookingData.value = _bookingData.value.copy(
            selectedDate = date,
            selectedTime = time
        )
    }

    fun updatePackage(packageType: String, duration: String, price: Int) {
        _bookingData.value = _bookingData.value.copy(
            packageType = packageType,
            duration = duration,
            packagePrice = price
        )
    }

    fun updatePatientDetails(name: String, gender: String, age: String, problem: String) {
        _bookingData.value = _bookingData.value.copy(
            patientName = name,
            patientGender = gender,
            patientAge = age,
            patientProblem = problem
        )
    }

    fun updatePaymentMethod(method: String) {
        _bookingData.value = _bookingData.value.copy(paymentMethod = method)
    }

    fun addCard(card: CreditCard) {
        val currentCards = _bookingData.value.savedCards.toMutableList()
        currentCards.add(card)
        _bookingData.value = _bookingData.value.copy(
            savedCards = currentCards,
            selectedCardId = card.id,
            paymentMethod = "Card"
        )
    }

    fun selectCard(cardId: String) {
        _bookingData.value = _bookingData.value.copy(
            selectedCardId = cardId,
            paymentMethod = "Card"
        )
    }

    fun clearSelectedCard() {
        _bookingData.value = _bookingData.value.copy(
            selectedCardId = null
        )
    }

    fun updateCard(updatedCard: CreditCard) {
        val currentCards = _bookingData.value.savedCards.toMutableList()
        val index = currentCards.indexOfFirst { it.id == updatedCard.id }
        if (index != -1) {
            currentCards[index] = updatedCard
            _bookingData.value = _bookingData.value.copy(
                savedCards = currentCards,
                selectedCardId = updatedCard.id,
                paymentMethod = "Card"
            )
        }
    }
}