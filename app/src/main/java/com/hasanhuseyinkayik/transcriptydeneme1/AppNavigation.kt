package com.hasanhuseyinkayik.transcriptydeneme1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("speech_to_text") {
            SpeechToTextScreen()
        }
        composable("text_to_speech"){
            TextToSpeechScreen(navController = navController)
        }
        composable("image_to_text"){
            ImageToTextScreen()
        }
        composable("text_to_speech_upload"){
            TextToSpeechUploadScreen()
        }
    }
}
