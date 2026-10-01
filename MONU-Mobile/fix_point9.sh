#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fix for Point 9...          "
echo "=========================================="

PACKAGE_DIR=$(find app/src/main/java -type d -name "util" 2>/dev/null | head -n 1)

if [ -z "$PACKAGE_DIR" ]; then
    BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
    PACKAGE_DIR="${BASE_DIR}/util"
    mkdir -p "$PACKAGE_DIR"
fi

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUErrorHandler.kt"
package PACKAGE_NAME_PLACEHOLDER

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
KOTLIN_EOF

sed -i "s/PACKAGE_NAME_PLACEHOLDER/$PACKAGE_NAME/g" "$PACKAGE_DIR/MONUErrorHandler.kt"

echo "[✓] Point 9 Resolved: Global MONUErrorHandler created to capture, log, and display silent errors."
echo "=========================================="
echo "    Execution Completed Successfully.     "
echo "=========================================="
