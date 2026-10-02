package com.monu.mobile.feature.permissions

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import com.monu.mobile.domain.model.MONUPermission
import com.monu.mobile.domain.model.MONUPermissionStatus

class MONUPermissionCenter(
    private val context: Context
) {

    fun inspect(): List<MONUPermission> {
        return listOf(
            permission(
                "camera",
                Manifest.permission.CAMERA,
                "Camera",
                "Required only for camera features."
            ),
            permission(
                "audio",
                Manifest.permission.RECORD_AUDIO,
                "Microphone",
                "Required only for voice input."
            ),
            permission(
                "notifications",
                Manifest.permission.POST_NOTIFICATIONS,
                "Notifications",
                "Required for Android notification delivery."
            )
        )
    }

    private fun permission(
        id: String,
        permission: String,
        title: String,
        description: String
    ): MONUPermission {
        val granted =
            ContextCompat.checkSelfPermission(
                context,
                permission
            ) == PackageManager.PERMISSION_GRANTED

        return MONUPermission(
            id = id,
            androidPermission = permission,
            title = title,
            description = description,
            status = if (granted) {
                MONUPermissionStatus.GRANTED
            } else {
                MONUPermissionStatus.DENIED
            }
        )
    }
}
