package com.monu.mobile.feature.capabilities

import android.content.Context
import android.content.pm.PackageManager
import android.hardware.camera2.CameraManager
import com.monu.mobile.domain.model.MONUCapabilityStatus
import com.monu.mobile.domain.model.MONUDeviceCapability

class MONUDeviceCapabilityInspector(
    private val context: Context
) {

    fun inspect(): List<MONUDeviceCapability> {

        val packageManager = context.packageManager

        val hasCamera =
            packageManager.hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY
            )

        val hasMicrophone =
            packageManager.hasSystemFeature(
                PackageManager.FEATURE_MICROPHONE
            )

        val cameraCount = try {
            val manager =
                context.getSystemService(Context.CAMERA_SERVICE)
                    as CameraManager
            manager.cameraIdList.size
        } catch (_: Exception) {
            0
        }

        return listOf(
            MONUDeviceCapability(
                id = "camera",
                title = "Camera Hardware",
                description = "Detected cameras: $cameraCount",
                status = if (hasCamera)
                    MONUCapabilityStatus.AVAILABLE
                else
                    MONUCapabilityStatus.UNAVAILABLE,
                verified = true
            ),
            MONUDeviceCapability(
                id = "microphone",
                title = "Microphone Hardware",
                description = "Device microphone hardware availability.",
                status = if (hasMicrophone)
                    MONUCapabilityStatus.AVAILABLE
                else
                    MONUCapabilityStatus.UNAVAILABLE,
                verified = true
            ),
            MONUDeviceCapability(
                id = "storage",
                title = "Application Storage",
                description = "Application sandbox storage is available.",
                status = MONUCapabilityStatus.AVAILABLE,
                verified = true
            ),
            MONUDeviceCapability(
                id = "root",
                title = "Root Authority",
                description = "Normal APK does not assume root authority.",
                status = MONUCapabilityStatus.UNKNOWN,
                verified = false
            )
        )
    }
}
