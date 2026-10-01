#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fix for Point 5...          "
echo "=========================================="

BASE_DIR=$(find app/src/main/java -mindepth 3 -maxdepth 5 -type d | head -n 1)
PACKAGE_DIR="${BASE_DIR}/util"
mkdir -p "$PACKAGE_DIR"

PACKAGE_NAME=$(echo "$PACKAGE_DIR" | sed 's/.*app\/src\/main\/java\///' | tr '/' '.')

cat << 'KOTLIN_EOF' > "$PACKAGE_DIR/MONUResourceManager.kt"
package PACKAGE_NAME_PLACEHOLDER

import android.content.Context
import android.os.Debug
import java.lang.ref.WeakReference

/**
 * Resource Management & Memory Leak Prevention Engine (Fix for Point 5)
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
KOTLIN_EOF

sed -i "s/PACKAGE_NAME_PLACEHOLDER/$PACKAGE_NAME/g" "$PACKAGE_DIR/MONUResourceManager.kt"

echo "[✓] Point 5 Resolved: MONUResourceManager implemented successfully."
echo "=========================================="
echo "    Execution Completed Successfully.     "
echo "=========================================="
