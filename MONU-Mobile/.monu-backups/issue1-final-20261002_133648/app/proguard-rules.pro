
# Fix for Point 7: Security & Release Obfuscation Rules
-keep class com.monu.** { *; }
-keepclassmembers class * {
    @androidx.annotation.Keep *;
}
-dontwarn okhttp3.**
-dontwarn retrofit2.**
-dontwarn kotlinx.coroutines.**
-keepattributes Signature, InnerClasses, *Annotation*
