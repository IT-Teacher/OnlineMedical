package com.example.appointment


import android.R.attr.type
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appointment.ui.theme.AppointmentTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppointmentTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()
                    val bookingViewModel: BookingViewModel = viewModel()
                    NavHost(navController = navController, startDestination = "profile") {
                        composable("profile") {
                            val sampleProfile = DoctorProfile(
                                name = "Dr. Jenny Watson",
                                specialty = "Immunologists",
                                hospital = "Christ Hospital in London, UK",
                                patients = "5,000+",
                                experience = "10+",
                                rating = "4.8",
                                reviewsCount = "4,942",
                                about = "Dr. Jenny Watson is the top most Immunologists specialist in Christ Hospital at London. She achieved several awards for her wonderful contribution in medical field. She is available for private consultation, view more.",
                                workingTime = "Monday - Friday, 08.00 AM - 20.00 PM",
                                reviews = listOf(
                                    Review(
                                        name = "Charolette Hanlin",
                                        avatarRes = R.drawable.charolette,
                                        rating = 5,
                                        text = "Dr. Jenny is very professional in her work and responsive. I have consulted and my problem is solved. 😍😍",
                                        likes = 938,
                                        daysAgo = "6 days ago"
                                    ),
                                    Review(
                                        name = "Darron Kulikowski",
                                        avatarRes = R.drawable.darron,
                                        rating = 4,
                                        text = "The doctor is very beautiful and the service is excellent!! I like it and want to consult again 😂😂",
                                        likes = 863,
                                        daysAgo = "6 days ago"
                                    ),

                                )
                            )
                            DoctorProfileScreen(navController = navController, profile = sampleProfile)
                        }
                        composable("reviews") {

                            val sampleReviews = listOf(
                                Review(
                                    name = "Charolette Hanlin",
                                    avatarRes = R.drawable.charolette,
                                    rating = 5,
                                    text = "Dr. Jenny is very professional in her work and responsive. I have consulted and my problem is solved. 😍😍",
                                    likes = 938,
                                    daysAgo = "6 days ago"
                                ),
                                Review(
                                    name = "Darron Kulikowski",
                                    avatarRes = R.drawable.darron,
                                    rating = 4,
                                    text = "The doctor is very beautiful and the service is excellent!! I like it and want to consult again 😂😂",
                                    likes = 863,
                                    daysAgo = "6 days ago"
                                ),
                                Review(
                                    name = "Lauralee Quintero",
                                    avatarRes = R.drawable.laurelle,
                                    rating = 4,
                                    text = "Doctors who are very skilled and fast in service. I highly recommend Dr. Jenny for all who want to consult👍👍",
                                    likes = 629,
                                    daysAgo = "6 days ago"
                                ),
                                Review(
                                    name = "Aileen Fullbright",
                                    avatarRes = R.drawable.aileen,
                                    rating = 5,
                                    text = "Doctors who are very skilled and fast in service. My illness is cured, thank you very much!😊",
                                    likes = 553,
                                    daysAgo = "6 days ago"
                                ),
                                Review(
                                    name = "Lauralee Quintero",
                                    avatarRes = R.drawable.rodolfo,
                                    rating = 4,
                                    text = "Dr. Jenny is very professional in her work and responsive. I have consulted and my problem is solved👏",
                                    likes = 748,
                                    daysAgo = "6 days ago"
                                )
                            )
                            ReviewsScreen(reviews = sampleReviews, navController = navController)
                        }

                        composable("book_appointment") {
                            BookAppointmentScreen(navController = navController,
                                viewModel = bookingViewModel)
                        }

                        composable("select_package") {
                            SelectPackageScreen(navController = navController,
                                viewModel = bookingViewModel)
                        }

                        composable("patient_details") {
                            PatientDetailsScreen(navController = navController,
                                viewModel = bookingViewModel)
                        }

                        composable("payment_options") {
                            PaymentScreen(navController = navController,
                                viewModel = bookingViewModel)
                        }

                        composable("review_summary") {
                            ReviewSummaryScreen(
                                navController = navController,
                                viewModel = bookingViewModel
                            )
                        }

                        composable(
                            route = "add_card/{cardId}",
                            arguments = listOf(navArgument("cardId") {
                                type = NavType.StringType
                                nullable = true
                                defaultValue = null
                            })
                        ) { backStackEntry ->
                            val cardId = backStackEntry.arguments?.getString("cardId")
                            AddNewCardScreen(
                                navController = navController,
                                viewModel = bookingViewModel,
                                cardId = cardId
                            )
                        }

                        composable("add_card") {
                            AddNewCardScreen(
                                navController = navController,
                                viewModel = bookingViewModel,
                                cardId = null
                            )
                        }
                    }
                }
            }
        }
    }
}