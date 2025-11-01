package uz.itteacher.onlinemedical.favourite.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import uz.itteacher.onlinemedical.Doctor

@Composable
fun RemoveFavoriteDialog(
    doctor: Doctor,
    onCancel: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        confirmButton = { Button(onClick = onConfirm) { Text("Yes, Remove") } },
        dismissButton = { OutlinedButton(onClick = onCancel) { Text("Cancel") } },
        title = { Text("Remove from Favorites?") },
        text = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = doctor.imageUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp)
                        .clip(CircleShape)
                )
                Spacer(Modifier.width(8.dp))
                Column {
                    Text(doctor.name, fontWeight = FontWeight.Bold)
                    Text("${doctor.speciality} | ${doctor.hospital}", fontSize = 12.sp)
                }
            }
        }
    )
}
