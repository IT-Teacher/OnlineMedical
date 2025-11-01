package com.example.appointment

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

data class PaymentMethod(
    val id: String,
    val name: String,
    val iconRes: Int
)

@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: BookingViewModel
) {
    val bookingData by viewModel.bookingData.collectAsState()

    var selectedPayment by remember {
        mutableStateOf(
            if (bookingData.selectedCardId != null) "Card"
            else bookingData.paymentMethod.takeIf { it.isNotEmpty() } ?: "PayPal"
        )
    }
    var selectedCardId by remember { mutableStateOf(bookingData.selectedCardId) }

    val paymentMethods = listOf(
        PaymentMethod("PayPal", "PayPal", R.drawable.paypal),
        PaymentMethod("GooglePay", "Google Pay", R.drawable.google),
        PaymentMethod("ApplePay", "Apple Pay", R.drawable.apple)
    )

    Scaffold(
        bottomBar = {
            Button(
                onClick = {
                    if (selectedCardId != null) {
                        viewModel.selectCard(selectedCardId!!)
                    } else {
                        viewModel.updatePaymentMethod(selectedPayment)
                        viewModel.clearSelectedCard()
                    }
                    navController.navigate("review_summary")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF2563EB)
                )
            ) {
                Text("Next", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF9FAFB))
                .padding(padding)
                .padding(horizontal = 16.dp)
        ) {

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
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
                        text = "Payments",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.SemiBold,
                        modifier = Modifier.padding(start = 12.dp),
                        color = Color.Black
                    )
                }
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Black
                )
            }

            Text(
                text = "Select the payment method you want to use.",
                fontSize = 14.sp,
                color = Color.Gray,
                modifier = Modifier.padding(bottom = 24.dp)
            )


            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                paymentMethods.forEach { method ->
                    PaymentMethodItem(
                        method = method,
                        isSelected = selectedPayment == method.id && selectedCardId == null,
                        onSelect = {
                            selectedPayment = method.id
                            selectedCardId = null
                        }
                    )
                }


                bookingData.savedCards.forEach { card ->
                    SavedCardItem(
                        card = card,
                        isSelected = selectedCardId == card.id,
                        onSelect = {
                            selectedCardId = card.id
                            selectedPayment = "Card"
                        },
                        onEdit = {
                            navController.navigate("add_card/${card.id}")
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))


            Button(
                onClick = { navController.navigate("add_card") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFEFF6FF),
                    contentColor = Color(0xFF2563EB)
                )
            ) {
                Text(
                    "Add New Card",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
fun PaymentMethodItem(
    method: PaymentMethod,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSelect() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = method.iconRes),
                        contentDescription = method.name,
                        modifier = Modifier.size(24.dp)
                    )
                }


                Text(
                    text = method.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }


            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color(0xFF2563EB) else Color(0xFFD1D5DB),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB))
                    )
                }
            }
        }
    }
}

@Composable
fun SavedCardItem(
    card: CreditCard,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onEdit() },
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFF3F4F6)),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.mastercard),
                        contentDescription = "Card",
                        modifier = Modifier.size(24.dp)
                    )
                }


                Text(
                    text = "•••• •••• •••• ${getLastFourDigits(card.cardNumber)}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }


            Box(
                modifier = Modifier
                    .size(24.dp)
                    .border(
                        width = 2.dp,
                        color = if (isSelected) Color(0xFF2563EB) else Color(0xFFD1D5DB),
                        shape = CircleShape
                    )
                    .clickable(onClick = onSelect),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Box(
                        modifier = Modifier
                            .size(12.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB))
                    )
                }
            }
        }
    }
}