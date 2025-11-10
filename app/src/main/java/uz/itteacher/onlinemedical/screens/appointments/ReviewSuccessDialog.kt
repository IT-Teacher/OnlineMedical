package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

@Composable
fun ReviewSuccessDialog(onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(Color.White),
            modifier = Modifier.padding(16.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    // Main circle
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFF3366FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Filled.Star, null, tint = Color.White, modifier = Modifier.size(50.dp))
                    }

                    Box(
                        modifier = Modifier
                            .offset(x = (-35).dp, y = (-50).dp)
                            .size(15.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = 35.dp, y = (-50).dp)
                            .size(15.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = (-60).dp, y = 0.dp)
                            .size(10.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = 60.dp, y = 0.dp)
                            .size(10.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = (-35).dp, y = 50.dp)
                            .size(15.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = 35.dp, y = 50.dp)
                            .size(15.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = 0.dp, y = (-70).dp)
                            .size(8.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .offset(x = 0.dp, y = 70.dp)
                            .size(8.dp)
                            .background(Color(0xFF6699FF), CircleShape)
                    )
                }
                Spacer(Modifier.height(16.dp))
                Text("Review Successful!", color = Color(0xFF3366FF), fontWeight = FontWeight.Bold, fontSize = 20.sp)
                Spacer(Modifier.height(8.dp))
                Text(
                    "Your review has been successfully submitted, thank you very much!",
                    textAlign = TextAlign.Center,
                    color = Color(0xFF666666)
                )
                Spacer(Modifier.height(24.dp))
                Button(
                    onClick = onDismiss,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(50.dp)
                ) {
                    Text("OK", color = Color.White)
                }
            }
        }
    }
}