package com.hasanhuseyinkayik.transcriptydeneme1

import android.content.Intent
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.util.*

@Composable
fun TextToSpeechScreen(navController: NavHostController) {
    val context = LocalContext.current

    var textInput by remember { mutableStateOf("") }
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }
    var selectedLanguage by remember { mutableStateOf(Locale.ENGLISH) }
    val showErrorToast = remember { mutableStateOf(false) }

    val languageOptions = listOf(
        "İngilizce" to Locale.ENGLISH,
        "Almanca" to Locale.GERMAN,
        "Fransızca" to Locale.FRENCH,
        "Türkçe" to Locale("tr", "TR")
    )

    LaunchedEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status != TextToSpeech.SUCCESS) {
                Toast.makeText(context, "TextToSpeech başlatılamadı.", Toast.LENGTH_SHORT).show()
            } else {
                val result = textToSpeech?.setLanguage(selectedLanguage)
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Toast.makeText(context, "Dil desteklenmiyor. Lütfen dil paketini yükleyin.", Toast.LENGTH_LONG).show()
                    val installIntent = Intent()
                    installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                    context.startActivity(installIntent)
                }
            }
        }
    }

    if (showErrorToast.value) {
        Toast.makeText(context, "Lütfen bir yazı girin.", Toast.LENGTH_SHORT).show()
        showErrorToast.value = false
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Metni Seslendir",
            color = Color.Black,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        var expanded by remember { mutableStateOf(false) }
        var selectedLanguageName by remember { mutableStateOf(languageOptions.first().first) }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        ) {
            Button(
                onClick = { expanded = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(0.dp)
            ) {
                Text(text = selectedLanguageName)
            }
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                languageOptions.forEach { (name, locale) ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            selectedLanguage = locale
                            selectedLanguageName = name
                            expanded = false
                            val result = textToSpeech?.setLanguage(locale)
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Toast.makeText(context, "$name dili desteklenmiyor. Lütfen dil paketini yükleyin.", Toast.LENGTH_LONG).show()
                                val installIntent = Intent()
                                installIntent.action = TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA
                                context.startActivity(installIntent)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Yazınızı buraya girin") },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                if (textInput.isNotBlank()) {
                    textToSpeech?.speak(textInput, TextToSpeech.QUEUE_FLUSH, null, null)
                } else {
                    showErrorToast.value = true
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(vertical = 8.dp)
        ) {
            Text(
                text = "Yazıyı Seslendir",
                color = Color.White,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }
}
