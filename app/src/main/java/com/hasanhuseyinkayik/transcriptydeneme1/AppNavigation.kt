package com.hasanhuseyinkayik.transcriptydeneme1

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hasanhuseyinkayik.transcriptydeneme1.mainMenu.ImageToTextScreen
import com.hasanhuseyinkayik.transcriptydeneme1.mainMenu.MainScreen
import com.hasanhuseyinkayik.transcriptydeneme1.mainMenu.SpeechToText
import com.hasanhuseyinkayik.transcriptydeneme1.mainMenu.SpeechToTextScreen
import com.hasanhuseyinkayik.transcriptydeneme1.mainMenu.TextToSpeechFileUpload

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable("speech_to_text") {
            SpeechToTextScreen(navController)
        }
        composable("speech_to_text_record") {
            SpeechToText(navController)
        }
        composable("text_to_speech"){
            TextToSpeechFileUpload(navController = navController)
        }
        composable("image_to_text"){
            ImageToTextScreen()
        }
    }
}
