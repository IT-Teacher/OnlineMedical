package uz.itteacher.onlinemedical

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.request.ImageRequest

@OptIn(ExperimentalMaterial3Api::class)


@Composable
fun HomeScreen(
    viewModel: DoctorViewModel = viewModel(),
    onSearchClick: () -> Unit = {}
) {
    val doctors by viewModel.doctors.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var selectedSpeciality by remember { mutableStateOf("All") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Good Morning 👋", fontSize = 14.sp, color = Color.Gray)
                        Text("Andrew Ainsley", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    }
                },
                actions = {
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.notif),
                            modifier = Modifier.size(22.dp),
                            contentDescription = "Notifications"
                        )
                    }
                    IconButton(onClick = {}) {
                        Icon(
                            painter = painterResource(R.drawable.favoutite),
                            modifier = Modifier.size(22.dp),
                            contentDescription = "Favorites"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8F9FD))
        ) {
            when {
                isLoading -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    CircularProgressIndicator()
                }

                error != null -> Box(Modifier.fillMaxSize(), Alignment.Center) {
                    Text("Error: $error")
                }

                else -> {
                    LazyColumn(contentPadding = PaddingValues(bottom = 80.dp)) {
                        item {
                            // 🔹 Search Bar
                            SearchBar(
                                value = searchQuery,
                                onClick = onSearchClick
                            )

                            // 🔹 Banner
                            Image(
                                painter = painterResource(R.drawable.banner),
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .padding(16.dp)
                                    .clip(MaterialTheme.shapes.medium),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(Modifier.height(8.dp))
                            Text(
                                text = "Doctor Speciality",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                            Spacer(Modifier.height(8.dp))

                            // 🔹 Speciality Row — tanlansa filterlanadi
                            DoctorSpecialityRow { speciality ->
                                selectedSpeciality = speciality
                            }

                            Spacer(Modifier.height(12.dp))
                            Text(
                                text = "Top Doctors",
                                fontWeight = FontWeight.Bold,
                                fontSize = 18.sp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                            )
                        }

                        // 🔹 Search yoki Speciality bo‘yicha filter
                        val filteredDoctors = doctors.filter { doctor ->
                            val matchesSearch = doctor.name.contains(searchQuery, true) ||
                                    doctor.speciality.contains(searchQuery, true)
                            val matchesSpeciality =
                                selectedSpeciality == "All" || doctor.speciality.contains(selectedSpeciality, true)
                            matchesSearch && matchesSpeciality
                        }

                        items(filteredDoctors) { doctor ->
                            DoctorCard(doctor)
                        }
                    }
                }
            }
        }
    }
}





@Composable
fun SearchBar(
    value: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(Color.White)
            .clickable { onClick() } // 🔹 faqat bu joy bosilganda navController.navigate("search")
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                painter = painterResource(R.drawable.search),
                contentDescription = "Search",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = if (value.isEmpty()) "Search doctor or speciality" else value,
                color = if (value.isEmpty()) Color.Gray else Color.Black
            )
        }
    }
}



@Composable
fun SpecialityItem(title: String, iconRes: Int, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Surface(
            shape = CircleShape,
            color = Color(0xFFEFF4FF),
            modifier = Modifier.size(60.dp)
        ) {
            Icon(
                painter = painterResource(iconRes),
                contentDescription = title,
                tint = Color(0xFF3371FF),
                modifier = Modifier.padding(16.dp)
            )
        }
        Spacer(Modifier.height(6.dp))
        Text(title, fontSize = 13.sp, color = Color.Black)
    }
}
@Composable
fun DoctorSpecialityRow(onSpecialityClick: (String) -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SpecialityItem("General", R.drawable.group) { onSpecialityClick("General") }
        SpecialityItem("Dentist", R.drawable.tooth) { onSpecialityClick("Dentist") }
        SpecialityItem("Nutrition", R.drawable.fruit) { onSpecialityClick("Nutrition") }
        SpecialityItem("Ophthalm.", R.drawable.eye) { onSpecialityClick("Ophthalm.") }
    }

    Spacer(modifier = Modifier.height(20.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        SpecialityItem("Neurology", R.drawable.brain) { onSpecialityClick("Neurology") }
        SpecialityItem("Pediatric", R.drawable.girl) { onSpecialityClick("Pediatric") }
        SpecialityItem("Radiology", R.drawable.sklet) { onSpecialityClick("Radiology") }
        SpecialityItem("All", R.drawable.more) { onSpecialityClick("All") }
    }
}






@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: DoctorViewModel = viewModel(),
    onBack: () -> Unit
) {
    val doctors by viewModel.doctors.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val filteredDoctors = doctors.filter { doctor ->
        (selectedCategory == "All" || doctor.speciality.contains(selectedCategory, true)) &&
                (doctor.name.contains(searchQuery, true) || doctor.speciality.contains(searchQuery, true))
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(end = 12.dp)
                    ) {
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            placeholder = { Text("Search doctors...", color = Color.Gray) },
                            singleLine = true,
                            shape = CircleShape,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .shadow(4.dp, CircleShape),
                            leadingIcon = {
                                Icon(
                                    painter = painterResource(R.drawable.search,),
                                    contentDescription = null,
                                    tint = Color.Gray,
                                    modifier = Modifier.size(20.dp)
                                )
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                unfocusedBorderColor = Color.Transparent,
                                focusedBorderColor = Color(0xFF3371FF),
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            painter = painterResource(R.drawable.back),
                            contentDescription = "Back",
                            tint = Color(0xFF3371FF),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color(0xFFF8F9FD))
            )
        },
        containerColor = Color(0xFFF8F9FD)
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF8F9FD))
                .padding(padding)
        ) {
            // 🔹 Gorizontal Filter bo‘lim
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                val categories = listOf(
                    "All", "General", "Dentist", "Ophthalm.", "Nutrition",
                    "Neurology", "Pediatric", "Radiology"
                )
                items(categories) { category ->
                    FilterChip(
                        selected = selectedCategory == category,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
                                fontSize = 13.sp,
                                fontWeight = if (selectedCategory == category) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(0xFF3371FF),
                            selectedLabelColor = Color.White,
                            containerColor = Color.White,
                            labelColor = Color.Black
                        ),
                        elevation = FilterChipDefaults.filterChipElevation(4.dp)
                    )
                }
            }

            // 🔹 Doctorlar ro‘yxati
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                if (filteredDoctors.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(top = 100.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No doctors found 😕", color = Color.Gray)
                        }
                    }
                } else {
                    items(filteredDoctors) { doctor ->
                        DoctorCard(doctor)
                    }
                }
            }
        }
    }
}




@Composable
fun BottomNavigationBar(navController: NavHostController, currentRoute: String) {
    NavigationBar(containerColor = Color.White) {

        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = { navController.navigate("home") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.home),
                    contentDescription = "Home",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Home", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = currentRoute == "appointments",
            onClick = { navController.navigate("appointments") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.calendar),
                    contentDescription = "Appointments",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Appointment", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = currentRoute == "history",
            onClick = { navController.navigate("history") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.history),
                    contentDescription = "History",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("History", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = currentRoute == "articles",
            onClick = { navController.navigate("articles") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.newspaper),
                    contentDescription = "Articles",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Articles", fontSize = 10.sp) }
        )

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { navController.navigate("profile") },
            icon = {
                Icon(
                    painter = painterResource(R.drawable.user),
                    contentDescription = "Profile",
                    modifier = Modifier.size(22.dp)
                )
            },
            label = { Text("Profile", fontSize = 10.sp) }
        )
    }
}



@Composable
fun DoctorCard(doctor: Doctor) {
    val ctx = LocalContext.current
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 6.dp),
        elevation = CardDefaults.cardElevation(6.dp),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = ImageRequest.Builder(ctx)
                    .data(doctor.imageUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = doctor.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                Text(doctor.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Text(doctor.speciality, color = MaterialTheme.colorScheme.primary)
                Text("🏥 ${doctor.hospital}", fontSize = 13.sp)
                Text("📍 ${doctor.location}", fontSize = 13.sp)
                Text("⭐ ${doctor.rating} (${doctor.reviews} reviews)", fontSize = 13.sp)
            }
        }
    }
}
@Composable
fun MainApp() {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route ?: "home"

    Scaffold(
        bottomBar = {
            if (currentRoute in listOf("home", "appointments", "history", "articles", "profile")) {
                BottomNavigationBar(navController, currentRoute)
            }
        }
    ) { padding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(padding)
        ) {
            composable("home") {
                HomeScreen(
                    viewModel = viewModel(),
                    onSearchClick = { navController.navigate("search") }
                )
            }
            composable("search") {
                SearchScreen(
                    viewModel = viewModel(),
                    onBack = { navController.popBackStack() }
                )
            }
            composable("appointments") { Text("Appointments Screen", color = Color.Black) }
            composable("history") { Text("History Screen", color = Color.Black) }
            composable("articles") { Text("Articles Screen", color = Color.Black) }
            composable("profile") { Text("Profile Screen", color = Color.Black) }
        }
    }
}


