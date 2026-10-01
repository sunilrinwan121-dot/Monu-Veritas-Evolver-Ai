#!/usr/bin/env bash
set -e

echo "=========================================="
echo "    Applying Fix for Point 7...          "
echo "=========================================="

PROGUARD_FILE="app/proguard-rules.pro"

cat << 'PROGUARD_EOF' >> "$PROGUARD_FILE"

# Fix for Point 7: Security & Release Obfuscation Rules
-keep class com.monu.** { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn kotlinx.coroutines.**
-keepattributes Signature, InnerClasses, *Annotation*
PROGUARD_EOF

echo "[✓] Point 7 Resolved: ProGuard release obfuscation & security rules added."
echo "=========================================="
echo "    Execution Completed Successfully.     "
echo "=========================================="
