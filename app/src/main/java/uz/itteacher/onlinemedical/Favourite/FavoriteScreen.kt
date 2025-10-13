package uz.itteacher.onlinemedical.Favourite

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import uz.itteacher.onlinemedical.Favourite.Doctor
import uz.itteacher.onlinemedical.Favourite.DoctorRepository
import uz.itteacher.onlinemedical.Favourite.viewModel

val viewModel: FavoriteViewModel = viewModel()

fun viewModel(): FavoriteViewModel {
    return TODO("Provide the return value")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: FavoriteViewModel = viewModel()
) {
    val favorites by viewModel.favorites.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    var selectedFilter by remember { mutableStateOf("All") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Favorite Doctor") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = null)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.Search, contentDescription = null)
                    }
                }
            )
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding)) {
            // 🔹 Filter buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                listOf("All", "General", "Dentist", "Nutritionist").forEach { filter ->
                    FilterChip(
                        selected = selectedFilter == filter,
                        onClick = { selectedFilter = filter },
                        label = { Text(filter) }
                    )
                }
            }

            // 🔹 Doctor list
            LazyColumn {
                items(favorites) { doctor ->
                    DoctorCard(
                        doctor = doctor,
                        onRemove = { viewModel.onRemoveClick(doctor) }
                    )
                }
            }
        }

        // 🔹 Remove Dialog
        if (showDialog != null) {
            RemoveFavoriteDialog(
                doctor = showDialog!!,
                onCancel = { viewModel.cancelDialog() },
                onConfirm = { viewModel.confirmRemove(showDialog!!) }
            )
        }
    }
}
