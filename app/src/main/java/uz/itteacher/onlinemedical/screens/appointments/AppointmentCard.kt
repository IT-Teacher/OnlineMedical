// AppointmentCard.kt
package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import uz.itteacher.onlinemedical.R
import uz.itteacher.onlinemedical.model.AppointmentData

@Composable
fun AppointmentCard(appointment: AppointmentData, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .heightIn(min = 100.dp, max = 250.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = appointment.doctorPhotoUrl,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground)
                    ),
                    contentDescription = "Doctor",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appointment.doctorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${appointment.packageType}     -     ",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Button(
                            border = BorderStroke(1.dp, Color(0xFF007BFF)),
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF).copy(alpha = 0f), contentColor = Color(0xFF007BFF)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text("Upcoming", fontSize = 12.sp, maxLines = 1)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "Message",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("appointment", appointment)
                                    navController.navigate("messaging_appointment")
                                }
                        )
                    }
                    Text(
                        text = "${appointment.date} | ${appointment.time.split(" – ")[0]}",
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    border = BorderStroke(1.dp, Color(0xFF007BFF)),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF007BFF)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(40.dp)
                        .defaultMinSize(minWidth = 120.dp)
                ) {
                    Text("Cancel Appointment", fontSize = 14.sp, maxLines = 1)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF), contentColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .height(40.dp)
                        .defaultMinSize(minWidth = 100.dp)
                ) {
                    Text("Reschedule", fontSize = 14.sp, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun CompletedAppointmentCard(appointment: AppointmentData, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .heightIn(min = 100.dp, max = 250.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = appointment.doctorPhotoUrl,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground)
                    ),
                    contentDescription = "Doctor",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appointment.doctorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${appointment.packageType}     -     ",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Button(
                            border = BorderStroke(1.dp, Color(0xFF28A745)),
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF28A745)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text("Completed", fontSize = 12.sp, maxLines = 1)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.message),
                            contentDescription = "Message",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    navController.currentBackStackEntry?.savedStateHandle?.set("appointment", appointment)
                                    navController.navigate("messaging_appointment") {
                                        popUpTo(navController.graph.startDestinationId)
                                        launchSingleTop = true
                                    }
                                }
                        )
                    }
                    Text(
                        text = "${appointment.date} | ${appointment.time.split(" – ")[0]}",
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    border = BorderStroke(1.dp, Color(0xFF007BFF)),
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFF007BFF)),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .height(40.dp)
                        .defaultMinSize(minWidth = 120.dp)
                ) {
                    Text("Book Again", fontSize = 14.sp, maxLines = 1)
                }
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF007BFF), contentColor = Color.White),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier
                        .height(40.dp)
                        .defaultMinSize(minWidth = 100.dp)
                ) {
                    Text("Leave a review", fontSize = 14.sp, maxLines = 1)
                }
            }
        }
    }
}

@Composable
fun CanceledAppointmentCard(appointment: AppointmentData, navController: NavHostController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
            .heightIn(min = 100.dp, max = 250.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        model = appointment.doctorPhotoUrl,
                        placeholder = painterResource(R.drawable.ic_launcher_foreground),
                        error = painterResource(R.drawable.ic_launcher_foreground)
                    ),
                    contentDescription = "Doctor",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = appointment.doctorName,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${appointment.packageType}     -     ",
                            color = Color.DarkGray,
                            fontSize = 14.sp,
                            maxLines = 1
                        )
                        Button(
                            border = BorderStroke(1.dp, Color(0xFFFF0000)),
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent, contentColor = Color(0xFFFF0000)),
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .height(36.dp)
                                .padding(horizontal = 4.dp)
                        ) {
                            Text("Canceled", fontSize = 12.sp, maxLines = 1)
                        }
                        Spacer(modifier = Modifier.width(20.dp))
                        Image(
                            painter = painterResource(id = R.drawable.call),
                            contentDescription = "Call",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Text(
                        text = "${appointment.date} | ${appointment.time.split(" – ")[0]}",
                        color = Color.DarkGray,
                        fontSize = 13.sp,
                        maxLines = 1
                    )
                }
            }
        }
    }
}