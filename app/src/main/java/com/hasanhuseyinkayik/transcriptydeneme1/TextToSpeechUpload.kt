package com.hasanhuseyinkayik.transcriptydeneme1

import android.speech.tts.TextToSpeech
import android.speech.tts.Voice
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

    var pitch by remember { mutableFloatStateOf(1.0f) }
    var speechRate by remember { mutableFloatStateOf(1.0f) }

    var availableVoices by remember { mutableStateOf<List<Voice>>(emptyList()) }
    var selectedVoice by remember { mutableStateOf<Voice?>(null) }

    var expanded by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech?.language = Locale("tr", "TR")
                val voices = textToSpeech?.voices?.filter {
                    it.locale.language == "tr"
                } ?: emptyList()
                availableVoices = voices
                selectedVoice = voices.firstOrNull()
            } else {
                Toast.makeText(context, "TTS başlatılamadı", Toast.LENGTH_SHORT).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        OutlinedTextField(
            value = userText,
            onValueChange = { userText = it },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            placeholder = { Text("Bir metin yazın...") },
            maxLines = Int.MAX_VALUE
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text("Ses Hızı: %.1f".format(speechRate))
        Slider(
            value = speechRate,
            onValueChange = { speechRate = it },
            valueRange = 0.5f..2f,
            steps = 5
        )

        Text("Ses Tonu: %.1f".format(pitch))
        Slider(
            value = pitch,
            onValueChange = { pitch = it },
            valueRange = 0.5f..2f,
            steps = 5
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (availableVoices.isNotEmpty()) {
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(onClick = { expanded = true }, modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = selectedVoice?.let { voiceLabel(it) }
                            ?: "Ses Seç"
                    )
                }
                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    availableVoices.forEachIndexed { index, voice ->
                        DropdownMenuItem(
                            text = { Text("${voiceLabel(voice)} (${index + 1})") },
                            onClick = {
                                selectedVoice = voice
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = {
                val text = userText.text
                if (text.isBlank()) {
                    Toast.makeText(context, "Lütfen bir metin girin", Toast.LENGTH_SHORT).show()
                } else {
                    textToSpeech?.apply {
                        setPitch(pitch)
                        setSpeechRate(speechRate)
                        selectedVoice?.let { voice = it }
                        speak(text, TextToSpeech.QUEUE_FLUSH, null, "utteranceId")
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .height(70.dp)
        ) {
            Text(text = "Metni Sesli Oku", fontSize = 18.sp)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }
}

fun voiceLabel(voice: Voice): String {
    val gender = when {
        voice.name.contains("cfs", true) || voice.name.contains("female", true) -> "Kadın"
        voice.name.contains("cfc", true) || voice.name.contains("male", true) -> "Erkek"
        else -> "Bilinmeyen"
    }

    val language = voice.locale.displayLanguage

    return "$language"
}
