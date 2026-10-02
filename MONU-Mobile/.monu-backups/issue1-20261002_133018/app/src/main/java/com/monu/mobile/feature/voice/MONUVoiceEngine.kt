package com.monu.mobile.feature.voice

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale

class MONUVoiceEngine(
    context: Context,
    private val onReady: (Boolean) -> Unit = {}
) : TextToSpeech.OnInitListener {

    private val textToSpeech = TextToSpeech(context.applicationContext, this)

    private var initialized = false

    override fun onInit(status: Int) {
        initialized = status == TextToSpeech.SUCCESS

        if (initialized) {
            textToSpeech.language = Locale.getDefault()
            textToSpeech.setSpeechRate(1.0f)
            textToSpeech.setPitch(1.0f)
        }

        onReady(initialized)
    }

    fun speak(
        text: String,
        speechRate: Float = 1.0f,
        pitch: Float = 1.0f
    ) {
        if (!initialized || text.isBlank()) return

        textToSpeech.setSpeechRate(speechRate)
        textToSpeech.setPitch(pitch)

        textToSpeech.speak(
            text,
            TextToSpeech.QUEUE_FLUSH,
            null,
            "MONU_MESSAGE"
        )
    }

    fun stop() {
        if (initialized) {
            textToSpeech.stop()
        }
    }

    fun shutdown() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }
}
