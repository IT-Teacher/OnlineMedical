package uz.itteacher.onlinemedical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.google.firebase.Firebase
import com.google.firebase.database.database
import uz.itteacher.onlinemedical.screens.appointments.AppointmentsScreen
import uz.itteacher.onlinemedical.ui.theme.OnlineMedicalTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Firebase.database.setPersistenceEnabled(true)

        enableEdgeToEdge()
        setContent {
            OnlineMedicalTheme {
                AppointmentsScreen()
            }
        }
    }
}