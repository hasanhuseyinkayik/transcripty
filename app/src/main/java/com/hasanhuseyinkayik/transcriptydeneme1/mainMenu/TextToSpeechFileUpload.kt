package com.hasanhuseyinkayik.transcriptydeneme1.mainMenu

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.speech.tts.TextToSpeech
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

@Composable
fun TextToSpeechFileUpload(navController: NavHostController) {
    val context = LocalContext.current
    var textInput by remember { mutableStateOf("") }
    var textToSpeech by remember { mutableStateOf<TextToSpeech?>(null) }
    var selectedLanguage by remember { mutableStateOf(Locale.ENGLISH) }
    var selectedLanguageName by remember { mutableStateOf("İngilizce") }
    var expanded by remember { mutableStateOf(false) }
    val showErrorToast = remember { mutableStateOf(false) }

    val languageOptions = listOf(
        "İngilizce" to Locale.ENGLISH,
        "Almanca" to Locale.GERMAN,
        "Fransızca" to Locale.FRENCH,
        "Türkçe" to Locale("tr", "TR")
    )

    val launcher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            textInput = readTextFromUri(context, it) ?: "Dosya okunamadı."
        }
    }

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
        Toast.makeText(context, "Seslendirilecek bir yazı bulunamadı.", Toast.LENGTH_SHORT).show()
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
            text = "Metinden Sese Çevir",
            color = Color.Black,
            fontSize = 32.sp,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Button(
            onClick = {
                launcher.launch("text/plain") // Sadece .txt dosyaları seçilecek
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Text(text = "Dosya Seç", color = Color.White)
        }

        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = textInput,
            onValueChange = { textInput = it },
            label = { Text("Metin") },
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        // Dil Seçimi
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
            ) {
                Text(text = "Dil: " + selectedLanguageName)
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

        Spacer(modifier = Modifier.height(16.dp))

        // Seslendir Butonu
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
        ) {
            Text(text = "Metni Seslendir", color = Color.White, fontSize = 18.sp)
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            textToSpeech?.stop()
            textToSpeech?.shutdown()
        }
    }
}

fun readTextFromUri(context: Context, uri: Uri): String? {
    return try {
        context.contentResolver.openInputStream(uri)?.use { inputStream ->
            BufferedReader(InputStreamReader(inputStream)).use { reader ->
                reader.readText()
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
