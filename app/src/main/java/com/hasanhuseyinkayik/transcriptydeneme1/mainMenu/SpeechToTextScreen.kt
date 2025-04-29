package com.hasanhuseyinkayik.transcriptydeneme1.mainMenu

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavHostController
import com.hasanhuseyinkayik.transcriptydeneme1.R
import com.hasanhuseyinkayik.transcriptydeneme1.ui.theme.TranscriptyDeneme1Theme

@Composable
fun SpeechToTextScreen(navController: NavHostController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sesten Metne Çevir",
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 48.dp)
        )

        Button(
            onClick = { /* Dosya Yükleme işlemi */ },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(100.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.file),
                    contentDescription = "Dosya Yükle",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Dosya Yükle",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Button(
            onClick = { navController.navigate("speech_to_text_record") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(100.dp)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.mic),
                    contentDescription = "Ses Kaydet",
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
                Text(
                    text = "Ses Kaydet",
                    color = Color.White,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

