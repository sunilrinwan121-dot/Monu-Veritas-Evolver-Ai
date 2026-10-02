package com.monu.mobile.util

import android.content.Context
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

/**
 * Unified Global Error Handler Engine (Fix for Point 9)
 * Prevents error swallowing and ensures user notification and proper logging.
 */
object MONUErrorHandler {

    private const val TAG = "MONUErrorHandler"
    private val _errorEvents = MutableSharedFlow<String>()
    val errorEvents: SharedFlow<String> = _errorEvents.asSharedFlow()

    fun handleError(context: Context?, throwable: Throwable, customMessage: String? = null) {
        val displayMessage = customMessage ?: throwable.localizedMessage ?: "An unexpected error occurred."
        Log.e(TAG, "Captured Error: ${throwable.message}", throwable)

        CoroutineScope(Dispatchers.Main).launch {
            context?.let {
                Toast.makeText(it, displayMessage, Toast.LENGTH_SHORT).show()
            }
            _errorEvents.emit(displayMessage)
        }
    }

    fun logAndNotify(context: Context?, message: String) {
        Log.w(TAG, "Error Event: $message")
        CoroutineScope(Dispatchers.Main).launch {
            context?.let {
                Toast.makeText(it, message, Toast.LENGTH_SHORT).show()
            }
            _errorEvents.emit(message)
        }
    }
}
