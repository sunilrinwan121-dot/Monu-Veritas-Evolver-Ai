package com.monu.mobile.feature.settings

import com.monu.mobile.domain.model.MONUSettingItem
import com.monu.mobile.domain.model.MONUSettings

class MONUSettingsCenter {

    fun currentSettings(): MONUSettings {
        return MONUSettings()
    }

    fun availableSettings(): List<MONUSettingItem> {
        val settings = currentSettings()

        return listOf(
            MONUSettingItem(
                id = "appearance",
                title = "Appearance",
                description = "Theme and visual customization.",
                enabled = true
            ),
            MONUSettingItem(
                id = "server",
                title = "Server Configuration",
                description = "Connected MONU Server settings.",
                enabled = settings.serverConfigured
            ),
            MONUSettingItem(
                id = "notifications",
                title = "Notifications",
                description = "MONU notification preferences.",
                enabled = settings.notificationsEnabled
            ),
            MONUSettingItem(
                id = "voice",
                title = "Voice",
                description = "Text-to-Speech and voice interaction.",
                enabled = settings.voiceEnabled
            ),
            MONUSettingItem(
                id = "offline",
                title = "Offline Mode",
                description = "Persistent command queue behavior.",
                enabled = settings.offlineModeEnabled
            )
        )
    }
}
