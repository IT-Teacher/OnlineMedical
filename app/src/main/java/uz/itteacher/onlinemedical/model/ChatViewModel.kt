// viewmodel/ChatViewModel.kt
package uz.itteacher.onlinemedical.model

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uz.itteacher.onlinemedical.screens.appointments.ChatMessage
import java.text.SimpleDateFormat
import java.util.*

class ChatViewModel : ViewModel() {
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("chats")
    val currentUserId = auth.currentUser?.uid ?: "patient_sample"
    private val doctorId = "doctor_jenny" // Will be dynamic later

    private val _messages = MutableStateFlow<List<ChatMessage>>(emptyList())
    val messages: StateFlow<List<ChatMessage>> = _messages

    private val chatId by lazy {
        val ids = listOf(currentUserId, doctorId).sorted()
        "${ids[0]}_${ids[1]}"
    }

    init {
        listenToMessages()
    }

    private fun listenToMessages() {
        db.child(chatId).addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val list = mutableListOf<ChatMessage>()
                for (child in snapshot.children) {
                    val msg = child.getValue(ChatMessage::class.java)
                    msg?.let { list.add(it) }
                }
                list.sortBy { it.timestamp }
                _messages.value = list
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    fun sendMessage(text: String) {
        val formatter = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = formatter.format(Date())
        val msg = ChatMessage(
            text = text,
            senderId = currentUserId,
            time = time,
            timestamp = System.currentTimeMillis()
        )
        val key = db.child(chatId).push().key ?: return
        db.child(chatId).child(key).setValue(msg)
    }
}