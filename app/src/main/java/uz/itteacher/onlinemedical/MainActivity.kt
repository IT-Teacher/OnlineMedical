package uz.itteacher.onlinemedical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import uz.itteacher.onlinemedical.WelcomeScreen
import uz.itteacher.onlinemedical.FillProfileScreen
import uz.itteacher.onlinemedical.RegisterScreen
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {

            WelcomeScreen()
        }
    }
}
