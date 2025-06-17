package com.hasanhuseyinkayik.transcriptydeneme1.mainMenu

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.net.Uri
import android.os.ParcelFileDescriptor
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
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
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
            val mimeType = context.contentResolver.getType(it)
            when (mimeType) {
                "text/plain" -> {
                    textInput = readTextFromUri(context, it) ?: "Dosya okunamadı."
                }
                "application/pdf" -> {
                    readTextFromPdf(context, it) { result ->
                        textInput = result ?: "PDF'den metin alınamadı."
                    }
                }
                else -> {
                    Toast.makeText(context, "Sadece .txt veya .pdf dosyaları destekleniyor.", Toast.LENGTH_SHORT).show()
                }
            }
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
                    val installIntent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
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
                launcher.launch("*/*") // hem .txt hem .pdf seçilebilir
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
                Text(text = "Dil: $selectedLanguageName")
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
                                val installIntent = Intent(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA)
                                context.startActivity(installIntent)
                            }
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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

fun readTextFromPdf(context: Context, uri: Uri, onTextExtracted: (String?) -> Unit) {
    try {
        val parcelFileDescriptor = context.contentResolver.openFileDescriptor(uri, "r") ?: return
        val renderer = PdfRenderer(parcelFileDescriptor)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        val allTexts = mutableListOf<String>()
        var processedPages = 0

        for (i in 0 until renderer.pageCount) {
            val page = renderer.openPage(i)
            val bitmap = Bitmap.createBitmap(page.width, page.height, Bitmap.Config.ARGB_8888)
            page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
            page.close()

            val image = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { visionText ->
                    allTexts.add(visionText.text)
                    processedPages++
                    if (processedPages == renderer.pageCount) {
                        onTextExtracted(allTexts.joinToString("\n\n"))
                        renderer.close()
                        parcelFileDescriptor.close()
                    }
                }
                .addOnFailureListener {
                    processedPages++
                    if (processedPages == renderer.pageCount) {
                        onTextExtracted(allTexts.joinToString("\n\n"))
                        renderer.close()
                        parcelFileDescriptor.close()
                    }
                }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        onTextExtracted(null)
    }
}

