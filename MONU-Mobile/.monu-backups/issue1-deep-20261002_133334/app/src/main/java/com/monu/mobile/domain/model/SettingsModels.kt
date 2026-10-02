package com.monu.mobile.domain.model

enum class MONUThemeMode {
    SYSTEM,
    LIGHT,
    DARK,
    CUSTOM
}

data class MONUSettings(
    val themeMode: MONUThemeMode = MONUThemeMode.SYSTEM,
    val serverConfigured: Boolean = false,
    val notificationsEnabled: Boolean = false,
    val voiceEnabled: Boolean = true,
    val realtimeEnabled: Boolean = false,
    val offlineModeEnabled: Boolean = true
)

data class MONUSettingItem(
    val id: String,
    val title: String,
    val description: String,
    val enabled: Boolean
)
