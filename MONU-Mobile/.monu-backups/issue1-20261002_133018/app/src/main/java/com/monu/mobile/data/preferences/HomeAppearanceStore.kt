package com.monu.mobile.data.preferences

import android.content.Context
import android.content.SharedPreferences
import com.monu.mobile.domain.model.HomeAppearance
import com.monu.mobile.domain.model.HomeBackgroundMode

class HomeAppearanceStore(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(
            "monu_home_appearance",
            Context.MODE_PRIVATE
        )

    fun save(appearance: HomeAppearance) {
        preferences.edit()
            .putString("mode", appearance.mode.name)
            .putString("background", appearance.backgroundValue)
            .putString("title", appearance.title)
            .putString("subtitle", appearance.subtitle)
            .apply()
    }

    fun load(): HomeAppearance {
        val modeName = preferences.getString(
            "mode",
            HomeBackgroundMode.DEFAULT.name
        ) ?: HomeBackgroundMode.DEFAULT.name

        return HomeAppearance(
            mode = runCatching {
                HomeBackgroundMode.valueOf(modeName)
            }.getOrDefault(HomeBackgroundMode.DEFAULT),
            backgroundValue = preferences.getString(
                "background",
                null
            ),
            title = preferences.getString(
                "title",
                "MONU"
            ) ?: "MONU",
            subtitle = preferences.getString(
                "subtitle",
                "Your Mobile Command Center"
            ) ?: "Your Mobile Command Center"
        )
    }
}
