// screens/appointments/ChatScreen.kt
package uz.itteacher.onlinemedical.screens.appointments

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import uz.itteacher.onlinemedical.R
import uz.itteacher.onlinemedical.model.AppointmentData
import uz.itteacher.onlinemedical.model.ChatViewModel
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ChatScreen(navController: NavHostController) {
    val viewModel: ChatViewModel = viewModel()
    val messages by viewModel.messages.collectAsState()
    val appointment = navController.previousBackStackEntry
        ?.savedStateHandle
        ?.get<AppointmentData>("appointment") ?: AppointmentData(doctorName = "Doctor")

    val listState = rememberLazyListState()
    var text by remember { mutableStateOf("") }

    // Auto-scroll to bottom
    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    Column(modifier = Modifier.fillMaxSize().background(Color(0xFFF5F5F5))) {
        // Top Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, "Back", tint = Color.Black)
            }
            AsyncImage(
                model = appointment.doctorPhotoUrl,
                contentDescription = "Doctor",
                placeholder = painterResource(R.drawable.ic_launcher_foreground),
                modifier = Modifier.size(44.dp).clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Spacer(Modifier.width(12.dp))
            Column {
                Text(appointment.doctorName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                Text("Typically replies in minutes", color = Color.Green, fontSize = 12.sp)
            }
            Spacer(Modifier.weight(1f))
            Icon(painterResource(R.drawable.more), "More", tint = Color.Black, modifier = Modifier.size(24.dp))
        }

        Divider(color = Color(0xFFE0E0E0))

        // Messages List
        LazyColumn(
            state = listState,
            modifier = Modifier.weight(1f).padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            item {
                Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Text(
                        "Session Start",
                        color = Color.Gray,
                        fontSize = 12.sp,
                        modifier = Modifier
                            .background(Color(0xFFE3F2FD), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    )
                }
                Spacer(Modifier.height(16.dp))
            }

            items(messages) { msg ->
                ChatBubble(
                    message = msg,
                    isFromMe = msg.senderId == viewModel.currentUserId
                )
            }
        }

        // Input Field
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextField(
                value = text,
                onValueChange = { text = it },
                placeholder = { Text("Type a message...", color = Color.Gray) },
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, Color.LightGray, RoundedCornerShape(24.dp))
                    .padding(horizontal = 16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                singleLine = false,
                maxLines = 4
            )

            Spacer(Modifier.width(8.dp))

            FloatingActionButton(
                onClick = {
                    if (text.trim().isNotEmpty()) {
                        viewModel.sendMessage(text.trim())
                        text = ""
                    }
                },
                containerColor = Color(0xFF007BFF),
                contentColor = Color.White,
                modifier = Modifier.size(52.dp)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Send")
            }
        }
    }
}

@Composable
fun ChatBubble(message: ChatMessage, isFromMe: Boolean) {
    val bubbleColor = if (isFromMe) Color(0xFF007BFF) else Color(0xFFE0E0E0)
    val textColor = if (isFromMe) Color.White else Color.Black
    val alignment = if (isFromMe) Alignment.End else Alignment.Start

    Column(
        horizontalAlignment = alignment,
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, RoundedCornerShape(16.dp))
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .widthIn(max = 300.dp)
        ) {
            Text(
                text = message.text,
                color = textColor,
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
        }
        Text(
            text = message.time,
            color = Color.Gray,
            fontSize = 10.sp,
            modifier = Modifier.padding(
                start = if (isFromMe) 0.dp else 8.dp,
                end = if (isFromMe) 8.dp else 0.dp,
                top = 2.dp
            )
        )
    }
}

data class ChatMessage(
    val text: String = "",
    val senderId: String = "",
    val time: String = "",
    val timestamp: Long = System.currentTimeMillis()
)