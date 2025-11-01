package com.example.appointment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.foundation.text.KeyboardOptions
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddNewCardScreen(
    navController: NavController,
    viewModel: BookingViewModel,
    cardId: String? = null
) {
    val bookingData by viewModel.bookingData.collectAsState()

    val existingCard = cardId?.let { id ->
        bookingData.savedCards.find { it.id == id }
    }

    var cardHolderName by remember { mutableStateOf(existingCard?.cardHolderName ?: "") }
    var cardNumberValue by remember {
        mutableStateOf(TextFieldValue(existingCard?.cardNumber ?: ""))
    }
    var expiryDateValue by remember {
        mutableStateOf(TextFieldValue(existingCard?.expiryDate ?: ""))
    }
    var cvv by remember { mutableStateOf(existingCard?.cvv ?: "") }

    val cardNumber = cardNumberValue.text
    val expiryDate = expiryDateValue.text

    val isFormValid = cardHolderName.length >= 3 &&
            cardNumber.replace(" ", "").length == 16 &&
            expiryDate.length == 8 &&
            cvv.length == 3

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    if (existingCard != null) {
                        val updatedCard = existingCard.copy(
                            cardHolderName = cardHolderName,
                            cardNumber = cardNumber,
                            expiryDate = expiryDate,
                            cvv = cvv
                        )
                        viewModel.updateCard(updatedCard)
                    } else {
                        val card = CreditCard(
                            id = UUID.randomUUID().toString(),
                            cardHolderName = cardHolderName,
                            cardNumber = cardNumber,
                            expiryDate = expiryDate,
                            cvv = cvv,
                            cardType = "Mastercard"
                        )
                        viewModel.addCard(card)
                    }
                    navController.popBackStack()
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF246BFD),
                    disabledContainerColor = Color(0xFF93C5FD)
                )
            ) {
                Text(
                    text = if (existingCard != null) "Update" else "Add",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .padding(horizontal = 16.dp, vertical = 16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    modifier = Modifier
                        .size(24.dp)
                        .clickable { navController.popBackStack() },
                    tint = Color.Black
                )
                Text(
                    text = if (existingCard != null) "Edit Card" else "Add New Card",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(start = 12.dp),
                    color = Color.Black
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp)
                    .height(200.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        androidx.compose.ui.graphics.Brush.linearGradient(
                            colors = listOf(
                                Color(0xFF4E8EF7),
                                Color(0xFF246BFD)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Mocard",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Image(
                            painter = painterResource(id = R.drawable.amazon),
                            contentDescription = "Card Type",
                            modifier = Modifier.size(50.dp),
                            colorFilter = ColorFilter.tint(Color.White)
                        )
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Text(
                        text = if (cardNumber.isEmpty()) "•••• •••• •••• ••••" else formatCardNumber(cardNumber),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.White,
                        letterSpacing = 2.sp
                    )

                    Spacer(modifier = Modifier.weight(1f))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = "Card holder name",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = if (cardHolderName.isEmpty()) "---" else cardHolderName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                        Column {
                            Text(
                                text = "Expiry date",
                                fontSize = 10.sp,
                                color = Color.White.copy(alpha = 0.7f)
                            )
                            Text(
                                text = if (expiryDate.isEmpty()) "MM/DD/YY" else expiryDate,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = Color.White
                            )
                        }
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(25.dp)),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.mastercard_white),
                                contentDescription = "Toggle",
                                modifier = Modifier.size(56.dp),
                                colorFilter = ColorFilter.tint(Color.White)
                            )
                        }
                    }
                }
            }

            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Card Name",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = cardHolderName,
                    onValueChange = { cardHolderName = it },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("Andrew Ainsley", color = Color.Gray)
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF7F8F9),
                        focusedContainerColor = Color(0xFFF7F8F9),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF246BFD),
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        cursorColor = Color(0xFF246BFD)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Card Number",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(12.dp))
                OutlinedTextField(
                    value = cardNumberValue,
                    onValueChange = { newValue ->
                        val digitsOnly = newValue.text.filter { it.isDigit() }
                        if (digitsOnly.length <= 16) {
                            val formatted = digitsOnly.chunked(4).joinToString(" ")
                            val cursorPosition = formatted.length
                            cardNumberValue = TextFieldValue(
                                text = formatted,
                                selection = TextRange(cursorPosition)
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth(),
                    placeholder = {
                        Text("2672 4738 7837 7285", color = Color.Gray)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedContainerColor = Color(0xFFF7F8F9),
                        focusedContainerColor = Color(0xFFF7F8F9),
                        unfocusedBorderColor = Color.Transparent,
                        focusedBorderColor = Color(0xFF246BFD),
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black,
                        cursorColor = Color(0xFF246BFD)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Expiry Date",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = expiryDateValue,
                            onValueChange = { newValue ->
                                val digitsOnly = newValue.text.filter { it.isDigit() }

                                if (digitsOnly.length <= 6) {
                                    val formatted = when (digitsOnly.length) {
                                        0 -> ""
                                        1 -> {
                                            if (digitsOnly.toInt() > 1) "0$digitsOnly/" else digitsOnly
                                        }
                                        2 -> {
                                            val month = digitsOnly.toInt()
                                            if (month > 12) "12/" else "$digitsOnly/"
                                        }
                                        3 -> {
                                            val month = digitsOnly.substring(0, 2).toInt()
                                            val validMonth = if (month > 12) "12" else digitsOnly.substring(0, 2)
                                            "$validMonth/${digitsOnly.substring(2)}"
                                        }
                                        4 -> {
                                            val month = digitsOnly.substring(0, 2).toInt()
                                            val day = digitsOnly.substring(2, 4).toInt()
                                            val validMonth = if (month > 12) "12" else digitsOnly.substring(0, 2)
                                            val validDay = if (day > 31) "31" else digitsOnly.substring(2, 4)
                                            "$validMonth/$validDay/"
                                        }
                                        else -> {
                                            val month = digitsOnly.substring(0, 2).toInt()
                                            val day = digitsOnly.substring(2, 4).toInt()
                                            val year = digitsOnly.substring(4)
                                            val validMonth = if (month > 12) "12" else digitsOnly.substring(0, 2)
                                            val validDay = if (day > 31) "31" else digitsOnly.substring(2, 4)
                                            "$validMonth/$validDay/$year"
                                        }
                                    }
                                    val cursorPosition = formatted.length
                                    expiryDateValue = TextFieldValue(
                                        text = formatted,
                                        selection = TextRange(cursorPosition)
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("09/07/26", color = Color.Gray)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF7F8F9),
                                focusedContainerColor = Color(0xFFF7F8F9),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF246BFD),
                                unfocusedTextColor = Color.Black,
                                focusedTextColor = Color.Black,
                                cursorColor = Color(0xFF246BFD)
                            ),
                            shape = RoundedCornerShape(12.dp),
                            trailingIcon = {
                                Image(
                                    painter = painterResource(id = R.drawable.img),
                                    contentDescription = "Calendar",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        )
                    }

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "CVV",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        OutlinedTextField(
                            value = cvv,
                            onValueChange = {
                                if (it.length <= 3 && it.all { char -> char.isDigit() }) {
                                    cvv = it
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            placeholder = {
                                Text("699", color = Color.Gray)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedContainerColor = Color(0xFFF7F8F9),
                                focusedContainerColor = Color(0xFFF7F8F9),
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF246BFD),
                                unfocusedTextColor = Color.Black,
                                focusedTextColor = Color.Black,
                                cursorColor = Color(0xFF246BFD)
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}


fun formatCardNumber(input: String): String {
    val digits = input.replace(" ", "")
    return buildString {
        digits.forEachIndexed { index, char ->
            if (index > 0 && index % 4 == 0) append(" ")
            append(char)
        }
    }.padEnd(19, '•')
}

fun getLastFourDigits(cardNumber: String): String {
    val digits = cardNumber.replace(" ", "")
    return if (digits.length >= 4) digits.takeLast(4) else "0000"
}