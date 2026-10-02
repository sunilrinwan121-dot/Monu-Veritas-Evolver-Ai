package com.monu.mobile.feature.home

import com.monu.mobile.domain.model.HomeAppearance
import com.monu.mobile.domain.model.HomeBackgroundMode

class MONUHomePersonalization {

    fun defaultAppearance(): HomeAppearance {
        return HomeAppearance(
            mode = HomeBackgroundMode.DEFAULT,
            title = "MONU",
            subtitle = "Your Mobile Command Center"
        )
    }

    fun imageAppearance(
        imageUri: String
    ): HomeAppearance {
        return HomeAppearance(
            mode = HomeBackgroundMode.IMAGE,
            backgroundValue = imageUri,
            title = "MONU",
            subtitle = "Personalized Command Center"
        )
    }

    fun generatedImageAppearance(
        imageReference: String
    ): HomeAppearance {
        return HomeAppearance(
            mode = HomeBackgroundMode.GENERATED_IMAGE,
            backgroundValue = imageReference,
            title = "MONU",
            subtitle = "AI Personalized Environment"
        )
    }
}
