package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import uz.itteacher.onlinemedical.R

@Composable
fun ConsultationEndedScreen(
    onBackToHome: () -> Unit,
    onLeaveReview: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8F9FA))
            .verticalScroll(rememberScrollState())
            .statusBarsPadding()
    ) {
        IconButton(onClick = onBackToHome, modifier = Modifier.padding(16.dp)) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back", tint = Color.Black)
        }

        Spacer(Modifier.height(40.dp))

        Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
            Box(contentAlignment = Alignment.Center) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .background(Color(0xFF3366FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(60.dp)
                    )
                }


                Box(
                    modifier = Modifier
                        .offset(x = 50.dp, y = (-60).dp)
                        .size(12.dp)
                        .background(Color(0xFF6699FF), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .offset(x = (-80).dp, y = -20.dp)
                        .size(7.dp)
                        .background(Color(0xFF6699FF), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .offset(x = 80.dp, y = 0.dp)
                        .size(10.dp)
                        .background(Color(0xFF6699FF), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .offset(x = (-45).dp, y = 60.dp)
                        .size(8.dp)
                        .background(Color(0xFF6699FF), CircleShape)
                )

                Box(
                    modifier = Modifier
                        .offset(x = -40.dp, y = (-80).dp)
                        .size(25.dp)
                        .background(Color(0xFF6699FF), CircleShape)
                )
                Box(
                    modifier = Modifier
                        .offset(x = 50.dp, y = 70.dp)
                        .size(5.dp)
                        .background(Color(0xFF6699FF), CircleShape)
                )
            }
        }

        Spacer(Modifier.height(32.dp))

        Text(
            "The consultation session has ended.",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Color.Black,
            modifier = Modifier.padding(horizontal = 32.dp),
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(8.dp))
        Text(
            "Recordings have been saved in activity.",
            fontSize = 16.sp,
            color = Color(0xFF666666),
            modifier = Modifier.padding(horizontal = 58.dp),
            textAlign = TextAlign.Center
        )

        Spacer(Modifier.height(40.dp))
        HorizontalDivider()

        Spacer(Modifier.height(32.dp))

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
            Image(
                painter = painterResource(R.drawable.ic_launcher_foreground), // Replace with actual doctor image
                contentDescription = "Doctor",
                modifier = Modifier.size(100.dp).clip(CircleShape)
            )
            Spacer(Modifier.height(16.dp))
            Text("Dr. Maria Foose", fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Text("Dermatologists", color = Color(0xFF666666))
            Text("The Venus Hospital in Paris, France", color = Color(0xFF666666))
        }

        Spacer(Modifier.height(48.dp))

        Row(modifier = Modifier.padding(horizontal = 32.dp), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            OutlinedButton(
                onClick = onBackToHome,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3366FF))
            ) { Text("Back to Home") }

            Button(
                onClick = onLeaveReview,
                modifier = Modifier.weight(1f).height(50.dp),
                shape = RoundedCornerShape(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF3366FF))
            ) { Text("Leave a Review", color = Color.White) }
        }

        Spacer(Modifier.height(32.dp))
    }
}