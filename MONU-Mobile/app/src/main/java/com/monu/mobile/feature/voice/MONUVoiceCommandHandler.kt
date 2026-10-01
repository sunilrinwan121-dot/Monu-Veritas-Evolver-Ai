package PACKAGE_NAME_PLACEHOLDER

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow

class MONUVoiceCommandHandler(private val context: Context) {

    private var speechRecognizer: SpeechRecognizer? = null
    private val _speechResults = MutableSharedFlow<String>()
    val speechResults: SharedFlow<String> = _speechResults.asSharedFlow()

    fun startListening() {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) return

        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
            setRecognitionListener(object : RecognitionListener {
                override fun onReadyForSpeech(params: Bundle?) {}
                override fun onBeginningOfSpeech() {}
                override fun onRmsChanged(rmsdB: Float) {}
                override fun onBufferReceived(buffer: ByteArray?) {}
                override fun onEndOfSpeech() {}
**Point 4** आपके प्रोजेक्ट में सफलतापूर्वक लागू हो गया है। `MONUNetworkObserver.kt` के जुड़ जाने से अब ऐप नेटवर्क कनेक्टिविटी को रियल-टाइम में मॉनिटर करेगा और इंटरनेट बंद होने पर ऑफ़लाइन मोड में सेफ़ स्विच हो जाएगा।

अब अगली प्राथमिक समस्या **Point 5 (Resource Management & Memory Leak Prevention / ऑटोमैटिक रिसोर्स क्लीनअप)** को ठीक करने के लिए नीचे दी गई कमांड चलाएँ:

```bash
cat << 'EOF' > fix_point5.sh
#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fix for Point 5...          "
echo "=========================================="

PACKAGE_DIR=$(find app/src/main/java -type d -name "util" -o -name "sys" 2>/dev/null | head -n 1)

if [ -z "$PACKAGE_DIR" ]; then
    BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
    PACKAGE_DIR="${BASE_DIR}/util"
    mkdir -p "$PACKAGE_DIR"
fi

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUResourceManager.kt"
package PACKAGE_NAME_PLACEHOLDER

import android.content.Context
import android.os.Debug
import java.lang.ref.WeakReference

/**
 * Resource Management & Memory Leak Prevention Engine (Fix for Point 5)
 * Monitors heap allocation and prevents context/memory leaks using weak references.
 */
object MONUResourceManager {

    private var contextRef: WeakReference<Context>? = null

    fun initialize(context: Context) {
        contextRef = WeakReference(context.applicationContext)
    }

    fun getAvailableMemoryMB(): Long {
        val runtime = Runtime.getRuntime()
        val usedMem = runtime.totalMemory() - runtime.freeMemory()
        val maxMem = runtime.maxMemory()
        return (maxMem - usedMem) / (1024 * 1024)
    }

    fun performMemoryCleanup() {
        System.gc()
        Runtime.getRuntime().gc()
    }

    fun isNativeHeapMemoryAvailable(): Boolean {
        val allocatedNative = Debug.getNativeHeapAllocatedSize()
        val maxNative = Debug.getNativeHeapSize()
        return (maxNative - allocatedNative) > 10 * 1024 * 1024
    }
}
