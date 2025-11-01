package uz.itteacher.onlinemedical

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import uz.itteacher.onlinemedical.favourite.ui.FavoriteDoctorScreen
import uz.itteacher.onlinemedical.favourite.viewmodel.FavoriteViewModel
import uz.itteacher.onlinemedical.ui.theme.OnlineMedicalTheme
import kotlin.getValue

class MainActivity : ComponentActivity() {
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            OnlineMedicalTheme {
                MainApp(favoriteViewModel)

            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    OnlineMedicalTheme {
        Greeting("Android")
    }
}