package com.monu.mobile.feature.voice

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.core.content.ContextCompat

class MONUVoiceInputEngine(
    context: Context,
    private val onResult: (String) -> Unit,
    private val onError: (String) -> Unit = {},
    private val onListeningStateChanged: (Boolean) -> Unit = {}
) : RecognitionListener {

    private val appContext = context.applicationContext

    private var speechRecognizer: SpeechRecognizer? = null

    private var listening = false

    private fun updateListeningState(value: Boolean) {
        listening = value
        onListeningStateChanged(value)
    }

    fun getRuntimeHealth(): String {
        return when {
            !hasMicrophonePermission() ->
                "Microphone permission required"
            !isAvailable() ->
                "Speech recognition unavailable"
            listening ->
                "Listening"
            else ->
                "Ready"
        }
    }

    fun isAvailable(): Boolean {
        return SpeechRecognizer.isRecognitionAvailable(appContext)
    }

    fun hasMicrophonePermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            appContext,
            Manifest.permission.RECORD_AUDIO
        ) == PackageManager.PERMISSION_GRANTED
    }

    fun startListening() {
        if (listening) return

        if (!hasMicrophonePermission()) {
            onError("Microphone permission is required.")
            updateListeningState(false)
            return
        }

        if (!isAvailable()) {
            onError("Speech recognition is not available on this device.")
            return
        }

        if (speechRecognizer == null) {
            speechRecognizer =
                SpeechRecognizer.createSpeechRecognizer(appContext)

            speechRecognizer?.setRecognitionListener(this)
        }

        val intent = Intent(
            RecognizerIntent.ACTION_RECOGNIZE_SPEECH
        ).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )

            putExtra(
                RecognizerIntent.EXTRA_PARTIAL_RESULTS,
                true
            )
        }

        updateListeningState(true)

        speechRecognizer?.startListening(intent)
    }

    fun stopListening() {
        updateListeningState(false)
        speechRecognizer?.stopListening()
    }

    fun shutdown() {
        updateListeningState(false)
        speechRecognizer?.cancel()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }

    override fun onReadyForSpeech(params: Bundle?) {
        updateListeningState(true)
    }

    override fun onBeginningOfSpeech() {
        updateListeningState(true)
    }

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        updateListeningState(false)
    }

    override fun onError(error: Int) {
        updateListeningState(false)

        onError(
            when (error) {
                SpeechRecognizer.ERROR_AUDIO ->
                    "Audio recording error."

                SpeechRecognizer.ERROR_CLIENT ->
                    "Speech recognition client error."

                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS ->
                    "Microphone permission denied."

                SpeechRecognizer.ERROR_NETWORK ->
                    "Speech recognition network error."

                SpeechRecognizer.ERROR_NETWORK_TIMEOUT ->
                    "Speech recognition network timeout."

                SpeechRecognizer.ERROR_NO_MATCH ->
                    "No speech recognized."

                SpeechRecognizer.ERROR_RECOGNIZER_BUSY ->
                    "Speech recognizer is busy."

                SpeechRecognizer.ERROR_SERVER ->
                    "Speech recognition server error."

                SpeechRecognizer.ERROR_SPEECH_TIMEOUT ->
                    "No speech detected."

                else ->
                    "Unknown speech recognition error."
            }
        )
    }

    override fun onResults(results: Bundle?) {
        updateListeningState(false)

        val matches =
            results?.getStringArrayList(
                SpeechRecognizer.RESULTS_RECOGNITION
            )

        val command =
            matches
                ?.firstOrNull()
                ?.trim()
                .orEmpty()

        if (command.isNotBlank()) {
            onResult(command)
        } else {
            onError("No voice command recognized.")
        }
    }

    override fun onPartialResults(
        partialResults: Bundle?
    ) = Unit

    override fun onEvent(
        eventType: Int,
        params: Bundle?
    ) = Unit
}
