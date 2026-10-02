package com.monu.mobile.domain.model

enum class HomeBackgroundMode {
    DEFAULT,
    SOLID_COLOR,
    IMAGE,
    GENERATED_IMAGE
}

data class HomeAppearance(
    val mode: HomeBackgroundMode = HomeBackgroundMode.DEFAULT,
    val backgroundValue: String? = null,
    val title: String = "MONU",
    val subtitle: String = "Your Mobile Command Center"
)

data class HomeQuickAction(
    val id: String,
    val title: String,
    val description: String,
    val destination: String
)
