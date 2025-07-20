package com.hasanhuseyinkayik.transcriptydeneme1.mainMenu

import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
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
import androidx.navigation.NavHostController
import java.util.*
import android.content.pm.PackageManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.text.TextRange
import androidx.core.content.ContextCompat

@Composable
fun SpeechToText(navController: NavHostController) {
    val context = LocalContext.current
    val activity = context as? ComponentActivity
    var recognizedText by remember { mutableStateOf(TextFieldValue("")) }
    var isListening by remember { mutableStateOf(false) }
    var selectedLanguage by remember { mutableStateOf("tr") }
    var ttsReady by remember { mutableStateOf(false) }
    var tts by remember { mutableStateOf<TextToSpeech?>(null) }

    val languages = mapOf("Türkçe" to "tr", "İngilizce" to "en")

    val hasPermission = ContextCompat.checkSelfPermission(
        context,
        android.Manifest.permission.RECORD_AUDIO
    ) == PackageManager.PERMISSION_GRANTED

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (!isGranted) {
                Toast.makeText(context, "Mikrofon izni gerekli.", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val speechRecognizer = remember {
        SpeechRecognizer.createSpeechRecognizer(context)
    }

    LaunchedEffect(Unit) {
        tts = TextToSpeech(context) { status ->
            ttsReady = status == TextToSpeech.SUCCESS
        }
    }

    LaunchedEffect(selectedLanguage, ttsReady) {
        if (ttsReady && tts != null) {
            val locale = Locale(selectedLanguage, if (selectedLanguage == "tr") "TR" else "US")
            val result = tts!!.setLanguage(locale)
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(context, "Seçilen dil desteklenmiyor.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    DisposableEffect(Unit) {
        speechRecognizer.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(params: Bundle?) {}
            override fun onBeginningOfSpeech() {}
            override fun onRmsChanged(rmsdB: Float) {}
            override fun onBufferReceived(buffer: ByteArray?) {}
            override fun onEndOfSpeech() {
                isListening = false
            }

            override fun onError(error: Int) {
                isListening = false
                Toast.makeText(context, "Hata oluştu: $error", Toast.LENGTH_SHORT).show()
            }

            override fun onResults(results: Bundle?) {
                val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!matches.isNullOrEmpty()) {
                    val newText = recognizedText.text + " " + matches[0]
                    recognizedText = TextFieldValue(
                        text = newText,
                        selection = TextRange(newText.length)
                    )
                }
            }

            override fun onPartialResults(partialResults: Bundle?) {
                val partial = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (!partial.isNullOrEmpty()) {
                    val newText = recognizedText.text + " " + partial[0]
                    recognizedText = TextFieldValue(
                        text = newText,
                        selection = TextRange(newText.length)
                    )
                }
            }

            override fun onEvent(eventType: Int, params: Bundle?) {}
        })

        onDispose {
            speechRecognizer.destroy()
            tts?.stop()
            tts?.shutdown()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Sesten Metne Çevir",
            fontSize = 28.sp,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Dil: ", fontSize = 16.sp)
            Spacer(modifier = Modifier.width(8.dp))
            DropdownMenuBox(languages = languages, selectedLanguage = selectedLanguage) {
                selectedLanguage = it
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = recognizedText,
            onValueChange = { recognizedText = it },
            label = { Text("Konuşma Metni") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (!hasPermission) {
                    permissionLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
                } else {
                    if (!isListening) {
                        val locale = Locale(selectedLanguage, if (selectedLanguage == "tr") "TR" else "US")
                        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE, locale.toLanguageTag())
                            putExtra(RecognizerIntent.EXTRA_LANGUAGE_PREFERENCE, locale.toLanguageTag())
                            putExtra(RecognizerIntent.EXTRA_ONLY_RETURN_LANGUAGE_PREFERENCE, locale.toLanguageTag())
                        }

                        speechRecognizer.startListening(recognizerIntent)
                        isListening = true
                        Toast.makeText(context, "Dinleniyor...", Toast.LENGTH_SHORT).show()
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text(if (isListening) "Dinleniyor..." else "Konuşmayı Başlat")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val textToRead = recognizedText.text
                if (textToRead.isNotBlank()) {
                    if (ttsReady && tts != null) {
                        tts!!.speak(textToRead, TextToSpeech.QUEUE_FLUSH, null, null)
                    }
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
        ) {
            Text("Metni Sesli Oku")
        }
    }
}

@Composable
fun DropdownMenuBox(
    languages: Map<String, String>,
    selectedLanguage: String,
    onLanguageSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box {
        Button(onClick = { expanded = true }) {
            val selectedLabel = languages.entries.firstOrNull { it.value == selectedLanguage }?.key ?: "Dil seç"
            Text(selectedLabel)
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            languages.forEach { (label, code) ->
                DropdownMenuItem(
                    text = { Text(label) },
                    onClick = {
                        onLanguageSelected(code)
                        expanded = false
                    }
                )
            }
        }
    }
}

