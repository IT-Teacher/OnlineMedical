package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

private val blueColor = Color(0xFF3366FF)
private val lightBlueBg = Color(0xFFE6EEFF)

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun SuccessDialog(
    onDismiss: () -> Unit,
    onViewAppointment: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss
    ) {
        AnimatedVisibility(
            visible = true,
            enter = scaleIn(initialScale = 0.8f, animationSpec = tween(400)) + fadeIn(tween(400)),
            exit = scaleOut(targetScale = 0.8f, animationSpec = tween(300)) + fadeOut(tween(300))
        ) {
            Column(
                modifier = Modifier
                    .width(300.dp)
                    .background(Color.White, RoundedCornerShape(30.dp))
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                AnimatedVisibility(
                    visible = true,
                    enter = scaleIn(
                        initialScale = 0f,
                        animationSpec = tween(500, delayMillis = 100)
                    ) + fadeIn(tween(300, delayMillis = 100)),
                    exit = scaleOut() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(blueColor, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarToday,
                            contentDescription = "Calendar Icon",
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Rescheduling Success!",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = blueColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Appointment successfully changed. You will receive a notification and the doctor you selected will contact you.",
                    fontSize = 14.sp,
                    color = Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it / 2 },
                            animationSpec = tween(300, delayMillis = 400)
                        ) + fadeIn(tween(300, delayMillis = 400)),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Button(
                            onClick = onViewAppointment,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = blueColor)
                        ) {
                            Text("View Appointment", fontWeight = FontWeight.SemiBold, color = Color.White)
                        }
                    }

                    AnimatedVisibility(
                        visible = true,
                        enter = slideInVertically(
                            initialOffsetY = { it },
                            animationSpec = tween(300, delayMillis = 500)
                        ) + fadeIn(tween(300, delayMillis = 500)),
                        exit = slideOutVertically() + fadeOut()
                    ) {
                        Button(
                            onClick = onDismiss,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(28.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = lightBlueBg)
                        ) {
                            Text("Cancel", fontWeight = FontWeight.SemiBold, color = blueColor)
                        }
                    }
                }
            }
        }
    }
}