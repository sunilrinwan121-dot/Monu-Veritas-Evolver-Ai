package com.monu.mobile.util

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
