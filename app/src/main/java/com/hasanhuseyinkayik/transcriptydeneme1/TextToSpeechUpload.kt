package com.hasanhuseyinkayik.transcriptydeneme1

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnInitListener
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.*

@Composable
fun TextToSpeechUploadScreen() {
    val context = LocalContext.current
    var userText by remember { mutableStateOf(TextFieldValue("")) }
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                val text = userText.text
                if (text.isBlank()) {
                    Toast.makeText(context, "Lütfen bir metin girin", Toast.LENGTH_SHORT).show()
                } else {
                    textToSpeech = TextToSpeech(context, OnInitListener { status ->
                        if (status == TextToSpeech.SUCCESS) {
                            val result = textToSpeech?.setLanguage(Locale("tr", "TR"))
                            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                                Toast.makeText(context, "Dil desteklenmiyor", Toast.LENGTH_SHORT).show()
                            } else {
                                textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, null)
                            }
                        } else {
                            Toast.makeText(context, "TTS başlatılamadı", Toast.LENGTH_SHORT).show()
                        }
                    })
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(70.dp)
        ) {
            Text(text = "Metni Sesli Oku", fontSize = 18.sp)
        }

        OutlinedTextField(
            value = userText,
            onValueChange = { userText = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = { Text("Bir metin yazın...") },
            maxLines = Int.MAX_VALUE
        )
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }
}
